
async function registerUser(payload) {
    const response = await fetch("http://localhost:5501/auth/signup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
    });

    // safer parsing
    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Signup failed");
    }

    return data;
}

async function handleGetStarted() {
    const role = document.getElementById("roleSelect").value;

    if (!role) {
        alert("Please select a role");
        return;
    }

    const payload = {
        name: document.getElementById("name").value,
        password: document.getElementById("password").value,
        role: role.toUpperCase(),
        emailaddress: document.getElementById("email").value
    };

    try {
        const response = await registerUser(payload);

        localStorage.setItem("jwtToken", response.jwtToken);
        localStorage.setItem("role", response.role);

        if (role === "volunteer") {
            window.location.href = "volreg.html";
        } 
        else if (role === "admin") {
            window.location.href = "adminreg.html";
        }

    } catch (error) {
        console.error("Signup Error:", error);
        alert(error.message);
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("roleForm");

    if (form) {
        form.addEventListener("submit", function (e) {
            e.preventDefault();
            handleGetStarted();
        });
    }
});