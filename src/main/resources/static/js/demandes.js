'use strict';
const $ = id => document.getElementById(id);
let page = 0, token, selectedId;
const labels = {NOUVELLE:'Nouvelle', ANNULEE:'Annulée', MODELISATION_REQUISE:'Modélisation requise', RECHERCHE_MAKER:'Recherche maker', ATTENTE_VALIDATION:'En attente de validation', EN_COURS_IMPRESSION:'En impression', CLOTUREE:'Clôturée'};
async function api(url, options = {}) {
    const res = await fetch(url, {credentials:'same-origin', ...options});
    const data = await res.json().catch(() => ({}));
    if (res.status === 401) { location.href = 'login.html'; throw new Error('Connexion requise'); }
    if (!res.ok) throw new Error(data.fields ? Object.entries(data.fields).map(([k,v]) => `${k} : ${v}`).join('\n') : data.message || `Requête refusée (${res.status}). Actualisez la page puis réessayez.`);
    return data;
}
async function write(url, data) {
    token = await api('/api/csrf');
    return api(url, {method:'POST', headers:{'Content-Type':'application/json', [token.headerName]:token.token}, body:data ? JSON.stringify(data) : undefined});
}
function element(tag, text, cls) {
    const el = document.createElement(tag); el.textContent = text;
    if (cls) el.className = cls;
    return el;
}
async function load() {
    $('status').textContent = 'Chargement de vos demandes…';
    $('prev').disabled = $('next').disabled = true;
    try {
        const data = await api(`/api/demandes?page=${page}&size=12`);
        $('requests').replaceChildren(); $('count').textContent = data.totalElements;
        for (const d of data.content) {
            const card = element('article', '', 'request-card');
            card.append(element('span', labels[d.etat] || d.etat, 'badge'), element('h3', d.titre), element('p', `${d.ville} · ${new Date(d.dateCreation).toLocaleDateString('fr-FR')}`), element('p', d.description.length > 130 ? d.description.slice(0,130) + '…' : d.description));
            const actions = element('div', '', 'card-actions');
            const detail = element('button', 'Voir les détails', 'quiet');
            detail.onclick = () => showDetail(d.id); actions.append(detail);
            if (d.etat === 'NOUVELLE') {
                const cancel = element('button', 'Annuler', 'quiet');
                cancel.onclick = () => { selectedId = d.id; $('cancelError').textContent = ''; $('cancelDialog').showModal(); };
                actions.append(cancel);
            }
            card.append(actions); $('requests').append(card);
        }
        $('status').textContent = data.totalElements ? '' : 'Aucune demande pour le moment. Commencez par décrire votre première pièce.';
        $('page').textContent = `${page + 1} / ${Math.max(1, data.totalPages)}`;
        $('prev').disabled = page === 0; $('next').disabled = page + 1 >= data.totalPages;
    } catch (e) { $('status').textContent = e.message + ' Rechargez la page pour réessayer.'; }
}
async function showDetail(id) {
    try {
        const d = await api(`/api/demandes/${id}`);
        $('detailTitle').textContent = d.titre; $('detail').replaceChildren();
        const fields = {'État':labels[d.etat] || d.etat,'Description':d.description,'Marque':d.marque,'Modèle':d.modeleAppareil,'Matériau souhaité':d.materiauSouhaite,'Budget maximum':d.budgetMax,'Ville':d.ville,'Code postal':d.codePostal};
        for (const [name,value] of Object.entries(fields)) $('detail').append(element('dt',name),element('dd',value ?? 'Non renseigné'));
        $('detailDialog').showModal();
    } catch(e) { $('status').textContent = e.message; }
}
document.querySelectorAll('[data-close]').forEach(b => b.onclick = () => $(b.dataset.close).close());
$('new').onclick = () => { $('formError').textContent = ''; $('formDialog').showModal(); };
$('prev').onclick = () => { page--; load(); }; $('next').onclick = () => { page++; load(); };
$('requestForm').onsubmit = async e => {
    e.preventDefault(); $('submit').disabled = true; $('formError').textContent = '';
    const data = Object.fromEntries(new FormData(e.target));
    for (const key in data) data[key] = data[key].trim() || null;
    data.budgetMax = data.budgetMax === null ? null : Number(data.budgetMax);
    try { await write('/api/demandes', data); $('formDialog').close(); e.target.reset(); page = 0; await load(); }
    catch(err) { $('formError').textContent = err.message; }
    finally { $('submit').disabled = false; }
};
$('confirmCancel').onclick = async () => {
    $('confirmCancel').disabled = true;
    try { await write(`/api/demandes/${selectedId}/annulation`); $('cancelDialog').close(); await load(); }
    catch(e) { $('cancelError').textContent = e.message; }
    finally { $('confirmCancel').disabled = false; }
};
(async () => {
    try {
        const user = await api('/api/auth/me');
        if (user.role !== 'DEMANDEUR') { $('status').textContent = 'Cet espace est réservé aux demandeurs.'; return; }
        $('new').disabled = false; await load();
        const params = new URLSearchParams(location.search);
        if (params.get('nouvelle') === '1') $('new').click();
        else if (/^[1-9][0-9]*$/.test(params.get('demande') || '')) await showDetail(params.get('demande'));
    } catch(e) { $('status').textContent = e.message; }
})();
