document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.querySelector('form');

    loginForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const usernameInput = document.querySelector('input[name="username"]').value;
        const passwordInput = document.querySelector('input[name="password"]').value;

        try {
            const response = await fetch('/req/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: new URLSearchParams({
                    username: usernameInput,
                    password: passwordInput
                })
            });

            if (response.redirected) {
                window.location.href = response.url;  // Redirect if server sends a redirect response
            } else if (response.ok) {
                alert("Login successful!");
                window.location.href = '/index';  // Redirect on success
            } else {
                alert("Invalid login credentials. Please try again.");
            }
        } catch (error) {
            alert("Error connecting to the server. Please try again later.");
            console.error("Login error:", error);
        }
    });
});
