class SPARouter {
    constructor() {
        this.routes = {
            '/login': { template: 'auth/login', fragment: 'login-form' },
            '/register': { template: 'auth/register', fragment: 'register-form' },
            '/dashboard': { template: 'dashboard/index', fragment: 'dashboard-content' },
            '/users': { template: 'dashboard/users', fragment: 'users-content' },
            '/addresses': { template: 'dashboard/addresses', fragment: 'addresses-content' }
        };
        this.init();
    }

    init() {
        window.addEventListener('popstate', () => this.route());
        document.addEventListener('DOMContentLoaded', () => this.route());
        this.setupNavigation();
    }

    async route() {
        const path = window.location.pathname;
        const route = this.routes[path] || this.routes['/dashboard'];
        
        // Mostrar loader
        document.getElementById('app-content').innerHTML = '<div class="text-center mt-5"><div class="spinner-border"></div></div>';
        
        try {
            const response = await fetch(`/fragments/${route.template}#${route.fragment}`);
            const html = await response.text();
            document.getElementById('app-content').innerHTML = html;
            
            // Carregar scripts específicos da página
            this.loadPageScript(path);
        } catch (error) {
            console.error('Error loading page:', error);
            this.showToast('Erro ao carregar a página', 'error');
        }
    }

    setupNavigation() {
        document.body.addEventListener('click', e => {
            if (e.target.matches('[data-link]')) {
                e.preventDefault();
                history.pushState(null, null, e.target.href);
                this.route();
            }
        });
    }

    loadPageScript(path) {
        if (path === '/login' || path === '/register') {
            const script = document.createElement('script');
            script.src = '/js/auth.js';
            document.body.appendChild(script);
        }
    }

    showToast(message, type = 'success') {
        Toastify({
            text: message,
            duration: 3000,
            close: true,
            gravity: "top",
            position: "right",
            backgroundColor: type === 'success' ? "#28a745" : "#dc3545"
        }).showToast();
    }
}

new SPARouter();

function checkUserRole() {
    const token = localStorage.getItem('token');
    if (!token) return null;
    
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.role;
}

function setupUI() {
    const role = checkUserRole();
    const nav = document.querySelector('nav');
    
    if (role === 'ADMIN') {
        nav.innerHTML += `
            <li class="nav-item">
                <a class="nav-link" data-link href="/users">Usuários</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" data-link href="/addresses">Endereços</a>
            </li>
        `;
    } else if (role === 'USUARIO') {
        nav.innerHTML += `
            <li class="nav-item">
                <a class="nav-link" data-link href="/profile">Meu Perfil</a>
            </li>
        `;
    }
}

// Chamar no carregamento da página
document.addEventListener('DOMContentLoaded', setupUI);