/* ============================================================
   Admin Dashboard – API-driven version (UPDATED)
   ============================================================ */

// ---- STATE ----
const data = {
  active: 0,
  pending: 0,
  completed: 0,
  volunteers: []
};

let completedList = [];
let currentSection = "active";

// ---- META ----
const sectionMeta = {
  active:     { title: "Active Tasks", subtitle: "Tasks currently in progress." },
  pending:    { title: "Pending Tasks", subtitle: "Tasks scheduled for the future." },
  completed:  { title: "Completed Tasks", subtitle: "History of finished tasks." },
  volunteers: { title: "Volunteer Approvals", subtitle: "Review and approve volunteer applications." },
};

// ---- HELPERS ----
const $ = (sel) => document.querySelector(sel);

const escapeHtml = (s) =>
  String(s).replace(/[&<>"']/g, (c) =>
    ({ "&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;" }[c])
  );

const initials = (name) =>
  (name || "")
    .split(" ")
    .map(n => n[0])
    .slice(0, 2)
    .join("")
    .toUpperCase();

// ---- COUNTS ----
function updateCounts() {
  $("#count-active").textContent = data.active;
  $("#count-pending").textContent = data.pending;
  $("#count-completed").textContent = data.completed;
  $("#count-volunteers").textContent = data.volunteers.length;
}

// ---- EMPTY ----
function emptyState(msg) {
  return `
    <div class="empty">
      <div class="empty-icon"><i class="fa-solid fa-inbox"></i></div>
      <p>${escapeHtml(msg)}</p>
    </div>
  `;
}

// ---- COMPLETED LIST ----
function renderCompletedList() {
  if (!completedList.length) return emptyState("No completed tasks yet.");

  return `
    <ul class="task-list">
      ${completedList.map(e => `

        <li class="task-item">
          <span class="task-icon"><i class="fa-solid fa-circle-check"></i></span>

          <div class="task-text">

            <p class="task-title">
              ${escapeHtml(
                typeof (e.name || e.title) === "object"
                  ? (e.name?.name || e.title?.name || JSON.stringify(e.name || e.title))
                  : (e.name || e.title)
              )}
            </p>

            <p class="task-meta">
              Ended: ${escapeHtml(
                typeof e.endTime === "object"
                  ? JSON.stringify(e.endTime)
                  : e.endTime || "N/A"
              )}
            </p>

          </div>
        </li>

      `).join("")}
    </ul>
  `;
}

// ---- VOLUNTEERS ----
function renderVolunteers(volunteers) {
  if (!volunteers.length) return emptyState("No pending approvals.");

  return `
    <ul class="volunteer-list">
      ${volunteers.map(v => `
        <li class="volunteer-item" data-id="${v.id}">
          <div class="volunteer-info">
            <div class="avatar">${escapeHtml(initials(v.userName))}</div>
            <div>
              <p class="volunteer-name">${escapeHtml(v.userName)}</p>
              <p class="volunteer-task">Applied for: ${escapeHtml(v.eventName)}</p>
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
    </ul>
  `;
}

// ---- RENDER SECTION ----
async function renderSection(section) {
  currentSection = section;

  document.querySelectorAll(".card").forEach(c =>
    c.classList.toggle("active", c.dataset.section === section)
  );

  $("#panel-title").textContent = sectionMeta[section].title;
  $("#panel-subtitle").textContent = sectionMeta[section].subtitle;

  const body = $("#panel-body");

  if (section === "completed") {
    await loadCompletedList();
    body.innerHTML = renderCompletedList();
    return;
  }

  if (section === "active") {
    body.innerHTML = emptyState("Active tasks are shown as count only.");
    return;
  }

  if (section === "pending") {
    body.innerHTML = emptyState("Pending tasks are shown as count only.");
    return;
  }

  if (section === "volunteers") {
    body.innerHTML = renderVolunteers(data.volunteers);
    return;
  }
}

// ---- TOAST ----
function showToast(msg, type = "success") {
  const container = $("#toast-container");

  const toast = document.createElement("div");
  toast.className = `toast ${type}`;
  toast.textContent = msg;

  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = "0";
    setTimeout(() => toast.remove(), 300);
  }, 2400);
}

// ---- VOLUNTEER ACTIONS ----
async function handleVolunteerAction(action, id) {
  const token = localStorage.getItem("jwtToken");

  const endpoint =
    action === "approve"
      ? `${API_BASE_URL}/admin/registrations/approve?id=${id}`
      : `${API_BASE_URL}/admin/registrations/reject?id=${id}`;

  try {
    const res = await fetch(endpoint, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (!res.ok) throw new Error("Action failed");

    // ✅ Remove from UI only AFTER success
    const idx = data.volunteers.findIndex(v => v.id === id);
    if (idx !== -1) {
      const v = data.volunteers[idx];
      data.volunteers.splice(idx, 1);

      updateCounts();
      renderSection("volunteers");

      showToast(
        action === "approve"
          ? `${v.userName} approved`
          : `${v.userName} rejected`,
        action === "approve" ? "success" : "error"
      );
    }

  } catch (error) {
    console.error(error);
    showToast("Server error. Try again.", "error");
  }
}

// ---- LOAD COUNTS ----
async function loadCounts() {
  const token = localStorage.getItem("jwtToken");
  const headers = { Authorization: `Bearer ${token}` };

  const [a, p, c] = await Promise.all([
    fetch(`${API_BASE_URL}/admin/tasks/active/count`, { headers }),
    fetch(`${API_BASE_URL}/admin/tasks/pending/count`, { headers }),
    fetch(`${API_BASE_URL}/admin/tasks/completed/count`, { headers })
  ]);

  const activeJson = await a.json();
  const pendingJson = await p.json();
  const completedJson = await c.json();

  // ✅ FIX HERE (adjust key if needed)
  data.active = activeJson.count ?? activeJson;
  data.pending = pendingJson.count ?? pendingJson;
  data.completed = completedJson.count ?? completedJson;

  updateCounts();
}

// ---- LOAD COMPLETED LIST ----
async function loadCompletedList() {
  const token = localStorage.getItem("jwtToken");

  const res = await fetch(`${API_BASE_URL}/admin/tasks/completed`, {
    headers: { Authorization: `Bearer ${token}` }
  });

  completedList = await res.json();
}

// ---- LOAD VOLUNTEERS ----
async function loadVolunteers() {
  const token = localStorage.getItem("jwtToken");

  const res = await fetch(`${API_BASE_URL}/admin/registrations/pending`, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });

  data.volunteers = await res.json();
  updateCounts();
}

// ---- ADMIN PROFILE ----
async function loadAdminDetails() {
  const token = localStorage.getItem("jwtToken");

  const res = await fetch(`${API_BASE_URL}/admin/profile`, {
    headers: { Authorization: `Bearer ${token}` }
  });

  const admin = await res.json();

  $("#adminName").textContent = admin.name || "Admin";
  $("#adminAvatar").textContent = (admin.name?.charAt(0) || "A").toUpperCase();
}

// ---- INIT ----
async function init() {
  document.querySelectorAll(".card").forEach(card => {
    card.addEventListener("click", () =>
      renderSection(card.dataset.section)
    );
  });

  $("#panel-body").addEventListener("click", e => {
    const btn = e.target.closest("button[data-action]");
    if (!btn) return;

    handleVolunteerAction(btn.dataset.action, Number(btn.dataset.id));
  });

  document.getElementById("createEventBtn").onclick = () => {
    showToast("Redirecting...");
    window.location.href = "createevent.html";
  };

  document.getElementById("logoutBtn").onclick = () => {
    localStorage.removeItem("jwtToken");
    window.location.href = "index.html";
  };

  await loadCounts();
  await loadVolunteers();
  loadAdminDetails();

  renderSection("active");
}

document.addEventListener("DOMContentLoaded", init);