document.addEventListener('DOMContentLoaded', function () {
    let dropArea = document.getElementById('drop-area');
    let preview = document.getElementById('preview');
    let userData = document.getElementById('userImageData');
    let fileInput = document.getElementById('userImage');

    if (dropArea) { //Check if the element exists before adding listeners
        ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
            dropArea.addEventListener(eventName, preventDefaults, false);
        });

        ['dragenter', 'dragover'].forEach(eventName => {
            dropArea.addEventListener(eventName, highlight, false);
        });

        ['dragleave', 'drop'].forEach(eventName => {
            dropArea.addEventListener(eventName, unhighlight, false);
        });

        dropArea.addEventListener('drop', handleDrop, false);

        // Add click listener to trigger file input
        dropArea.addEventListener('click', () => {
            fileInput.click();
        });
    }

    if (fileInput) { //Check if the element exists
        fileInput.addEventListener('change', function () {
            handleFiles(this.files);
        });
    }

    // Prevent default drag behaviors for the body too (optional but good practice)
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        document.body.addEventListener(eventName, preventDefaults, false);
    });


    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }

    function highlight(e) {
        dropArea.classList.add('highlight');
    }

    function unhighlight(e) {
        dropArea.classList.remove('highlight');
    }

    function handleDrop(e) {
        let dt = e.dataTransfer;
        let files = dt.files;
        handleFiles(files);
    }

    function handleFiles(files) {
        if (files.length > 1) {
            alert('Please upload only one image.');
            return;
        }
        if (files.length > 0) { // Check if files exist before processing
            previewFile(files[0]);
            uploadFile(files[0]);
        }
    }

    function previewFile(file) {
        let reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = function () {
            if (preview) { // Check if preview element exists
                preview.src = reader.result;
                preview.style.display = "block";
            }
        };
    }

    function uploadFile(file) {
        let reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = function () {
            if (userData) { // Check if userData element exists
                userData.value = reader.result;
            }
        };
    }
});