document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.querySelector('form');
    if (!loginForm) {
        console.error("Login form not found!");
        return;
    }

    loginForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const usernameInput = document.querySelector('input[name="username"]');
        const passwordInput = document.querySelector('input[name="password"]');

        if (!usernameInput || !passwordInput) {
            alert("One or more fields are missing!");
            return;
        }

        try {
            const response = await fetch('/req/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    username: usernameInput.value,
                    password: passwordInput.value
                })
            });

            if (response.ok) {
                window.location.href = '/re/login';  // Redirect on successful login
            } else {
                alert("Invalid login credentials.");
            }
        } catch (error) {
            alert("Error logging in. Please try again.");
        }
    });
});
