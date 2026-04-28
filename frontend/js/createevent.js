document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("eventForm");

    const startTimeInput = document.getElementById("startTime");
    const endTimeInput = document.getElementById("endTime");
    const durationText = document.getElementById("durationText");

    function calculateDuration() {
        const start = startTimeInput.value;
        const end = endTimeInput.value;

        if (!start || !end) {
            durationText.textContent = "Duration: --";
            return null;
        }

        const [sh, sm] = start.split(":").map(Number);
        const [eh, em] = end.split(":").map(Number);

        let startMinutes = sh * 60 + sm;
        let endMinutes = eh * 60 + em;

        let diff = endMinutes - startMinutes;

        if (diff <= 0) {
            durationText.textContent = "Invalid time range";
            return null;
        }

        let hours = diff / 60;

        if (hours === 1) {
            durationText.textContent = "Duration: 1 hour";
        } else {
            durationText.textContent = `Duration: ${hours} hours`;
        }

        return {
            startTime: start,
            endTime: end,
            durationHours: hours
        };
    }

    startTimeInput.addEventListener("change", calculateDuration);
    endTimeInput.addEventListener("change", calculateDuration);

    form.addEventListener("submit", async function (e) {
        e.preventDefault();

        const selectedCategory = document.querySelector('input[name="category"]:checked');

        if (!selectedCategory) {
            alert("Please select a category");
            return;
        }

        const durationData = calculateDuration();

        if (!durationData) {
            alert("Please enter valid start and end times");
            return;
        }

        const payload = {
            name: document.getElementById("eventName").value,
            description: document.getElementById("description").value,
            category: selectedCategory.value,
            date: document.getElementById("date").value,
            location: document.getElementById("location").value,

            // NEW FIELDS
            startTime: durationData.startTime,
            endTime: durationData.endTime,
            durationHours: durationData.durationHours
        };

        try {
            const response = await fetch(`${API_BASE_URL}/events/create`, {
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