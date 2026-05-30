// =========================================
// 1. VERIFICAÇÃO DE SEGURANÇA E TOKEN
// =========================================
const usuarioLogado = JSON.parse(localStorage.getItem('usuario'));
const tokenStr = localStorage.getItem('token');

if ((!usuarioLogado || !tokenStr) && !window.location.pathname.endsWith('login.html') && !window.location.pathname.endsWith('index.html')) {
    window.location.href = 'login.html';
}

// =========================================
// 2. INTERCEPTADOR DE FETCH (A MÁGICA)
// =========================================
const originalFetch = window.fetch;
window.fetch = async function() {
    let [resource, config] = arguments;

    if (typeof resource === 'string' && resource.startsWith('/api') && !resource.includes('/api/auth/login')) {
        config = config || {};
        config.headers = config.headers || {};

        const crachaAtual = localStorage.getItem('token');
        if (crachaAtual) {
            config.headers['Authorization'] = 'Bearer ' + crachaAtual;
        }
    }

    const response = await originalFetch(resource, config);

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
    localStorage.removeItem('token'); 
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
// ---> LÓGICA CAMALEÃO: MULTI-DEPARTAMENTOS E BLINDAGEM DE CORES <---
// ====================================================================
document.addEventListener("DOMContentLoaded", () => {
    const departamentoAtual = localStorage.getItem('departamentoAtual') || 'LOUVOR';
    
    // 👉 TRAVA DE CORES: Força a cor correta brilhante e ignora o cache antigo do navegador
    const DEPARTAMENTOS_CORES = {
        "LOUVOR": "#FF3300",
        "COMUNICAÇÃO": "#a855f7",
        "SALT": "#3b82f6",
        "27+": "#10b981",
        "JOIN": "#6366f1",
        "KIDS": "#facc15"
    };
    
    // Pega a cor oficial do dicionário acima
    const corCorreta = DEPARTAMENTOS_CORES[departamentoAtual] || '#FF3300';
    
    // Atualiza a memória do navegador do usuário na marra pra ele nunca mais usar cor velha
    localStorage.setItem('corAtual', corCorreta); 
    
    const userLogado = JSON.parse(localStorage.getItem('usuario')) || {};

    // Injeta a cor correta na página
    document.documentElement.style.setProperty('--primary', corCorreta);

    const badgeDept = document.getElementById('nomeDeptBadge');
    if (badgeDept) {
        badgeDept.innerText = departamentoAtual;
        badgeDept.style.color = corCorreta;
    }

    if (departamentoAtual !== 'LOUVOR') {
        const linksMenu = document.querySelectorAll('.sidebar a');
        
        linksMenu.forEach(link => {
            const textoLink = link.innerText.toUpperCase();
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
    
    if (user.cargo === 'PASTOR') {
        window.location.href = 'painel_pastor.html';
    } else if (['DEV', 'ADMIN', 'CONVIDADO'].includes(user.cargo) || (user.departamentos && user.departamentos.length > 1)) {
        window.location.href = 'portal.html';
    } else {
        localStorage.clear();
        window.location.href = 'login.html';
    }
}

// ====================================================================
// ---> TRAVA DE SEGURANÇA E LIBERAÇÃO PRO CONVIDADO (SHOWCASE) <---
// ====================================================================
document.addEventListener("DOMContentLoaded", () => {
    const usrConvidado = JSON.parse(localStorage.getItem('usuario'));
    const cargoConv = usrConvidado ? String(usrConvidado.cargo).toUpperCase().trim() : '';

    if (cargoConv === 'CONVIDADO') {
        const style = document.createElement('style');
        style.innerHTML = `
            button[onclick*="salvar"], button[onclick*="Salvar"], 
            button[onclick*="excluir"], button[onclick*="Excluir"], 
            button[onclick*="deletar"], button[onclick*="Deletar"],
            button[onclick*="enviarZap"], .btn-icon[onclick*="prepararEdicao"],
            .btn-action.edit, .btn-action.delete, .btn-add-event,
            .fa-trash, .fa-trash-alt, .fa-edit, .fa-whatsapp,
            .btn-salvar, #btnNovaMusica, #btnNovoMembro {
                display: none !important;
            }
            
            a[href*="whatsapp"], a[href*="wa.me"] {
                pointer-events: none !important;
                opacity: 0.3 !important;
                filter: grayscale(100%);
            }

            /* 🔑 LIBERA O MENU LATERAL PARA O CONVIDADO VER TUDO! */
            #linkAdmin, #linkPastor {
                display: flex !important;
            }
        `;
        document.head.appendChild(style);

        window.enviarZap = function() { 
            alert("Modo Demonstração: O envio de mensagens está desabilitado."); 
            return false; 
        };
        
        const originalOpen = window.open;
        window.open = function(url, target, features) {
            if (url && (url.includes('whatsapp.com') || url.includes('wa.me'))) {
                alert('Modo Demonstração: O envio de mensagens está desabilitado.');
                return null;
            }
            return originalOpen(url, target, features);
        };
    }
});
