// form-handler.js

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('registerForm');
    const message = document.getElementById('confirmationMessage');

    form.addEventListener('submit', function (e) {
        e.preventDefault(); // Prevent real submission (for demo)

        // Simulate a delay (like a network request)
        setTimeout(() => {
            message.style.display = 'block';

            // Optional: clear form
            form.reset();
        }, 300); // 300ms delay
    });
});
