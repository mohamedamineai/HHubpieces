// =====================================================================
// Hubpieces — comportement des visuels 3D décoratifs (<model-viewer>)
//  1. respecte prefers-reduced-motion (auto-rotate, parallaxe);
//  2. composant « holo » : progression du chargement, matérialisation du
//     modèle une fois chargé, état d'erreur, parallaxe au survol,
//     projecteur qui suit le curseur, pause des animations hors écran.
// Aucun de ces visuels n'est un fichier de fabrication (FichierStl).
// =====================================================================
(() => {
    const motionPreference = window.matchMedia('(prefers-reduced-motion: reduce)');
    const finePointer = window.matchMedia('(hover: hover) and (pointer: fine)');

    // ---------- 1. Auto-rotation conforme au mouvement réduit ----------
    const synchronizeAutoRotate = () => {
        document.querySelectorAll('model-viewer[data-motion-auto-rotate]').forEach((viewer) => {
            if (motionPreference.matches) {
                viewer.removeAttribute('auto-rotate');
            } else {
                viewer.setAttribute('auto-rotate', '');
            }
        });
    };

    synchronizeAutoRotate();
    if (typeof motionPreference.addEventListener === 'function') {
        motionPreference.addEventListener('change', synchronizeAutoRotate);
    } else if (typeof motionPreference.addListener === 'function') {
        // Safari versions predating MediaQueryList.addEventListener.
        motionPreference.addListener(synchronizeAutoRotate);
    }

    // ---------- 2. Composant holo ----------
    const setupStage = (stage) => {
        const viewer = stage.querySelector('model-viewer');
        if (!viewer) return;
        const loader = stage.querySelector('.holo-loader');
        const pct = stage.querySelector('[data-holo-pct]');

        const markLoaded = () => {
            stage.classList.add('is-loaded');
            stage.classList.remove('is-error');
            if (loader) loader.setAttribute('aria-hidden', 'true');
        };

        viewer.addEventListener('progress', (event) => {
            const ratio = event.detail && typeof event.detail.totalProgress === 'number'
                ? event.detail.totalProgress : 0;
            const value = Math.round(ratio * 100);
            if (loader) loader.style.setProperty('--holo-p', value);
            if (pct) pct.textContent = value + '%';
        });
        viewer.addEventListener('load', markLoaded);
        viewer.addEventListener('error', () => stage.classList.add('is-error'));

        // Le modèle peut déjà être chargé (cache) avant l'exécution du script.
        if (viewer.loaded) markLoaded();
    };

    const setupParallax = (panel) => {
        const stage = panel.querySelector('.holo-stage');
        const inner = panel.querySelector('.holo-stage-inner');
        let frame = 0;

        panel.addEventListener('pointermove', (event) => {
            if (motionPreference.matches || !finePointer.matches) return;
            if (frame) return;
            frame = requestAnimationFrame(() => {
                frame = 0;
                const rect = panel.getBoundingClientRect();
                const x = event.clientX - rect.left;
                const y = event.clientY - rect.top;
                panel.style.setProperty('--holo-mx', x + 'px');
                panel.style.setProperty('--holo-my', y + 'px');
                if (stage && inner) {
                    const s = stage.getBoundingClientRect();
                    const nx = ((event.clientX - s.left) / s.width) - 0.5;
                    const ny = ((event.clientY - s.top) / s.height) - 0.5;
                    const clamp = (v) => Math.max(-0.5, Math.min(0.5, v));
                    stage.style.setProperty('--holo-ry', (clamp(nx) * 14).toFixed(2) + 'deg');
                    stage.style.setProperty('--holo-rx', (clamp(ny) * -10).toFixed(2) + 'deg');
                }
            });
        });

        panel.addEventListener('pointerleave', () => {
            panel.style.removeProperty('--holo-mx');
            panel.style.removeProperty('--holo-my');
            if (stage) {
                stage.style.setProperty('--holo-rx', '0deg');
                stage.style.setProperty('--holo-ry', '0deg');
            }
        });
    };

    const init = () => {
        document.querySelectorAll('.holo-stage').forEach(setupStage);
        document.querySelectorAll('[data-holo]').forEach(setupParallax);

        // Pause des animations CSS lorsque le visuel sort de l'écran.
        if ('IntersectionObserver' in window) {
            const observer = new IntersectionObserver((entries) => {
                entries.forEach((entry) => {
                    entry.target.classList.toggle('is-paused', !entry.isIntersecting);
                });
            }, { rootMargin: '80px' });
            document.querySelectorAll('[data-holo], .holo-stage--auth').forEach((el) => observer.observe(el));
        }
    };

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})();
