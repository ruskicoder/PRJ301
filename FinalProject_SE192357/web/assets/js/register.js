let dropArea = document.getElementById('drop-area');
let preview = document.getElementById('preview');
let thumbnailData = document.getElementById('userImageData');
let fileInput = document.getElementById('userImage');

// Prevent default drag behaviors
;
['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
    dropArea.addEventListener(eventName, preventDefaults, false);
    document.body.addEventListener(eventName, preventDefaults, false);
});

// Highlight drop area when dragging over
;
['dragenter', 'dragover'].forEach(eventName => {
    dropArea.addEventListener(eventName, highlight, false);
});

// Remove highlight when dragging out
;
['dragleave', 'drop'].forEach(eventName => {
    dropArea.addEventListener(eventName, unhighlight, false);
});

// Handle dropped files
dropArea.addEventListener('drop', handleDrop, false);

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
        alert('Please upload only one image.'); // Alert if more than one file
        return;
    }
    ([...files]).forEach(previewFile); //Correct
    ([...files]).forEach(uploadFile);
}
function previewFile(file) {
    let reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = function () {
        preview.src = reader.result;
        preview.style.display = "block"; // Show preview
    };
}
function uploadFile(file) {
    let reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = function () {
        thumbnailData.value = reader.result; // Set Base64 data
    };
}
