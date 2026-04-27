
async function registerAdminDetails(payload) {
    const token = localStorage.getItem("jwtToken");

    const response = await fetch("http://localhost:5501/auth/registeradmin", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(payload)
    });

    return await response.json();
}

async function handleAdminFinish() {
    const payload = {
        organizationName: document.getElementById("organizationName").value,
        intention: document.getElementById("intention").value,
        location: document.getElementById("location").value
    };

    const response = await registerAdminDetails(payload);

    if (response.success) {
        window.location.href = "admindashboard.html";
    } 
    else {
        alert(response.message || "Admin registration failed");
    }
}

document.getElementById("adminForm").addEventListener("submit", function(e) {
    e.preventDefault();
    handleAdminFinish();
});