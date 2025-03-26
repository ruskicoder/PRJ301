
let dropArea = document.getElementById('drop-area');
let preview = document.getElementById('preview');
let thumbnailData = document.getElementById('thumbnail-data');
let fileInput = document.getElementById('thumbnail'); // Corrected ID

const MAX_FILE_SIZE_MB = 2; // Maximum file size in Megabytes
const MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024; // Convert MB to Bytes

// Prevent default drag behaviors
;['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
    if (dropArea) { // Check if dropArea exists
        dropArea.addEventListener(eventName, preventDefaults, false);
    }
    document.body.addEventListener(eventName, preventDefaults, false); // Keep for body
});

// Highlight drop area when dragging over
;['dragenter', 'dragover'].forEach(eventName => {
    if (dropArea) {
        dropArea.addEventListener(eventName, highlight, false);
    }
});

// Remove highlight when dragging out
;['dragleave', 'drop'].forEach(eventName => {
    if (dropArea) {
        dropArea.addEventListener(eventName, unhighlight, false);
    }
});

// Handle dropped files
if (dropArea) {
    dropArea.addEventListener('drop', handleDrop, false);
     // Add click listener to trigger file input
    dropArea.addEventListener('click', () => {
        if (fileInput) fileInput.click();
    });
}

// Handle file input change
if (fileInput) {
    fileInput.addEventListener('change', function() {
        handleFiles(this.files);
    });
}


function preventDefaults(e) {
    e.preventDefault();
    e.stopPropagation();
}

function highlight(e) {
    if (dropArea) dropArea.classList.add('highlight');
}

function unhighlight(e) {
    if (dropArea) dropArea.classList.remove('highlight');
}

function handleDrop(e) {
    let dt = e.dataTransfer;
    let files = dt.files;
    handleFiles(files);
}

function handleFiles(files) {
    if (files.length === 0) return; // No files selected/dropped

    if (files.length > 1) {
        alert('Please upload only one image.'); // Alert if more than one file
        clearFileInput(); // Clear input if multiple files selected
        return;
    }

    const file = files[0]; // Process the single file

    // *** File Size Validation ***
    if (file.size > MAX_FILE_SIZE_BYTES) {
        alert(`File is too large. Maximum size is ${MAX_FILE_SIZE_MB}MB.`);
        clearFileInput(); // Clear the invalid file from input
        resetPreview(); // Reset preview and hidden data
        return; // Stop processing this file
    }

    // *** File Type Validation (Optional but recommended) ***
    if (!file.type.startsWith('image/')) {
        alert('Invalid file type. Please upload an image (e.g., JPG, PNG, GIF).');
        clearFileInput();
        resetPreview();
        return;
    }


    // If validation passes, proceed
    previewFile(file);
    uploadFile(file);
}

function previewFile(file) {
    if (!preview) return; // Check if preview element exists
    let reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = function () {
        preview.src = reader.result;
        preview.style.display = "block"; // Show preview
    };
}

function uploadFile(file) {
    if (!thumbnailData) return; // Check if hidden input exists
    let reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = function () {
        thumbnailData.value = reader.result; // Set Base64 data
    };
}

// Helper function to clear the file input
function clearFileInput() {
    if (fileInput) {
        try {
            fileInput.value = null; // Standard way
        } catch (ex) { } // Handle potential exceptions in older browsers
        if (fileInput.value) { // Fallback for some older browsers
            fileInput.parentNode.replaceChild(fileInput.cloneNode(true), fileInput);
            // Re-find the new fileInput and re-attach listener if using cloneNode
             fileInput = document.getElementById('thumbnail');
             if(fileInput) {
                  fileInput.addEventListener('change', function() {
                      handleFiles(this.files);
                  });
             }
        }
    }
}

// Helper function to reset preview and hidden data
function resetPreview() {
     if (preview) {
        preview.src = "";
        preview.style.display = "none";
    }
    if (thumbnailData) {
        thumbnailData.value = "";
    }
}