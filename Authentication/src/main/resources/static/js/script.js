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

// model box
document.addEventListener("DOMContentLoaded", () => {
    const modal = document.getElementById("errorModal");
    if (modal) {
        // Wait for a user click AFTER load
        setTimeout(() => {
            document.addEventListener("click", () => {
                modal.style.display = "none";
            });
        }, 300); // Delay to let modal appear first
    }
});

// view password
function togglePassword(id, icon) {
    const input = document.getElementById(id);
    if (input.type === "password") {
        input.type = "text";
        icon.classList.remove("fa-eye");
        icon.classList.add("fa-eye-slash");
    } else {
        input.type = "password";
        icon.classList.remove("fa-eye-slash");
        icon.classList.add("fa-eye");
    }
}

// Countdown timer
let timer = 60;
const timerDisplay = document.getElementById("timer");
const resendBtn = document.getElementById("resendBtn");

const countdown = setInterval(() => {
    if (timer > 0) {
        timer--;
        const minutes = Math.floor(timer / 60);
        const seconds = timer % 60;
        timerDisplay.textContent = `Resend OTP in ${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
    } else {
        clearInterval(countdown);
        timerDisplay.style.display = 'none';
        resendBtn.style.display = 'inline-block';
    }
}, 1000);

//resend otp button action
resendBtn.addEventListener('click', () => {
    const form = document.createElement('form');
    form.method = 'post';
    form.action = '/resend-otp';

    const emailInput = document.querySelector('[name="email"]');
    const email = emailInput.value;

    const hiddenEmail = document.createElement('input');
    hiddenEmail.type = 'hidden';
    hiddenEmail.name = 'email';
    hiddenEmail.value = email;

    form.appendChild(hiddenEmail);
    document.body.appendChild(form);
    form.submit();
});

