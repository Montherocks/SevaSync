document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("eventForm");

    form.addEventListener("submit", async function (e) {
        e.preventDefault();

        const selectedCategory = document.querySelector('input[name="category"]:checked');

        if (!selectedCategory) {
            alert("Please select a category");
            return;
        }

        const payload = {
            name: document.getElementById("eventName").value,
            description: document.getElementById("description").value,
            category: selectedCategory.value,
            date: document.getElementById("date").value,
            time: document.getElementById("time").value,
            location: document.getElementById("location").value
        };

        try {
            const response = await fetch("http://localhost:8080/events/create", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + localStorage.getItem("jwtToken")
                },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                throw new Error("Failed to create event");
            }

            alert("Event Created Successfully");
            window.location.href = "admindashboard.html";

        } catch (error) {
            alert(error.message);
        }
    });

});