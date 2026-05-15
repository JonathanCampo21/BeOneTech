// =========================================
// 1. VERIFICAÇÃO DE SEGURANÇA E TOKEN
// =========================================
const usuarioLogado = JSON.parse(localStorage.getItem('usuario'));
const tokenStr = localStorage.getItem('token');

// Se não tem usuário ou não tem token, e a tela não for livre (login/index), rua!
if ((!usuarioLogado || !tokenStr) && !window.location.pathname.endsWith('login.html') && !window.location.pathname.endsWith('index.html')) {
    window.location.href = 'login.html';
}

// =========================================
// 2. INTERCEPTADOR DE FETCH (A MÁGICA)
// =========================================
// Isso anexa o Token JWT em TODAS as requisições da sua API automaticamente!
const originalFetch = window.fetch;
window.fetch = async function() {
    let [resource, config] = arguments;

    // Se a requisição for para a nossa API e NÃO for a rota de login
    if (typeof resource === 'string' && resource.startsWith('/api') && !resource.includes('/api/auth/login')) {
        config = config || {};
        config.headers = config.headers || {};

        // Grampeia o crachá no cabeçalho (Header) da requisição
        const crachaAtual = localStorage.getItem('token');
        if (crachaAtual) {
            config.headers['Authorization'] = 'Bearer ' + crachaAtual;
        }
    }

    const response = await originalFetch(resource, config);

    // Se o crachá vencer ou for inválido, o Java recusa (401/403) e a gente desloga.
    if (response.status === 401 || response.status === 403) {
        sair();
    }
    return response;
};

// =========================================
// 3. FUNÇÃO DE DESLOGAR (BOTÃO SAIR)
// =========================================
function sair() {
    localStorage.removeItem('usuario');
    localStorage.removeItem('token'); // Queima o crachá
    window.location.href = 'login.html';
}

// =========================================
// 4. TRANSIÇÃO SUAVE ENTRE AS TELAS (FADE)
// =========================================
document.addEventListener("DOMContentLoaded", () => {
    const links = document.querySelectorAll('.sidebar a');
    const mainContent = document.querySelector('.main-content');

    links.forEach(link => {
        link.addEventListener('click', function(e) {
            if (this.getAttribute('href') === '#' || this.classList.contains('btn-sair') || this.classList.contains('active')) {
                return;
            }

            e.preventDefault();
            const destino = this.href;

            if (mainContent) {
                mainContent.classList.add('saindo');
            }

            setTimeout(() => {
                window.location.href = destino;
            }, 150);
        });
    });
});