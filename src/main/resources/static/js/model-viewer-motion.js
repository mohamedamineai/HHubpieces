// Respect the OS/browser reduced-motion setting for Hubpieces decorative 3D assets.
(() => {
    const motionPreference = window.matchMedia('(prefers-reduced-motion: reduce)');
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
})();
