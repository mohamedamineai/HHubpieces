package com.hubpieces.service;

import com.hubpieces.model.*;
import com.hubpieces.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthSessionTest {
    private AuthService service;
    private UtilisateurRepository users;
    private Utilisateur user;
    @BeforeEach void setup() {
        users = mock(UtilisateurRepository.class);
        service = new AuthService(users, mock(RoleRepository.class), mock(ProfilMakerRepository.class),
                mock(ProfilExpertCaoRepository.class), mock(PasswordEncoder.class));
        user = new Utilisateur("Nom", "Prénom actuel", "user@example.test", "hash"); user.setId(1L);
        Role role = mock(Role.class); when(role.getNom()).thenReturn(RoleNom.DEMANDEUR);
        user.setRoles(Set.of(role)); when(users.findById(1L)).thenReturn(Optional.of(user));
    }
    @Test void returnsCurrentDatabaseIdentity() {
        var result = service.currentUser(1L, "DEMANDEUR");
        assertTrue(result.isSuccess()); assertEquals("Prénom actuel", result.getPrenom());
    }
    @Test void rejectsSuspendedSession() {
        user.setStatut(StatutUtilisateur.SUSPENDU);
        assertEquals(401, assertThrows(ResponseStatusException.class,
                () -> service.currentUser(1L,"DEMANDEUR")).getStatusCode().value());
    }
    @Test void rejectsRevokedRole() {
        assertEquals(401, assertThrows(ResponseStatusException.class,
                () -> service.currentUser(1L,"ADMIN")).getStatusCode().value());
    }
    @Test void rejectsDeletedUser() {
        when(users.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> service.currentUser(1L,"DEMANDEUR"));
    }
    @Test void rejectsMissingSession() {
        assertThrows(ResponseStatusException.class, () -> service.currentUser(null,null));
        verify(users,never()).findById(any());
    }
}
