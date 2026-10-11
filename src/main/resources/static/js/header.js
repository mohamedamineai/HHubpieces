// ==================== HUBPIECES - HEADER DYNAMIQUE SELON LE ROLE ====================

const NAV_CONFIG = {
    GUEST: {
        links: [
            { href: "index.html#accueil", icon: "fa-home", label: "accueil" },
            { href: "index.html#catalogue", icon: "fa-cube", label: "catalogue stl" },
            { href: "index.html#makers", icon: "fa-print", label: "makers" },
            { href: "index.html#experts", icon: "fa-drafting-compass", label: "experts cao" },
            { href: "index.html#avis", icon: "fa-star", label: "avis" }
        ]
    },
    DEMANDEUR: {
        dashboard: "dashboard-demandeur.html",
        links: [
            { href: "dashboard-demandeur.html#mes-demandes", icon: "fa-list", label: "mes demandes" },
            { href: "dashboard-demandeur.html#nouvelle-demande", icon: "fa-plus", label: "nouvelle demande" },
            { href: "index.html#catalogue", icon: "fa-cube", label: "catalogue stl" },
            { href: "dashboard-demandeur.html#messages", icon: "fa-envelope", label: "messages" }
        ]
    },
    MAKER: {
        dashboard: "dashboard-maker.html",
        links: [
            { href: "dashboard-maker.html#demandes-dispo", icon: "fa-search", label: "demandes dispo" },
            { href: "dashboard-maker.html#mes-offres", icon: "fa-handshake", label: "mes offres" },
            { href: "dashboard-maker.html#commandes", icon: "fa-box", label: "commandes" },
            { href: "dashboard-maker.html#messages", icon: "fa-envelope", label: "messages" }
        ]
    },
    EXPERT_CAO: {
        dashboard: "dashboard-expertcao.html",
        links: [
            { href: "dashboard-expertcao.html#mes-fichiers", icon: "fa-folder", label: "mes fichiers" },
            { href: "dashboard-expertcao.html#upload", icon: "fa-upload", label: "uploader stl" },
            { href: "dashboard-expertcao.html#portfolio", icon: "fa-image", label: "portfolio" },
            { href: "dashboard-expertcao.html#messages", icon: "fa-envelope", label: "messages" }
        ]
    },
    ADMIN: {
        dashboard: "dashboard-admin.html",
        links: [
            { href: "dashboard-admin.html#utilisateurs", icon: "fa-users", label: "utilisateurs" },
            { href: "dashboard-admin.html#litiges", icon: "fa-gavel", label: "litiges" },
            { href: "dashboard-admin.html#validation-stl", icon: "fa-check-circle", label: "validation stl" }
        ]
    }
};

const ROLE_LABELS = {
    DEMANDEUR: "Demandeur",
    MAKER: "Maker",
    EXPERT_CAO: "Expert CAO",
    ADMIN: "Administrateur"
};

async function fetchCurrentUser() {
    try {
        const res = await fetch("/api/auth/me", { credentials: "same-origin" });
        if (!res.ok) return null;
        return await res.json();
    } catch (e) {
        return null;
    }
}

function buildNavLinks(links) {
    return links.map(l => `
        <li class="nav-item">
            <a class="nav-link nav-pill-item" href="${l.href}">
                <i class="fas ${l.icon} me-1"></i>${l.label}
            </a>
        </li>
    `).join("");
}

function buildGuestActions() {
    return `
        <a href="login.html" class="btn btn-outline-glass">connexion</a>
        <a href="register.html" class="btn btn-orange-solid">inscription</a>
    `;
}

function buildUserActions(user) {
    const initiale = (user.prenom ? user.prenom.charAt(0) : "U").toUpperCase();
    const roleLabel = ROLE_LABELS[user.role] || user.role;
    const dashboardUrl = (NAV_CONFIG[user.role] && NAV_CONFIG[user.role].dashboard) || "index.html";

    return `
        <div class="user-menu-wrapper">
            <button class="user-menu-trigger" id="userMenuTrigger" type="button">
                <span class="user-avatar-small">${initiale}</span>
                <span class="user-menu-name">${user.prenom || ""}</span>
                <i class="fas fa-chevron-down ms-1 small"></i>
            </button>
            <div class="dropdown-glass" id="userDropdown">
                <div class="dropdown-header">
                    <strong>${user.prenom || ""} ${user.nom || ""}</strong>
                    <span class="role-badge-nav">${roleLabel}</span>
                </div>
                <a href="${dashboardUrl}" class="dropdown-item-glass">
                    <i class="fas fa-gauge me-2"></i>mon tableau de bord
                </a>
                <a class="dropdown-item-glass" id="logoutBtn">
                    <i class="fas fa-sign-out-alt me-2"></i>deconnexion
                </a>
            </div>
        </div>
    `;
}

async function renderHeader() {
    const placeholder = document.getElementById("navbar-placeholder");
    if (!placeholder) return;

    const user = await fetchCurrentUser();
    const role = (user && user.role) ? user.role : "GUEST";
    const config = NAV_CONFIG[role] || NAV_CONFIG.GUEST;

    placeholder.innerHTML = `
        <nav class="navbar navbar-expand-lg navbar-glass fixed-top">
            <div class="container">
                <a class="navbar-brand d-flex align-items-center gap-2" href="index.html">
                    <img src="images/logo_hub.jpeg" alt="hubpieces logo" class="nav-logo">
                    <span class="brand-hub">hub</span><span class="brand-pieces">pieces</span>
                </a>
                <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarnav">
                    <i class="fas fa-bars text-white"></i>
                </button>
                <div class="collapse navbar-collapse justify-content-center" id="navbarnav">
                    <ul class="navbar-nav nav-pills-glass mx-auto">
                        ${buildNavLinks(config.links)}
                    </ul>
                    <div class="d-flex align-items-center gap-2 mt-2 mt-lg-0">
                        ${user ? buildUserActions(user) : buildGuestActions()}
                    </div>
                </div>
            </div>
        </nav>
    `;

    initNavbarScrollEffect();

    if (user) {
        const trigger = document.getElementById("userMenuTrigger");
        const dropdown = document.getElementById("userDropdown");

        trigger.addEventListener("click", (e) => {
            e.stopPropagation();
            dropdown.classList.toggle("open");
        });

        document.addEventListener("click", (e) => {
            if (!trigger.contains(e.target) && !dropdown.contains(e.target)) {
                dropdown.classList.remove("open");
            }
        });

        document.getElementById("logoutBtn").addEventListener("click", async (e) => {
            e.preventDefault();
            await fetch("/api/auth/logout", { method: "POST", credentials: "same-origin" });
            window.location.href = "index.html";
        });
    }

    document.dispatchEvent(new CustomEvent("userLoaded", { detail: user }));
}

function initNavbarScrollEffect() {
    const navbar = document.querySelector(".navbar-glass");
    if (!navbar) return;
    window.addEventListener("scroll", () => {
        if (window.scrollY > 50) navbar.classList.add("scrolled");
        else navbar.classList.remove("scrolled");
    });
}

document.addEventListener("DOMContentLoaded", renderHeader);
