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

// otp
document.addEventListener("DOMContentLoaded", function () {
    const inputs = document.querySelectorAll('.otp-inputs input');
    const form = document.getElementById("otpForm");

    inputs.forEach((input, index) => {
        input.setAttribute("name", "otp" + index); // Give each input a unique name

        input.addEventListener("input", () => {
            if (input.value.length === 1 && index < inputs.length - 1) {
                inputs[index + 1].focus();
            }
        });

        input.addEventListener("keydown", (e) => {
            if (e.key === "Backspace" && input.value === "" && index > 0) {
                inputs[index - 1].focus();
            }
        });
    });

    form.addEventListener("submit", function (e) {
        const otp = Array.from(inputs).map(i => i.value).join('');
        const hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';
        hiddenInput.name = 'otp';
        hiddenInput.value = otp;
        form.appendChild(hiddenInput);
    });
});
