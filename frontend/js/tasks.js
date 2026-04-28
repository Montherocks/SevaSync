document.addEventListener("DOMContentLoaded", async function () {

    const tasksGrid = document.getElementById("tasksGrid");

    const BASE_URL = "http://localhost:8080";

    try {
        const response = await fetch(`${BASE_URL}/events/all`);

        if (!response.ok) {
            throw new Error("Failed to fetch events");
        }

        const events = await response.json();

        events.forEach(event => {

            let imagePath = "images/default.jpg";

            if (event.category === "Tree Planting") imagePath = "images/treeplanting.jpg";
            else if (event.category === "Park Cleanup") imagePath = "images/parkcleanup.jpg";
            else if (event.category === "River Cleanup") imagePath = "images/rivercleanup.jpg";
            else if (event.category === "Food Distribution") imagePath = "images/fooddistribution.jpg";
            else if (event.category === "Clothes Distribution") imagePath = "images/clothesdistribution.jpg";

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
                        
                        <!-- FIXED TIME DISPLAY -->
                        <span>
                            ${event.startTime ? event.startTime.slice(0, 5) : "--"} 
                            - 
                            ${event.endTime ? event.endTime.slice(0, 5) : "--"}
                        </span>

                        <span>${event.location}</span>
                    </div>

                    <button onclick="registerTask(${event.id}, this)">
                        Register
                    </button>

                </div>
            `;

            tasksGrid.appendChild(card);
        });

    } catch (error) {
        alert(error.message);
    }
});


// ================= REGISTER TASK =================

async function registerTask(eventId, button) {

    button.innerText = "Registering...";
    button.disabled = true;

    const userId = localStorage.getItem("userId");
    const token = localStorage.getItem("jwtToken");

    const BASE_URL = "http://localhost:8080";

    try {
        const response = await fetch(
            `${BASE_URL}/register/${eventId}`,
            {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
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