const newQuestionForm = document.getElementById('newQuestionForm');

newQuestionForm.addEventListener('submit', function (event) {
  event.preventDefault();
  const question = document.getElementById('newQuestion').value;
  const questionMatter = document.getElementById('questionMatterModal').value;
  fetch('/posts/new', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ question, questionMatter })
  })
    .then(response => {
      if (!response.ok) {
        throw new Error(`Erro na requisição: ${response.status}`);
      }
      window.location.href = '/index';
    })
});

const postsContainer = document.getElementById('postsContainer');

function createPost({ username, postQuestion, commentsCount, postId }) {
  const postDiv = document.createElement('div');
  postDiv.className = 'col-md-4 mb-3';

  postDiv.innerHTML = `
    <form class = "form-card">
      <div class="post-card">
        <div class="d-flex align-items-center">
          <img
            src="/img/icon-home.png"
            alt="User"
            class="rounded-circle me-2"
            width="40"
          />
          <span>${username}</span>
        </div>
        <p>${postQuestion}</p>
        <div class="d-flex justify-content-between">
          <small class="text-primary">${commentsCount} comentários</small>
          <input hidden value=${postId}>
          <button type="submit" class="viewButton">Visualizar</button>
        </div>
      </div>
    </form>
  `;

  return postDiv;
}

document.addEventListener('DOMContentLoaded', () => {
  fetch('/posts/all')
    .then(response => response.json())
    .then(posts => {
      posts.forEach(post => {
        const postElement = createPost(post);
        postsContainer.appendChild(postElement);
      });
    })
    .catch(error => console.error('Erro ao buscar posts:', error));

  fetch('/matters/all')
    .then(response => response.json())
    .then(data => {
      const selectFilterPrincipal = document.getElementById('questionMatterPrincipal');
      data.forEach(type => {
        const li = document.createElement('li');
        const button = document.createElement('button');
        button.classList.add('dropdown-item');
        button.value = type.matterId;
        button.textContent = type.matterName;
        li.appendChild(button);
        selectFilterPrincipal.appendChild(li);
      });

      const selectFilterModal = document.getElementById('questionMatterModal');
      data.forEach(type => {
        const option = document.createElement('option');
        option.value = type.matterId;
        option.textContent = type.matterName;
        selectFilterModal.appendChild(option);
      });

    })
    .catch(error => console.error('Erro ao carregar tipos de questões:', error));

})
