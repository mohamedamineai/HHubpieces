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
    const currentPage = window.location.pathname.split("/").pop();
    const requiredRole = PAGE_ROLE_MAP[currentPage];

    // Si la page n'est pas protegee, on sort
    if (requiredRole === undefined) return;

    try {
        const res = await fetch("/api/auth/me", { credentials: "same-origin" });

        if (!res.ok) {
            window.location.href = "login.html";
            return;
        }

        const user = await res.json();

        // Si la page requiert un role specifique
        if (requiredRole !== null && user.role !== requiredRole) {
            // Si admin, on lui laisse l'acces a tout (mais il sera redirige vers son dashboard)
            if (user.role === "ADMIN") {
                // Admin peut acceder aux pages admin uniquement
                if (requiredRole !== "ADMIN") {
                    window.location.href = REDIRECT_BY_ROLE[user.role];
                }
                return;
            }
            window.location.href = REDIRECT_BY_ROLE[user.role] || "index.html";
        }
    } catch (e) {
        window.location.href = "login.html";
    }
}

document.addEventListener("DOMContentLoaded", guardPage);
