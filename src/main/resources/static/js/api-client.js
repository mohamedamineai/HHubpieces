/* Client same-origin : session, CSRF et erreurs structurées. Aucun jeton en localStorage. */
window.HubApi = (() => {
    async function request(path, options = {}) {
        if (!path.startsWith('/api/')) throw new Error('Seules les API locales sont autorisées.');
        const method = (options.method || 'GET').toUpperCase();
        const headers = new Headers(options.headers || {});
        if (!['GET', 'HEAD', 'OPTIONS'].includes(method)) {
            const token = await request('/api/csrf');
            headers.set(token.headerName, token.token);
        }
        const response = await fetch(path, {...options, method, headers, credentials:'same-origin'});
        const data = await response.json().catch(() => ({}));
        if (!response.ok) {
            const message = data.fields
                ? Object.entries(data.fields).map(([field, error]) => `${field} : ${error}`).join('\n')
                : data.message || `Requête refusée (${response.status}). Actualisez la page puis réessayez.`;
            const error = new Error(message); error.status = response.status; throw error;
        }
        return data;
    }
    return {request, post: (path, body) => request(path, {
        method:'POST', headers:{'Content-Type':'application/json'},
        body: body === undefined ? undefined : JSON.stringify(body)
    })};
})();
