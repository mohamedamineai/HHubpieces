// ==================== HUBPIECES - PROTECTION DES PAGES ====================

// Pages par role
const PAGE_ROLE_MAP = {
    "dashboard-demandeur.html": "DEMANDEUR",
    "dashboard-maker.html": "MAKER",
    "dashboard-expertcao.html": "EXPERT_CAO",
    "dashboard-admin.html": "ADMIN",
    "mes-demandes.html": "DEMANDEUR",
    "creer-demande.html": "DEMANDEUR",
    "mes-offres.html": "MAKER",
    "mes-modelisations.html": "EXPERT_CAO",
    "gestion-admin.html": "ADMIN",
    "mon-profil.html": null  // Tous les roles connectes
};

const REDIRECT_BY_ROLE = {
    DEMANDEUR: "dashboard-demandeur.html",
    MAKER: "dashboard-maker.html",
    EXPERT_CAO: "dashboard-expertcao.html",
    ADMIN: "dashboard-admin.html"
};

async function guardPage() {
    // Ne pas bloquer si ouvert en local en file://
    if (window.location.protocol === "file:") return;

    const currentPage = window.location.pathname.split("/").pop();
    const requiredRole = PAGE_ROLE_MAP[currentPage];

    // Si la page n'est pas protegee, on sort
    if (requiredRole === undefined) return;

    try {
        const res = await fetch("/api/auth/me", { credentials: "same-origin" });

        if (!res.ok) {
            // Si session expiree ou non connecte, redirection vers login
            window.location.href = "login.html";
            return;
        }

        const user = await res.json();

        // L'admin a acces a tout
        if (user.role === "ADMIN") return;

        // Si la page requiert un role specifique
        if (requiredRole !== null && user.role !== requiredRole) {
            window.location.href = REDIRECT_BY_ROLE[user.role] || "index.html";
        }
    } catch (e) {
        // En cas d'erreur reseau temporaire, ne pas faire de boucle si deja sur la page
        console.warn("auth-guard: verification de session non disponible", e);
    }
}

document.addEventListener("DOMContentLoaded", guardPage);
