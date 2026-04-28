async function loginUser(payload) {
    const response = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
    });

    let data = {};

    try {
        data = await response.json();
    } catch (error) {
        data = {};
    }

    if (!response.ok) {
        throw new Error(data.message || "Login failed");
    }

    return data;
}

async function handleLogin() {
    const payload = {
        email: document.getElementById("loginEmail").value,
        password: document.getElementById("loginPassword").value
    };

    try {
        const response = await loginUser(payload);

        console.log("Login Response:", response);

        localStorage.setItem("jwtToken", response.token || response.jwtToken);
        localStorage.setItem("role", response.role);

        if (response.role === "VOLUNTEER") {
            window.location.href = "dashfinal.html";
        } 
        else if (response.role === "ADMIN") {
            window.location.href = "admindashboard.html";
        } 
        else {
            alert("Role not recognized");
        }

    } catch (error) {
        console.error("Login Error:", error);
        alert(error.message);
    }
}

document.getElementById("loginForm").addEventListener("submit", function(e) {
    e.preventDefault();
    handleLogin();
});