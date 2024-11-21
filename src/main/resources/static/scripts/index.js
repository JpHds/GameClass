const newQuestionForm = document.getElementById('newQuestionForm');

newQuestionForm.addEventListener('submit', function (event) {
    event.preventDefault();
    const question = document.getElementById('newQuestion').value;

    fetch('/post/new', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({question})
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erro na requisição: ${response.status}`);
            }
            window.location.href = '/index'
        })
});
