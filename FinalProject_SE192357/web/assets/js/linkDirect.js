document.addEventListener('DOMContentLoaded', function() {
    const postLinks = document.querySelectorAll('a.post-link');

    postLinks.forEach(link => {
        link.addEventListener('click', function(event) {
            event.preventDefault(); // Stop the link from navigating normally

            const form = document.createElement('form');
            form.method = 'post';
            form.action = 'main'; // Point to your main servlet

            // Iterate over data attributes and create hidden inputs
            for (const key in this.dataset) {
                if (this.dataset.hasOwnProperty(key)) {
                    const hiddenInput = document.createElement('input');
                    hiddenInput.type = 'hidden';
                    // Convert camelCase data attribute names (e.g., data-filterDate)
                    // to the expected parameter name (e.g., filterDate)
                    const inputName = key; // Simple mapping for now
                    hiddenInput.name = inputName;
                    hiddenInput.value = this.dataset[key];
                    form.appendChild(hiddenInput);
                }
            }

            document.body.appendChild(form); // Add form to the page
            form.submit(); // Submit the form
            document.body.removeChild(form); // Clean up the form
        });
    });
});
