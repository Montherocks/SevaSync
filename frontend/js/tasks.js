document.addEventListener("DOMContentLoaded", async function () {

    const tasksGrid = document.getElementById("tasksGrid");

    try {
        const response = await fetch("http://localhost:8080/events/all");

        if (!response.ok) {
            throw new Error("Failed to fetch events");
        }

        const events = await response.json();

        events.forEach(event => {

            let imagePath = "";

            switch (event.category) {
                case "Tree Planting":
                    imagePath = "images/treeplanting.jpg";
                    break;
                case "Park Cleanup":
                    imagePath = "images/parkcleanup.jpg";
                    break;
                case "River Cleanup":
                    imagePath = "images/rivercleanup.jpg";
                    break;
                case "Food Distribution":
                    imagePath = "images/fooddistribution.jpg";
                    break;
                case "Clothes Distribution":
                    imagePath = "images/clothesdistribution.jpg";
                    break;
            }

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
                        <span>${event.time}</span>
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

async function registerTask(eventId, button) {

    button.innerText = "Confirming";
    button.disabled = true;

    const userId = localStorage.getItem("userId");

    try {
        const response = await fetch(`http://localhost:8080/register?userId=${userId}&eventId=${eventId}`, {
            method: "POST"
        });

        if (!response.ok) {
            throw new Error("Registration failed");
        }

    } catch (error) {
        button.innerText = "Register";
        button.disabled = false;
        alert(error.message);
    }
}