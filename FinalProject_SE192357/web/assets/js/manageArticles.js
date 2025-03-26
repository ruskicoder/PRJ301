window.onload = function () {
    let message = document.getElementById('message').value;
    let notification = document.getElementById('notification');
    if (message) {
        notification.textContent = message;
        notification.classList.add('show'); // Add 'show' class

        // Hide the notification after 3 seconds (adjust as needed)
        setTimeout(() => {
            notification.classList.remove('show'); // Remove 'show'
        }, 3000);
    }
};

function confirmDelete() {
    return confirm("Are you sure you want to delete this article?");
}