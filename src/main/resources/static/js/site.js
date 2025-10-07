(() => {
    // Helpers
    const $ = (sel, ctx = document) => ctx.querySelector(sel);
    const $$ = (sel, ctx = document) => Array.from(ctx.querySelectorAll(sel));

    const toggle = $('[data-nav-toggle]');
    const menu = $('[data-nav-menu]');
    const mq = window.matchMedia('(min-width: 769px)');

    // ---- Nav / menú ----
    if (toggle && menu) {
        const setMenuOpen = (open) => {
            menu.setAttribute('data-open', String(open));
            toggle.setAttribute('aria-expanded', String(open));
        };

        // Estado inicial según media query
        setMenuOpen(mq.matches);

        // Click del botón
        toggle.addEventListener('click', () => {
            const open = menu.getAttribute('data-open') === 'true';
            setMenuOpen(!open);
        }, {passive: true});

        // Cerrar al hacer click en un enlace (event delegation)
        menu.addEventListener('click', (ev) => {
            const a = ev.target.closest('.nav-link');
            if (!a)
                return;
            if (!mq.matches)
                setMenuOpen(false);
        }, {passive: true});

        // Reaccionar a cambios de viewport sin “resize” ruidoso
        mq.addEventListener('change', (e) => setMenuOpen(e.matches));
    }

    // ---- Scroll suave interno ----
    const smoothLinks = $$('a[href^="#"]');
    if (smoothLinks.length) {
        const prefersReduced = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
        smoothLinks.forEach((link) => {
            link.addEventListener('click', (ev) => {
                const href = link.getAttribute('href');
                if (!href || href.length === 1)
                    return;

                const target = document.getElementById(href.slice(1));
                if (!target)
                    return;

                ev.preventDefault();
                target.scrollIntoView({
                    behavior: prefersReduced ? 'auto' : 'smooth',
                    block: 'start'
                });
            });
        });
    }

    // ---- Resaltado de sección activa ----
    const navLinks = $$('.nav-link').filter((a) => a.hash);
    const sections = $$('section[id]');

    if (navLinks.length && sections.length && 'IntersectionObserver' in window) {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach((entry) => {
                if (!entry.isIntersecting)
                    return;
                const id = entry.target.id;
                navLinks.forEach((a) => a.classList.toggle('active', a.hash === `#${id}`));
            });
        }, {rootMargin: '-45% 0px -45% 0px', threshold: 0});

        sections.forEach((s) => observer.observe(s));
    }
})();
