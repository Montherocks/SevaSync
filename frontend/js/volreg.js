
async function registerVolunteerDetails(payload) {
    const token = localStorage.getItem("jwtToken");

    const response = await fetch("http://localhost:5501/volunteer/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(payload)
    });

    return await response.json();
}

async function handleVolunteerFinish() {
    const payload = {
        skill: document.getElementById("skills").value,
        location: document.getElementById("location").value,
        availabilityStart: document.getElementById("startDate").value,
        availabilityEnd: document.getElementById("endDate").value
    };

    const response = await registerVolunteerDetails(payload);

    if (response.success) {
        window.location.href = "dashfinal.html";
    } 
    else {
        alert(response.message || "Volunteer registration failed");
    }
}

document.getElementById("volunteerForm").addEventListener("submit", function(e) {
    e.preventDefault();
    handleVolunteerFinish();
});