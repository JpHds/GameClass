const newQuestionForm = document.getElementById('newQuestionForm');

// Submit para criar nova questão
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

async function fetchCards(matterFilter) {
  try {
    if (matterFilter && matterFilter != "0") {
      url = `/posts/matter/${matterFilter}`
    } else {
      url = '/posts/all'
    }
    const response = await fetch(url);
    cards = await response.json();
    renderPagination();
    renderCards(currentPage);
  } catch (error) {
    console.error("Erro ao buscar dados dos cards:", error);
  }
}

// Renderizar os cards da página atual
function renderCards(page) {
  cardContainer.innerHTML = ""; // Limpar cards
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
              <span class="badge ${card.userType === 'TEACHER' ? 'bg-danger' : 'bg-primary'}">${card.userType === 'TEACHER' ? 'Professor' : 'Estudante'}</span>
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

// =========================== Criação dos cards dinâmicos ===========================

// Filtrar posts por matéria
const filterMatter = document.getElementById('filterMatterModal');

filterMatter.addEventListener('submit', (e) => {
  e.preventDefault();
  const matterId = document.getElementById('selecMatterFilter').value;
  fetchCards(matterId);

  const filterModal = new bootstrap.Modal(document.getElementById("filterMatterModal"));
  filterModal.hide();
})

// =========================== Modal de visualização dos posts ===========================
document.addEventListener('click', async function (event) {
  if (event.target.classList.contains('btnViewPost')) {
    const postId = event.target.getAttribute('data-id');

    try {
      // Faz o fetch para obter os dados do post com base no id
      const responsePost = await fetch(`/posts/${postId}`); // Altere para o endpoint correto
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
          <div class = "mb-2" style = "text-align:center">
            <h4>${postData.username}</h4>
          </div>
          <div class = "mb-2" id="questionModal">
            <p style="padding: 5px">${postData.postQuestion}</p>
          </div>
          <p><strong>Comentários:</strong> ${postData.commentsCount}</p>
          <div class="comments">
            <ul>
            ${commentsData.length > 0
            ? commentsData.map(comment =>
              `<div class="comment-box" id="commentBox" data-value="${comment.commentId}">
                <div class="comment-header">
                  <p id="commentUser">${comment.username}</p>
                </div>
                <div class="comment-body">
                  <p id="commentText">${comment.textComment}</p>
                </div>
                <div class="comment-rating">
                  <span>Avaliação: </span>
                  <div class="stars" data-selected-rating="0">
                    <span class="star" data-value="1">☆</span>
                    <span class="star" data-value="2">☆</span>
                    <span class="star" data-value="3">☆</span>
                    <span class="star" data-value="4">☆</span>
                    <span class="star" data-value="5">☆</span>
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
});
// =========================== Modal de visualização dos posts ===========================


// =========================== Execução ao inicializar a página ===========================
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
    const voteValue = event.target.getAttribute('data-value')
    const postId = document.getElementById('commentBox').getAttribute('data-value')

    fetch('/vote/update', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        voteValue: voteValue, 
        postId: postId
      })
    }
    )
  }
});

