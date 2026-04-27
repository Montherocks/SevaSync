
async function loginUser(payload) {
    const response = await fetch("http://localhost:5501/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
    });

    return await response.json();
}

async function handleLogin() {
    const payload = {
        email: document.getElementById("loginEmail").value,
        password: document.getElementById("loginPassword").value
    };

    const response = await loginUser(payload);

    if (response.jwtToken) {
        localStorage.setItem("jwtToken", response.jwtToken);
        localStorage.setItem("role", response.role);

        if (response.role === "VOLUNTEER") {
            window.location.href = "dashfinal.html";
        } 
        else if (response.role === "ADMIN") {
            window.location.href = "admindashboard.html";
        }
    } 
    else {
        alert(response.message || "Login failed");
    }
}

document.getElementById("loginForm").addEventListener("submit", function(e) {
    e.preventDefault();
    handleLogin();
});