/* ============================================================
   Admin Dashboard – data, rendering, and interactions
   ============================================================ */

const data = {
  active: [],
  pending: [],
  completed: [],
  volunteers: [],
};

// ---- Section meta (titles + subtitles) ----
const sectionMeta = {
  active:     { title: "Active Tasks",        subtitle: "Tasks currently in progress." },
  pending:    { title: "Pending Tasks",       subtitle: "Tasks scheduled for the future." },
  completed:  { title: "Completed Tasks",     subtitle: "History of finished tasks." },
  volunteers: { title: "Volunteer Approvals", subtitle: "Review and approve volunteer applications." },
};

let currentSection = "active";

// ---- Helpers ----
const $ = (sel) => document.querySelector(sel);
const escapeHtml = (s) =>
  String(s).replace(/[&<>"']/g, (c) => ({ "&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;" }[c]));
const initials = (name) =>
  name.split(" ").map((n) => n[0]).slice(0, 2).join("").toUpperCase();

// ---- Update card counts ----
function updateCounts() {
  $("#count-active").textContent     = data.active.length;
  $("#count-pending").textContent    = data.pending.length;
  $("#count-completed").textContent  = data.completed.length;
  $("#count-volunteers").textContent = data.volunteers.length;
}

// ---- Render helpers ----
function emptyState(label) {
  return `
    <div class="empty">
      <div class="empty-icon"><i class="fa-solid fa-inbox"></i></div>
      <p>${escapeHtml(label)}</p>
    </div>`;
}

function renderTaskList(tasks, dateLabel, dateKey, emptyLabel) {
  if (!tasks.length) return emptyState(emptyLabel);
  return `
    <ul class="task-list">
      ${tasks.map((t) => `
        <li class="task-item">
          <span class="task-icon"><i class="fa-solid fa-bolt"></i></span>
          <div class="task-text">
            <p class="task-title">${escapeHtml(t.title)}</p>
            ${t[dateKey] ? `<p class="task-meta">${dateLabel}: ${escapeHtml(t[dateKey])}</p>` : ""}
          </div>
        </li>
      `).join("")}
    </ul>`;
}

function renderVolunteers(volunteers) {
  if (!volunteers.length) return emptyState("All caught up — no pending approvals.");
  return `
    <ul class="volunteer-list">
      ${volunteers.map((v) => `
        <li class="volunteer-item" data-id="${v.id}">
          <div class="volunteer-info">
            <div class="avatar">${escapeHtml(initials(v.name))}</div>
            <div>
              <p class="volunteer-name">${escapeHtml(v.name)}</p>
              <p class="volunteer-task">Applied for: ${escapeHtml(v.task)}</p>
            </div>
          </div>
          <div class="volunteer-actions">
            <button class="btn btn-approve" data-action="approve" data-id="${v.id}">
              <i class="fa-solid fa-check"></i> Approve
            </button>
            <button class="btn btn-reject" data-action="reject" data-id="${v.id}">
              <i class="fa-solid fa-xmark"></i> Reject
            </button>
          </div>
        </li>
      `).join("")}
    </ul>`;
}

// ---- Render the active section ----
function renderSection(section) {
  currentSection = section;

  // Highlight active card
  document.querySelectorAll(".card").forEach((c) => {
    c.classList.toggle("active", c.dataset.section === section);
  });

  // Update titles
  $("#panel-title").textContent    = sectionMeta[section].title;
  $("#panel-subtitle").textContent = sectionMeta[section].subtitle;

  // Render body
  const body = $("#panel-body");
  switch (section) {
    case "active":
      body.innerHTML = renderTaskList(data.active, "Due", "due", "No active tasks.");
      break;
    case "pending":
      body.innerHTML = renderTaskList(data.pending, "Scheduled", "due", "No pending tasks.");
      break;
    case "completed":
      body.innerHTML = renderTaskList(data.completed, "Completed", "completedOn", "No completed tasks yet.");
      break;
    case "volunteers":
      body.innerHTML = renderVolunteers(data.volunteers);
      break;
  }
}

// ---- Toast notifications ----
function showToast(message, type = "success") {
  const container = $("#toast-container");
  const toast = document.createElement("div");
  toast.className = `toast ${type}`;
  toast.textContent = message;
  container.appendChild(toast);
  setTimeout(() => {
    toast.style.opacity = "0";
    toast.style.transition = "opacity 0.3s ease";
    setTimeout(() => toast.remove(), 300);
  }, 2400);
}

// ---- Volunteer actions ----
function handleVolunteerAction(action, id) {
  const idx = data.volunteers.findIndex((v) => v.id === id);
  if (idx === -1) return;
  const v = data.volunteers[idx];
  data.volunteers.splice(idx, 1);
  updateCounts();
  renderSection("volunteers");

  if (action === "approve") {
    showToast(`${v.name} approved for "${v.task}"`, "success");
  } else {
    showToast(`${v.name}'s application rejected`, "error");
  }
}

async function loadAdminDetails() {
  try {
    const res = await fetch("/api/admin/profile", {
      method: "GET",
      credentials: "include" // important if using session/cookies
    });

    if (!res.ok) throw new Error("Failed to fetch admin data");

    const admin = await res.json();

    document.getElementById("admin-id").textContent =
      "Admin ID: " + admin.id;

  } catch (err) {
    console.error(err);
    document.getElementById("admin-id").textContent =
      "Admin ID: Not available";
  }
}

// ---- Event wiring ----
function init() {
  // Create Event Button Click
document.getElementById("createEventBtn").addEventListener("click", () => {
  showToast("Redirecting to Create Event page...", "success");
  window.location.href = "createevent.html";

  // Later replace with actual navigation
  // window.location.href = "/create-event.html";
});
  // Card clicks
  document.querySelectorAll(".card").forEach((card) => {
    card.addEventListener("click", () => renderSection(card.dataset.section));
  });

  // Delegated approve/reject buttons
  $("#panel-body").addEventListener("click", (e) => {
    const btn = e.target.closest("button[data-action]");
    if (!btn) return;
    handleVolunteerAction(btn.dataset.action, Number(btn.dataset.id));
  });

  updateCounts();
  renderSection("active");
  loadAdminDetails();
  //loadSampleData()
}

document.addEventListener("DOMContentLoaded", init);

document.getElementById("logoutBtn").addEventListener("click", function () {
    localStorage.removeItem("jwtToken");

    alert("Logged out successfully");

    window.location.href = "index.html";
});

// ---- TEMP TEST DATA (REMOVE LATER) ----
// function loadSampleData() {
//   data.active = [
//     { id: 1, title: "Community cleanup drive", due: "Today" },
//     { id: 2, title: "Food distribution – Sector 12", due: "Tomorrow" },
//   ];

//   data.pending = [
//     { id: 11, title: "Tree plantation event", due: "May 02" },
//   ];

//   data.completed = [
//     { id: 21, title: "Beach cleanup", completedOn: "Apr 18" },
//   ];

//   data.volunteers = [
//     { id: 101, name: "Aarav Sharma", task: "Tree plantation event" },
//   ];

//   updateCounts();
//   renderSection("active");
// }