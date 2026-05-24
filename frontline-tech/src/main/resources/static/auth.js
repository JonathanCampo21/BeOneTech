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

// ====================================================================
// ---> LÓGICA CAMALEÃO: MULTI-DEPARTAMENTOS <---
// ====================================================================
document.addEventListener("DOMContentLoaded", () => {
    // 1. Pega as escolhas salvas na memória
    const departamentoAtual = localStorage.getItem('departamentoAtual') || 'LOUVOR';
    const corAtual = localStorage.getItem('corAtual') || '#FF3300';
    const userLogado = JSON.parse(localStorage.getItem('usuario')) || {};

    // 2. INJETA A COR NO CSS DA PÁGINA INTEIRA
    document.documentElement.style.setProperty('--primary', corAtual);

    // 3. ATUALIZA O NOME DO DEPARTAMENTO NO MENU (Se existir o elemento)
    // Supondo que você crie um <span id="nomeDeptBadge"> no seu HTML depois
    const badgeDept = document.getElementById('nomeDeptBadge');
    if (badgeDept) {
        badgeDept.innerText = departamentoAtual;
        badgeDept.style.color = corAtual;
    }

    // 4. ESCONDE AS ABAS DO LOUVOR PARA OS OUTROS DEPARTAMENTOS
    // Como você usa FontAwesome, vamos procurar os links pelo ícone ou pelo texto
    if (departamentoAtual !== 'LOUVOR') {
        const linksMenu = document.querySelectorAll('.sidebar a');
        
        linksMenu.forEach(link => {
            const textoLink = link.innerText.toUpperCase();
            // Se o botão for de Repertório ou Sugestões, a gente "apaga" ele da tela
            if (textoLink.includes('REPERTÓRIO') || textoLink.includes('SUGESTÕES')) {
                link.style.display = 'none';
            }
        });
    }
});

// ====================================================================
// ---> FUNÇÃO ATUALIZADA PARA O BOTÃO DE VOLTAR/SAIR <---
// ====================================================================
function sairSistema() {
    const user = JSON.parse(localStorage.getItem('usuario')) || {};
    
    // Se for Pastor, DEV ou cara de 2 ministérios, o "Sair" joga ele de volta pro Portal/Dashboard
    if (user.cargo === 'PASTOR') {
        window.location.href = 'painel_pastor.html';
    } else if (['DEV', 'ADMIN'].includes(user.cargo) || (user.departamentos && user.departamentos.length > 1)) {
        window.location.href = 'portal.html';
    } else {
        // Se for membro normal de 1 área só, sai de verdade pro login
        localStorage.clear();
        window.location.href = 'login.html';
    }
}
