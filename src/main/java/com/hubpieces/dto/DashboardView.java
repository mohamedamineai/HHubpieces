package com.hubpieces.dto;

import com.hubpieces.model.EtatDemande;
import java.util.List;
import java.util.Map;

public record DashboardView(long total, Map<EtatDemande, Long> parEtat, List<DemandeView> recentes) {}
