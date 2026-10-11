package com.hubpieces.service;

import com.hubpieces.dto.DemandeInput;
import com.hubpieces.model.*;
import com.hubpieces.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DemandeServiceTest {
    private DemandeRepository demandes;
    private UtilisateurRepository utilisateurs;
    private DemandeService service;
    private Utilisateur user;
    @BeforeEach void setup() {
        demandes = mock(DemandeRepository.class);
        utilisateurs = mock(UtilisateurRepository.class);
        service = new DemandeService(demandes, utilisateurs);
        user = new Utilisateur(); user.setId(1L);
        Role role = mock(Role.class); when(role.getNom()).thenReturn(RoleNom.DEMANDEUR);
        user.setRoles(Set.of(role));
        when(utilisateurs.findById(1L)).thenReturn(Optional.of(user));
    }
    @Test void anonymousCannotList() {
        var error = assertThrows(ResponseStatusException.class, () -> service.list(null, 0, 12));
        assertEquals(401, error.getStatusCode().value()); verifyNoInteractions(demandes);
    }
    @Test void suspendedCannotList() {
        user.setStatut(StatutUtilisateur.SUSPENDU);
        assertEquals(403, assertThrows(ResponseStatusException.class, () -> service.list(1L, 0, 12)).getStatusCode().value());
    }
    @Test void wrongRoleCannotList() {
        user.setRoles(Set.of());
        assertEquals(403, assertThrows(ResponseStatusException.class, () -> service.list(1L, 0, 12)).getStatusCode().value());
    }
    @Test void cannotReadAnotherOwnersRequest() {
        when(demandes.findByIdAndDemandeurId(9L,1L)).thenReturn(Optional.empty());
        assertEquals(404, assertThrows(ResponseStatusException.class, () -> service.get(9L,1L)).getStatusCode().value());
    }
    @Test void creationAssignsOwnerAndInitialState() {
        when(demandes.save(any(Demande.class))).thenAnswer(call -> call.getArgument(0));
        var result = service.create(1L, new DemandeInput(" Bouton ", " Pièce cassée ", null,null,null,100.0," Tanger ",null));
        assertEquals("Bouton", result.titre()); assertEquals(EtatDemande.NOUVELLE, result.etat());
        verify(demandes).save(argThat(d -> d.getDemandeur() == user && d.getVille().equals("Tanger")));
    }
    @Test void cancellationIsIdempotent() {
        Demande d = new Demande("Bouton", "Description", user);
        when(demandes.findByIdAndDemandeurId(9L,1L)).thenReturn(Optional.of(d));
        when(demandes.save(d)).thenReturn(d);
        assertEquals(EtatDemande.ANNULEE, service.cancel(9L,1L).etat());
        assertEquals(EtatDemande.ANNULEE, service.cancel(9L,1L).etat());
        verify(demandes, times(1)).save(d);
    }
    @Test void cannotCancelAnEngagedRequest() {
        for (EtatDemande state : EtatDemande.values()) {
            if (state == EtatDemande.NOUVELLE || state == EtatDemande.ANNULEE) continue;
            Demande d = new Demande("Bouton", "Description", user); d.setEtat(state);
            when(demandes.findByIdAndDemandeurId(9L,1L)).thenReturn(Optional.of(d));
            assertEquals(409, assertThrows(ResponseStatusException.class, () -> service.cancel(9L,1L)).getStatusCode().value());
        }
        verify(demandes, never()).save(any());
    }
    @Test void paginationIsBounded() {
        assertEquals(400, assertThrows(ResponseStatusException.class, () -> service.list(1L,-1,12)).getStatusCode().value());
        assertEquals(400, assertThrows(ResponseStatusException.class, () -> service.list(1L,0,51)).getStatusCode().value());
    }
    @Test void dashboardDefaultsEveryStateToZero() {
        when(demandes.countByEtat(1L)).thenReturn(List.of());
        when(demandes.findByDemandeurId(eq(1L), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(org.springframework.data.domain.Page.empty());
        var result = service.dashboard(1L);
        assertEquals(0L, result.total());
        assertEquals(EtatDemande.values().length, result.parEtat().size());
        assertTrue(result.parEtat().values().stream().allMatch(n -> n == 0L));
        assertTrue(result.recentes().isEmpty());
    }
    @Test void dashboardAggregatesOnlyTheOwnersData() {
        var row = mock(DemandeRepository.EtatCount.class);
        when(row.getEtat()).thenReturn(EtatDemande.NOUVELLE); when(row.getNombre()).thenReturn(8L);
        when(demandes.countByEtat(1L)).thenReturn(List.of(row));
        Demande d = new Demande("Bouton", "Description", user);
        when(demandes.findByDemandeurId(eq(1L), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(d)));
        var result = service.dashboard(1L);
        assertEquals(8L, result.total()); assertEquals(Long.valueOf(8), result.parEtat().get(EtatDemande.NOUVELLE));
        assertEquals("Bouton", result.recentes().get(0).titre());
        verify(demandes).findByDemandeurId(eq(1L), argThat(page -> page.getPageSize() == 5 && page.getPageNumber() == 0));
    }
    @Test void dashboardRequiresSession() {
        assertEquals(401, assertThrows(ResponseStatusException.class, () -> service.dashboard(null)).getStatusCode().value());
        verifyNoInteractions(demandes);
    }

}
