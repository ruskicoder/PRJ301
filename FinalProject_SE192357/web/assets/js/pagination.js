document.addEventListener('DOMContentLoaded', function () {
    const articlesPerPage = 6; // Configurable: Number of articles per page
    const articleContainer = document.getElementById('articles-container');
    const paginationControlsTop = document.getElementById('pagination-controls-top');
    const paginationControlsBottom = document.getElementById('pagination-controls-bottom');

    if (!articleContainer || !paginationControlsTop || !paginationControlsBottom) {
        // If essential elements aren't found, don't run pagination
        console.error("Pagination elements not found.");
        return;
    }

    const articleCards = Array.from(articleContainer.getElementsByClassName('article-card'));
    const totalArticles = articleCards.length;
    const totalPages = Math.ceil(totalArticles / articlesPerPage);
    let currentPage = 1;

    function createPaginationControls(container) {
        container.innerHTML = `
            <button class="pagination-btn prev-btn" disabled>« Previous</button>
            <span class="page-info">Page <span class="current-page">1</span> of <span class="total-pages">${totalPages}</span></span>
            <button class="pagination-btn next-btn">Next »</button>
        `;

        const prevBtn = container.querySelector('.prev-btn');
        const nextBtn = container.querySelector('.next-btn');

        prevBtn.addEventListener('click', () => {
            if (currentPage > 1) {
                displayPage(currentPage - 1);
            }
        });

        nextBtn.addEventListener('click', () => {
            if (currentPage < totalPages) {
                displayPage(currentPage + 1);
            }
        });
        // Disable next button initially if only one page
        if (totalPages <= 1) {
            nextBtn.disabled = true;
        }
    }


    function updatePaginationControls() {
        const allPrevBtns = document.querySelectorAll('.prev-btn');
        const allNextBtns = document.querySelectorAll('.next-btn');
        const allCurrentPageSpans = document.querySelectorAll('.current-page');

        allCurrentPageSpans.forEach(span => span.textContent = currentPage);

        allPrevBtns.forEach(btn => btn.disabled = (currentPage === 1));
        allNextBtns.forEach(btn => btn.disabled = (currentPage === totalPages));
    }

    function displayPage(pageNumber) {
        currentPage = pageNumber;
        const startIndex = (currentPage - 1) * articlesPerPage;
        const endIndex = startIndex + articlesPerPage;

        // Hide all cards first
        articleCards.forEach(card => card.style.display = 'none');

        // Show only the cards for the current page
        const cardsToShow = articleCards.slice(startIndex, endIndex);
        cardsToShow.forEach(card => card.style.display = 'flex'); // Use 'flex' as per article-card style

        updatePaginationControls();

        // Optional: Scroll to the top of the article container when changing pages
        // articleContainer.scrollIntoView({ behavior: 'smooth' });
    }

    // --- Initialization ---
    if (totalArticles > 0) {
        createPaginationControls(paginationControlsTop);
        createPaginationControls(paginationControlsBottom);
        displayPage(1); // Display the first page initially
    } else {
        // If no articles, clear pagination controls
        paginationControlsTop.innerHTML = '';
        paginationControlsBottom.innerHTML = '';
    }
});
