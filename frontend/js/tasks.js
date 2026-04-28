document.addEventListener("DOMContentLoaded", async function () {

    const tasksGrid = document.getElementById("tasksGrid");
    const BASE_URL = "http://localhost:8080";

    const token = localStorage.getItem("jwtToken");

    try {
        const [regResponse, eventResponse] = await Promise.all([
            fetch(`${BASE_URL}/register/myRegistration`, {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }),
            fetch(`${BASE_URL}/events/all`)
        ]);

        if (!eventResponse.ok) {
            throw new Error("Failed to fetch events");
        }

        const registrations = regResponse.ok ? await regResponse.json() : [];
        const events = await eventResponse.json();

        console.log("Registrations:", registrations);

        const registeredEventIds = new Set(
            registrations.map(r => r.eventId)
        );

        tasksGrid.innerHTML = "";

        events.forEach(event => {

            let imagePath = "images/default.jpg";

            if (event.category === "Tree Planting") imagePath = "images/treeplanting.jpg";
            else if (event.category === "Park Cleanup") imagePath = "images/parkcleanup.jpg";
            else if (event.category === "River Cleanup") imagePath = "images/rivercleanup.jpg";
            else if (event.category === "Food Distribution") imagePath = "images/fooddistribution.jpg";
            else if (event.category === "Clothes Distribution") imagePath = "images/clothesdistribution.jpg";

            const isRegistered = registeredEventIds.has(event.id);

            const card = document.createElement("div");
            card.classList.add("task-card");

            card.innerHTML = `
                <img src="${imagePath}" alt="${event.category}">

                <div class="task-content">

                    <div class="task-header">
                        <h2>${event.name}</h2>
                        <span class="category">${event.category}</span>
                    </div>

                    <p>${event.description}</p>

                    <div class="task-details">
                        <span>${event.date}</span>

                        <span>
                            ${event.startTime ? event.startTime.slice(0, 5) : "--"} 
                            - 
                            ${event.endTime ? event.endTime.slice(0, 5) : "--"}
                        </span>

                        <span>${event.location}</span>
                    </div>

                    <button 
                        onclick="registerTask(${event.id}, this)"
                        ${isRegistered ? "disabled" : ""}
                    >
                        ${isRegistered ? "Registered ✔" : "Register"}
                    </button>

                </div>
            `;

            tasksGrid.appendChild(card);
        });

    } catch (error) {
        console.error(error);
        alert("Failed to load events");
    }
});


// ================= REGISTER TASK =================

async function registerTask(eventId, button) {

    button.innerText = "Registering...";
    button.disabled = true;

    const token = localStorage.getItem("jwtToken");
    const BASE_URL = "http://localhost:8080";

    try {
        const response = await fetch(
            `${BASE_URL}/register/${eventId}`,
            {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        if (!response.ok) {
            throw new Error("Registration failed");
        }

        button.innerText = "Registered ✔";

    } catch (error) {
        button.innerText = "Register";
        button.disabled = false;
        alert(error.message);
    }
}