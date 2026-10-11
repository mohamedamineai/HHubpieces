'use strict';
const field = id => document.getElementById(id);
const stateLabels = {NOUVELLE:'Nouvelle', ANNULEE:'Annulée', CLOTUREE:'Clôturée', MODELISATION_REQUISE:'Modélisation requise', RECHERCHE_MAKER:'Recherche maker', ATTENTE_VALIDATION:'Validation', EN_COURS_IMPRESSION:'En impression'};
function node(tag, text, cls) {
    const el = document.createElement(tag); el.textContent = text;
    if (cls) el.className = cls;
    return el;
}
async function refreshDashboard() {
    field('retry').hidden = true;
    field('metrics').setAttribute('aria-busy','true');
    field('status').textContent = 'Chargement de votre espace…';
    field('recent').replaceChildren();
    for (const id of ['total','newCount','closedCount','cancelledCount']) field(id).textContent = '—';
    try {
        const user = await HubApi.request('/api/auth/me');
        if (user.role !== 'DEMANDEUR') {
            const dashboards = {MAKER:'dashboard-maker.html', EXPERT_CAO:'dashboard-expertcao.html', ADMIN:'dashboard-admin.html'};
            location.replace(dashboards[user.role] || 'login.html'); return;
        }
        field('welcome').replaceChildren(node('span', `Bonjour ${user.prenom || ''}.`), document.createElement('br'), document.createTextNode('Vos projets prennent vie.'));
        const summary = await HubApi.request('/api/demandes/dashboard');
        field('total').textContent = summary.total;
        field('newCount').textContent = summary.parEtat.NOUVELLE;
        field('closedCount').textContent = summary.parEtat.CLOTUREE;
        field('cancelledCount').textContent = summary.parEtat.ANNULEE;
        for (const d of summary.recentes) {
            const link = node('a','','recent-item'); link.href = `mes-demandes.html?demande=${encodeURIComponent(d.id)}`;
            const content = node('div','','recent-content');
            content.append(node('h3',d.titre), node('p',`${d.ville || 'Ville non renseignée'} · ${new Date(d.dateCreation).toLocaleDateString('fr-FR')}`));
            link.append(node('span','◇','recent-icon'),content,node('span',stateLabels[d.etat] || 'État non renseigné','badge'));
            field('recent').append(link);
        }
        if (!summary.recentes.length) {
            const empty = node('div','Aucune demande pour le moment. Votre premier projet commence par une description.','empty-panel');
            const link = node('a','Créer ma première demande →'); link.href = 'mes-demandes.html?nouvelle=1';
            empty.append(link); field('recent').append(empty);
        }
        field('status').textContent = summary.recentes.length ? 'Vos cinq demandes les plus récentes, au maximum.' : '';
    } catch (error) {
        if (error.status === 401) { location.replace('login.html'); return; }
        field('status').textContent = error.message; field('retry').hidden = false;
    } finally { field('metrics').setAttribute('aria-busy','false'); }
}
field('retry').onclick = refreshDashboard;
field('logout').onclick = async () => {
    field('logout').disabled = true;
    try { await HubApi.post('/api/auth/logout'); location.replace('login.html'); }
    catch(error) { field('status').textContent = error.message; field('logout').disabled = false; }
};
refreshDashboard();
