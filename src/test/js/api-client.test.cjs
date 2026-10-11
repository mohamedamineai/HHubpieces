const {test} = require('node:test');
const assert = require('node:assert/strict');
const vm = require('node:vm');
const fs = require('node:fs');
const code = fs.readFileSync('src/main/resources/static/js/api-client.js', 'utf8');
function client(fetch) {
    const context = {window:{}, fetch, Headers}; vm.runInNewContext(code, context);
    return context.window.HubApi;
}
const response = (data, status = 200) => ({ok:status < 400, status, json:async () => data});
test('GET uses the session without a CSRF round-trip', async () => {
    const calls = []; const api = client(async (...args) => {calls.push(args); return response({total:0});});
    assert.equal((await api.request('/api/demandes/dashboard')).total, 0);
    assert.equal(calls.length, 1); assert.equal(calls[0][1].credentials, 'same-origin');
});
test('POST obtains a fresh token then sends JSON with the session', async () => {
    const calls = [];
    const api = client(async (path, opts) => {calls.push([path, opts]); return response(path === '/api/csrf' ? {headerName:'X-XSRF-TOKEN',token:'test-token'} : {success:true});});
    await api.post('/api/auth/login',{email:'user@example.test'});
    assert.equal(calls[0][0], '/api/csrf'); assert.equal(calls[1][0], '/api/auth/login');
    assert.equal(calls[1][1].headers.get('X-XSRF-TOKEN'), 'test-token');
    assert.equal(calls[1][1].method, 'POST'); assert.equal(JSON.parse(calls[1][1].body).email, 'user@example.test');
});
test('validation errors preserve status and field messages', async () => {
    const api = client(async () => response({fields:{titre:'obligatoire'}},400));
    await assert.rejects(api.request('/api/demandes'), e => e.status === 400 && e.message.includes('titre : obligatoire'));
});
test('CSRF acquisition failure prevents the mutation', async () => {
    let calls = 0; const api = client(async () => {calls++; return response({},403);});
    await assert.rejects(api.post('/api/auth/logout')); assert.equal(calls,1);
});
test('empty response body is accepted on logout', async () => {
    const api = client(async path => path === '/api/csrf' ? response({headerName:'X-XSRF-TOKEN',token:'x'}) : {ok:true,status:200,json:async () => {throw new Error('empty');}});
    assert.equal(Object.keys(await api.post('/api/auth/logout')).length,0);
});
test('external API destinations are refused before fetch', async () => {
    let called = false; const api = client(async () => {called = true;});
    await assert.rejects(api.request('https://example.com/api/')); assert.equal(called,false);
});
