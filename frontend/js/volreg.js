async function registerVolunteerDetails(payload) {
    const token = localStorage.getItem("jwtToken");

    console.log("Stored Token:", token);

    const response = await fetch("http://localhost:8080/auth/registervolunteer", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
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
        throw new Error(data.message || "Volunteer registration failed");
    }

    return data;
}

async function handleVolunteerFinish() {
    const payload = {
        skill: document.getElementById("skills").value,
        location: document.getElementById("location").value,
        availabilityStart: document.getElementById("startDate").value,
        availabilityEnd: document.getElementById("endDate").value
    };

    try {
        const response = await registerVolunteerDetails(payload);

        console.log("Volunteer Registration Response:", response);

        window.location.href = "dashfinal.html";

    } catch (error) {
        console.error("Volunteer Registration Error:", error);
        alert(error.message);
    }
}

document.getElementById("volunteerForm").addEventListener("submit", function(e) {
    e.preventDefault();
    handleVolunteerFinish();
});