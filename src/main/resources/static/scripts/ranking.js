document.addEventListener('DOMContentLoaded', () => {
    const rankBody = document.getElementById('rankBody');
    const ul = document.createElement('ul');
    rankBody.append(ul);

    fetch('/users/rank', {
        method: 'GET'
    })
    .then(response => response.json())
    .then(data => {
        const users = data || [];
        const numItems = users.length;

        users.forEach(user => {
            const li = document.createElement('li');
            li.textContent = user.username;
            ul.appendChild(li);
        });


        for (let i = numItems; i < 10; i++) {
            const li = document.createElement('li');
            li.textContent = `Ainda não temos um ${i}° lugar`;
            ul.appendChild(li);
        }
    })
    .catch(error => {
        console.error('Erro ao buscar dados:', error);
    });
});
