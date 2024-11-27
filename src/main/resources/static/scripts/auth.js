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
    const cpf = document.getElementById('registerCpf').value;
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
                window.location.href = '/';
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });

})
