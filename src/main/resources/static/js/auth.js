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

        payload.rayonActionKm = payload.rayonActionKm ? Number(payload.rayonActionKm) : null;

        const submit = form.querySelector('[type="submit"]');
        if (submit.disabled) return;
        submit.disabled = true;
        try {
            const data = await HubApi.post("/api/auth/register", payload);

            if (data.success) {
                showMessage(msgEl, "success", data.message + " - redirection...");
                setTimeout(() => redirectByRole(data.role), 1200);
            } else {
                showMessage(msgEl, "error", data.message);
            }
        } catch (err) {
            showMessage(msgEl, "error", err.message || "Erreur serveur. Réessayez.");
        } finally {
            submit.disabled = false;
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

        const submit = form.querySelector('[type="submit"]');
        if (submit.disabled) return;
        submit.disabled = true;
        try {
            const data = await HubApi.post("/api/auth/login", payload);

            if (data.success) {
                showMessage(msgEl, "success", "Connexion reussie - redirection...");
                setTimeout(() => redirectByRole(data.role), 1000);
            } else {
                showMessage(msgEl, "error", data.message);
            }
        } catch (err) {
            showMessage(msgEl, "error", err.message || "Erreur serveur. Réessayez.");
        } finally {
            submit.disabled = false;
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    redirectIfLoggedIn();
    initRoleTabs();
    initRegisterForm();
    initLoginForm();
});
