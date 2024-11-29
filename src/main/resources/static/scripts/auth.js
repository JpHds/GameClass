const loginForm = document.getElementById('loginForm');

loginForm.addEventListener('submit', function (event) {
    event.preventDefault();
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;

    fetch('/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password })
    })
        .then(response => {
            if (!response.ok) {
                window.location.href = '/auth?loginError'
            } else {
                window.location.href = '/index';
            }
        })
});

const registerForm = document.getElementById('registerForm');

registerForm.addEventListener('submit', function (event) {
    event.preventDefault();
    const username = document.getElementById('registerUsername').value;
    const password = document.getElementById('registerPassword').value;
    const email = document.getElementById('registerEmail').value;
    const cpf = cpfFormat(document.getElementById('registerCpf').value);
    const userType = document.getElementById('registerUserType').value;

    fetch('/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password, email, cpf, userType })
    })
        .then(response => {
            if (!response.ok) {
                window.location.href = '/auth?registerError'
            } else {
                window.location.href = '/auth?registerSuccess';
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });

})
window.onload = function () {
    var mensagem = document.getElementById("loginSuccess");

    mensagem.style.display = "block";

    setTimeout(function () {
        mensagem.style.animation = "fadeOut 2s forwards";
    }, 2000);
};

document.getElementById('registerCpf').addEventListener('input', function (event) {
    let input = event.target;
    let valor = input.value;

    valor = valor.replace(/\D/g, "");

    if (valor.length <= 11) {
        if (valor.length <= 3) {
            input.value = valor;
        } else if (valor.length <= 6) {
            input.value = valor.replace(/(\d{3})(\d{1,3})/, '$1.$2');
        } else if (valor.length <= 9) {
            input.value = valor.replace(/(\d{3})(\d{3})(\d{1,3})/, '$1.$2.$3');
        } else {
            input.value = valor.replace(/(\d{3})(\d{3})(\d{3})(\d{1,2})/, '$1.$2.$3-$4');
        }
    }
});

function cpfFormat(cpf) {
    cpf = cpf.replace(/\D/g, "");
    return cpf;
}


