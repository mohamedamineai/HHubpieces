// ==================== HUBPIECES - PROTECTION DES PAGES DASHBOARD ====================

const DASHBOARD_ROLE_MAP = {
    "dashboard-demandeur.html": "DEMANDEUR",
    "dashboard-maker.html": "MAKER",
    "dashboard-expertcao.html": "EXPERT_CAO",
    "dashboard-admin.html": "ADMIN"
};

const REDIRECT_BY_ROLE = {
    DEMANDEUR: "dashboard-demandeur.html",
    MAKER: "dashboard-maker.html",
    EXPERT_CAO: "dashboard-expertcao.html",
    ADMIN: "dashboard-admin.html"
};

async function guardDashboard() {
    const currentPage = window.location.pathname.split("/").pop();
    const requiredRole = DASHBOARD_ROLE_MAP[currentPage];
    if (!requiredRole) return;

    try {
        const res = await fetch("/api/auth/me", { credentials: "same-origin" });

        if (!res.ok) {
            window.location.href = "login.html";
            return;
        }

        const user = await res.json();

        if (user.role !== requiredRole) {
            window.location.href = REDIRECT_BY_ROLE[user.role] || "index.html";
        }
    } catch (e) {
        window.location.href = "login.html";
    }
}

document.addEventListener("DOMContentLoaded", guardDashboard);
