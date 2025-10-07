(function () {
    const toggle = document.querySelector('[data-nav-toggle]');
    const menu = document.querySelector('[data-nav-menu]');

    if (toggle && menu) {
        menu.setAttribute('data-open', window.innerWidth >= 769 ? 'true' : 'false');
        const closeMenu = () => {
            toggle.setAttribute('aria-expanded', 'false');
            menu.setAttribute('data-open', 'false');
        };

        toggle.addEventListener('click', () => {
            const expanded = toggle.getAttribute('aria-expanded') === 'true';
            const nextState = !expanded;
            toggle.setAttribute('aria-expanded', String(nextState));
            menu.setAttribute('data-open', String(nextState));
        });

        menu.querySelectorAll('.nav-link').forEach((link) => {
            link.addEventListener('click', () => {
                if (window.innerWidth < 769) {
                    closeMenu();
                }
            });
        });

        window.addEventListener('resize', () => {
            if (window.innerWidth >= 769) {
                menu.setAttribute('data-open', 'true');
                toggle.setAttribute('aria-expanded', 'false');
            } else {
                menu.setAttribute('data-open', 'false');
            }
        });
    }

    const smoothLinks = document.querySelectorAll('a[href^="#"]');
    smoothLinks.forEach((link) => {
        link.addEventListener('click', (ev) => {
            const targetId = link.getAttribute('href');
            if (!targetId || targetId.length === 1) {
                return;
            }
            const target = document.querySelector(targetId);
            if (!target) {
                return;
            }
            ev.preventDefault();
            target.scrollIntoView({behavior: 'smooth', block: 'start'});
        });
    });

    const navLinks = Array.from(document.querySelectorAll('[data-nav-menu] .nav-link'))
            .filter((link) => link.hash);
    const sections = Array.from(document.querySelectorAll('section[id]'));

    if (navLinks.length && sections.length) {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach((entry) => {
                const link = navLinks.find((nav) => nav.hash === `#${entry.target.id}`);
                if (!link) {
                    return;
                }
                if (entry.isIntersecting) {
                    navLinks.forEach((nav) => nav.classList.remove('active'));
                    link.classList.add('active');
                }
            });
        }, {rootMargin: '-45% 0px -45% 0px'});

        sections.forEach((section) => observer.observe(section));
    }
})();