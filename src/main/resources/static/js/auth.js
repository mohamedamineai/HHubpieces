// ==================== HUBPIECES - AUTH.JS ====================

const DASHBOARD_BY_ROLE = {
    DEMANDEUR: "dashboard-demandeur.html",
    MAKER: "dashboard-maker.html",
    EXPERT_CAO: "dashboard-expertcao.html",
    ADMIN: "dashboard-admin.html"
};

function redirectByRole(role) {
    window.location.href = DASHBOARD_BY_ROLE[role] || "index.html";
}

async function redirectIfLoggedIn() {
    try {
        const res = await fetch("/api/auth/me", { credentials: "same-origin" });
        if (res.ok) {
            const user = await res.json();
            if (user && user.role) {
                redirectByRole(user.role);
            }
        }
    } catch (e) {
        // utilisateur non connecte, on reste sur la page
    }
}

function initRoleTabs() {
    const tabs = document.querySelectorAll(".role-tab");
    const fieldGroups = document.querySelectorAll(".role-fields");
    const roleInput = document.getElementById("roleInput");

    tabs.forEach(tab => {
        tab.addEventListener("click", () => {
            tabs.forEach(t => t.classList.remove("active"));
            tab.classList.add("active");

            const role = tab.getAttribute("data-role");
            if (roleInput) roleInput.value = role;

            fieldGroups.forEach(fg => fg.classList.remove("active"));
            const target = document.querySelector(`.role-fields[data-for="${role}"]`);
            if (target) target.classList.add("active");
        });
    });
}

function showMessage(el, type, text) {
    el.className = "auth-msg " + type;
    el.textContent = text;
}

function initRegisterForm() {
    const form = document.getElementById("registerForm");
    if (!form) return;

    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const msgEl = document.getElementById("authMsg");
        const formData = new FormData(form);
        const payload = Object.fromEntries(formData.entries());

        if (payload.motDePasse !== payload.confirmMotDePasse) {
            showMessage(msgEl, "error", "Les mots de passe ne correspondent pas.");
            return;
        }
        delete payload.confirmMotDePasse;

        if (payload.rayonActionKm) payload.rayonActionKm = parseInt(payload.rayonActionKm);

        try {
            const res = await fetch("/api/auth/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });
            const data = await res.json();

            if (data.success) {
                showMessage(msgEl, "success", data.message + " - redirection...");
                setTimeout(() => redirectByRole(data.role), 1200);
            } else {
                showMessage(msgEl, "error", data.message);
            }
        } catch (err) {
            showMessage(msgEl, "error", "Erreur serveur. Reessayez.");
        }
    });
}

function initLoginForm() {
    const form = document.getElementById("loginForm");
    if (!form) return;

    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const msgEl = document.getElementById("authMsg");
        const formData = new FormData(form);
        const payload = Object.fromEntries(formData.entries());

        try {
            const res = await fetch("/api/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });
            const data = await res.json();

            if (data.success) {
                showMessage(msgEl, "success", "Connexion reussie - redirection...");
                setTimeout(() => redirectByRole(data.role), 1000);
            } else {
                showMessage(msgEl, "error", data.message);
            }
        } catch (err) {
            showMessage(msgEl, "error", "Erreur serveur. Reessayez.");
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    redirectIfLoggedIn();
    initRoleTabs();
    initRegisterForm();
    initLoginForm();
});
