// Submit para criar nova questão
const newQuestionForm = document.getElementById('newQuestionForm');
newQuestionForm.addEventListener('submit', function (event) {
  event.preventDefault();
  const question = document.getElementById('newQuestion').value;
  const matterId = document.getElementById('selectMatterNewQuestion').value;

  fetch('/posts/new', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      question: question,
      matterId: matterId
    })
  })
    .then(response => {
      if (!response.ok) {
        throw new Error(`Erro na requisição: ${response.status}`);
      }
      window.location.href = '/index';
    })
});

// =========================== Criação dos cards dinâmicos ===========================
const CARDS_PER_PAGE = 8; // Quantidade de cards por página
const cardContainer = document.getElementById("cards-container");
const pagination = document.getElementById("pagination");

let cards = []; // Lista de cards recebida do backend
let currentPage = 1;
let url = '';

async function fetchCards(matterId) {
  try {
    if (matterId && matterId != "0") {
      url = `/posts/matter/${matterId}/my`
    } else {
      url = '/posts/myPublishes'
    }
    const response = await fetch(url);
    cards = await response.json();

    if (cards.length > 0) {
      renderPagination();
      renderCards(currentPage);
    } else {
      cardContainer.innerHTML = `<div id="notFoundContainer" class="d-flex align-items-center">
                                  <picture>
                                    <source media="(max-width: 988px)" srcset="/img/without_publishes_mobile.png" width="100%"/>
                                    <img src="img/without_publishes.png" alt="Not found publishes." width="100%"/>
                                  </picture>
                                </div>`;
    }
  } catch (error) {
    console.error("Erro ao buscar dados dos cards:", error);
  }
}

// Renderizar os cards da página atual
function renderCards(page) {
  cardContainer.innerHTML = "";
  const start = (page - 1) * CARDS_PER_PAGE;
  const end = start + CARDS_PER_PAGE;

  const sortedCards = cards.slice().sort((a, b) => b.postId - a.postId)
  const cardsToShow = sortedCards.slice(start, end);

  cardsToShow.forEach(card => {
    const cardHTML = `
      <div class="col-12 col-sm-6 col-md-4 col-lg-3">
        <div class="card shadow-sm">
          <div class="card-body">
            
            <div class="d-flex justify-content-between mb-3">
              <span>${card.username}</span>
              <button class="deletePostBtn" data-id="${card.postId}" data-bs-toggle="modal" data-bs-target="#deletePostModal" style="color: red; border: none;"><i class="fas fa-trash"></i></button>
            </div>

            <div class="mb-3 card-text">
              ${card.postQuestion}
            </div>

            <div class="d-flex justify-content-between align-items-center">
              <span class="text-primary">${card.commentsCount} comentários</span>
              <button class="btn btn-primary btnViewPost" data-id="${card.postId}" data-bs-toggle="modal" data-bs-target="#viewQuestionModal">Visualizar</button>
            </div>
          </div>
        </div>
      </div>
    `;
    cardContainer.innerHTML += cardHTML;
  });
}

// Renderizar os botões de paginação
function renderPagination() {
  pagination.innerHTML = ""; // Limpar paginação
  const totalPages = Math.ceil(cards.length / CARDS_PER_PAGE);

  // Botão "Anterior"
  const prevButton = document.createElement("li");
  prevButton.className = `page-item ${currentPage === 1 ? "disabled" : ""}`;
  prevButton.innerHTML = `<a class="page-link" href="#" id="prev-page">Anterior</a>`;
  prevButton.addEventListener("click", (e) => {
    e.preventDefault();
    if (currentPage > 1) {
      currentPage--;
      renderCards(currentPage);
      renderPagination();
    }
  });
  pagination.appendChild(prevButton);

  // Botões de páginas
  for (let i = 1; i <= totalPages; i++) {
    const pageButton = document.createElement("li");
    pageButton.className = `page-item ${i === currentPage ? "active" : ""}`;
    pageButton.innerHTML = `<a class="page-link" href="#" data-page="${i}">${i}</a>`;
    pageButton.addEventListener("click", (e) => {
      e.preventDefault();
      currentPage = i;
      renderCards(currentPage);
      renderPagination();
    });
    pagination.appendChild(pageButton);
  }

  // Botão "Próximo"
  const nextButton = document.createElement("li");
  nextButton.className = `page-item ${currentPage === totalPages ? "disabled" : ""}`;
  nextButton.innerHTML = `<a class="page-link" href="#" id="next-page">Próximo</a>`;
  nextButton.addEventListener("click", (e) => {
    e.preventDefault();
    if (currentPage < totalPages) {
      currentPage++;
      renderCards(currentPage);
      renderPagination();
    }
  });
  pagination.appendChild(nextButton);
}

