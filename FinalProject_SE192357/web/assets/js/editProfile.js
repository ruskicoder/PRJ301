let dropArea = document.getElementById('drop-area');
let preview = document.getElementById('preview');
let userData = document.getElementById('userImageData');
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
    ([...files]).forEach(previewFile);
    ([...files]).forEach(uploadFile);
}
function previewFile(file) {
    let reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = function () {
        preview.src = reader.result;
        preview.style.display = "block";
    };
}
function uploadFile(file) {
    let reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = function () {
        userData.value = reader.result; // Set Base64 data
    };
}

let dropAreaAdmin = document.getElementById('drop-area-admin');
if (dropAreaAdmin !== null) {
    let previewAdmin = document.getElementById('preview-admin');
    let adminData = document.getElementById('adminUserImageData');
    let fileInputAdmin = document.getElementById('adminUserImage');

    // Prevent default drag behaviors
    ;
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        dropAreaAdmin.addEventListener(eventName, preventDefaults, false);
        document.body.addEventListener(eventName, preventDefaults, false);
    });

    // Highlight drop area when dragging over
    ;
    ['dragenter', 'dragover'].forEach(eventName => {
        dropAreaAdmin.addEventListener(eventName, highlightAdmin, false);
    });

    // Remove highlight when dragging out
    ;
    ['dragleave', 'drop'].forEach(eventName => {
        dropAreaAdmin.addEventListener(eventName, unhighlightAdmin, false);
    });

    // Handle dropped files
    dropAreaAdmin.addEventListener('drop', handleDropAdmin, false);
    function handleDropAdmin(e) {
        let dt = e.dataTransfer;
        let files = dt.files;
        handleFilesAdmin(files);
    }
    function handleFilesAdmin(files) {
        ([...files]).forEach(previewFileAdmin);
        ([...files]).forEach(uploadFileAdmin);
    }
    function highlightAdmin(e) {
        dropAreaAdmin.classList.add('highlight');
    }

    function unhighlightAdmin(e) {
        dropAreaAdmin.classList.remove('highlight');
    }
    function previewFileAdmin(file) {
        let reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = function () {
            previewAdmin.src = reader.result;
            previewAdmin.style.display = "block";
        };
    }
    function uploadFileAdmin(file) {
        let reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = function () {
            adminData.value = reader.result; // Set Base64 data
        };
    }
}