// Filtrar posts por matéria
const filterMatter = document.getElementById('filterMatterModal');

filterMatter.addEventListener('submit', (e) => {
  e.preventDefault();
  const matterId = document.getElementById('selecMatterFilter').value;

  currentPage = 1;
  fetchCards(matterId);

  const filterModal = new bootstrap.Modal(document.getElementById("filterMatterModal"));
  filterModal.hide();
})

// =========================== Modal de visualização dos posts ===========================
document.addEventListener('click', async function (event) {
  
  if (event.target.classList.contains('btnViewPost')) {
    const postId = event.target.getAttribute('data-id');

    try {
      const responsePost = await fetch(`/posts/${postId}`);
      const postData = await responsePost.json();
      const responseComments = await fetch(`/comments/${postId}`);
      let commentsData = [];

      if (responseComments.status == 200) {
        commentsData = await responseComments.json();
      }

      if (responsePost.ok) {

        const modalBody = document.getElementById('modalViewPostBody');
        modalBody.innerHTML = "";
        // Preenche o modal com os dados do post 
        modalBody.innerHTML = `
          <div class="mb-2" style="text-align:center">
            <h4>${postData.username}</h4>
          </div>
          <div class="mb-2 p-3" id="questionModal">
            <p>${postData.postQuestion}</p>
          </div>
          <p><strong>Comentários:</strong> ${postData.commentsCount}</p>
          <div class="comments">
            <ul>
              ${commentsData.length > 0
            ? commentsData.map(comment => `
                  <div class="comment-box" id="commentBox" data-value="${comment.commentId}">
                    <div class="comment-header">
                      <p id="commentUser">${comment.username}</p>
                    </div>
                    <div class="comment-body">
                      <p id="commentText">${comment.textComment}</p>
                    </div>
                    <div class="comment-rating">
                      <span>Avaliação: </span>
                      <div class="stars" data-selected-rating="${comment.voteValue || 0}">
                        ${Array.from({ length: 5 }, (_, i) => `
                          <span 
                            class="star ${i < (comment.voteValue || 0) ? 'selected' : ''}" 
                            data-value="${i + 1}">
                              ☆
                          </span>`).join('')}
                      </div>
                    </div>
                  </div>`).join('')
            : '<li>Nenhum comentário disponível.</li>'
          }
            </ul>
          </div>
        `;
      } else {
        console.error('Erro ao buscar dados do post');
      }
    } catch (error) {
      console.error('Erro ao fazer o fetch:', error);
    }
  }

  if (event.target.closest('.deletePostBtn')) {
    const postId = event.target.closest('.deletePostBtn').getAttribute('data-id');
    const deletePostBtnModal = document.getElementById('deletePostBtnModal');

    deletePostBtnModal.setAttribute('data-id', postId);
  }

});

document.addEventListener('DOMContentLoaded', () => {
  fetchCards();

  fetch('/matters/all')
    .then(response => response.json())
    .then(data => {

      const selectMatterNewQuestion = document.getElementById('selectMatterNewQuestion');
      data.forEach(type => {
        const option = document.createElement('option');
        option.value = type.matterId;
        option.textContent = type.matterName;
        selectMatterNewQuestion.appendChild(option);
      });

      const selecMatterFilter = document.getElementById('selecMatterFilter');
      data.forEach(type => {
        const option = document.createElement('option');
        option.value = type.matterId;
        option.textContent = type.matterName;
        selecMatterFilter.appendChild(option);
      });

    })
    .catch(error => console.error('Erro ao carregar tipos de questões:', error));
})

document.addEventListener('click', function (event) {
  if (event.target.classList.contains('star')) {
    const star = event.target;
    const starsContainer = star.closest('.stars');
    const commentBox = star.closest('.comment-box');

    const clickedValue = parseInt(star.dataset.value, 10);

    const allStars = starsContainer.querySelectorAll('.star');

    allStars.forEach(star => {
      const starValue = parseInt(star.dataset.value, 10);
      if (starValue <= clickedValue) {
        star.classList.add('selected');
      } else {
        star.classList.remove('selected');
      }
    });

    const voteValue = clickedValue;
    const commentId = commentBox.dataset.value;

    fetch('/votes/upsert', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        voteValue: voteValue,
        commentId: commentId
      })
    })
  }
});

document.getElementById('deletePostBtnModal').addEventListener('click', () => {
  const postId = document.getElementById('deletePostBtnModal').getAttribute('data-id');

  fetch(`/posts/${postId}`, {
    method: 'DELETE'
  })
   .then(response => {
      if (response.status === 204) {
        window.location.reload();
      } else {
        console.error('Erro ao deletar o post');
      }
    })
   .catch(error => console.error('Erro ao fazer o fetch:', error));
});