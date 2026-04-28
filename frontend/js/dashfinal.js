/* ---------- Data ---------- */
const addDays = (n) => {
  const d = new Date();
  d.setDate(d.getDate() + n);
  d.setHours(9, 30, 0, 0);
  return d;
};

let upcomingTasks = [];
let completedTasks = [];
let kpiData = {
  volunteerHours: 0,
  tasksPending: 0,
  tasksCompleted: 0
};

/* ---------- Count-up KPI animation ---------- */
function animateCounters(){
  document.querySelectorAll('[data-count]').forEach(el=>{
    const target = +el.dataset.count;
    const dur = 1200;
    const start = performance.now();
    const tick = (now)=>{
      const p = Math.min(1,(now-start)/dur);
      const eased = 1 - Math.pow(1-p,3);
      el.textContent = Math.round(eased*target).toLocaleString();
      if(p<1) requestAnimationFrame(tick);
    };
    requestAnimationFrame(tick);
  });
}

/* ---------- Calendar ---------- */
let viewDate = new Date();
let selectedDate = new Date();
const MONTHS = ["January","February","March","April","May","June",
  "July","August","September","October","November","December"];

function sameDay(a,b){
  return a.getFullYear()===b.getFullYear() && a.getMonth()===b.getMonth() && a.getDate()===b.getDate();
}

function renderCalendar(){
  const title = document.getElementById('calTitle');
  const grid = document.getElementById('calDays');
  const y = viewDate.getFullYear(), m = viewDate.getMonth();
  title.textContent = `${MONTHS[m]} ${y}`;

  const first = new Date(y,m,1);
  const startDay = first.getDay();
  const daysInMonth = new Date(y,m+1,0).getDate();
  const prevDays = new Date(y,m,0).getDate();
  const today = new Date();

  const cells = [];
  for(let i=startDay-1;i>=0;i--){
    cells.push({ d:prevDays-i, muted:true, date:new Date(y,m-1,prevDays-i) });
  }
  for(let d=1; d<=daysInMonth; d++){
    cells.push({ d, muted:false, date:new Date(y,m,d) });
  }
  while(cells.length % 7 !== 0){
    const d = cells.length - (startDay+daysInMonth) + 1;
    cells.push({ d, muted:true, date:new Date(y,m+1,d) });
  }

  grid.innerHTML = cells.map(c=>{
    const isToday = sameDay(c.date,today) && !c.muted;
    const hasTask = upcomingTasks.some(t=>sameDay(t.date,c.date)) && !c.muted;
    const cls = ['day'];
    if(c.muted) cls.push('muted');
    if(isToday) cls.push('today');
    if(hasTask) cls.push('has-task');
    return `<div class="${cls.join(' ')}" data-date="${c.date.toISOString()}">${c.d}</div>`;
  }).join('');
}

document.getElementById('calDays').addEventListener('click', (e) => {
  const day = e.target.closest('.day');
  if (!day || day.classList.contains('muted')) return;

  selectedDate = new Date(day.dataset.date);

  // Highlight selected day
  document.querySelectorAll('.day').forEach(d => d.classList.remove('selected'));
  day.classList.add('selected');

  renderUpcoming(); // update tasks
});

document.getElementById('prevMonth').addEventListener('click',()=>{
  viewDate = new Date(viewDate.getFullYear(), viewDate.getMonth()-1, 1);
  renderCalendar();
});
document.getElementById('nextMonth').addEventListener('click',()=>{
  viewDate = new Date(viewDate.getFullYear(), viewDate.getMonth()+1, 1);
  renderCalendar();
});

/* ---------- Upcoming task list ---------- */
function renderUpcoming(){
  const list = document.getElementById('taskList');
  const fmt = new Intl.DateTimeFormat('en',{ month:'short', day:'numeric', hour:'numeric', minute:'2-digit' });

  // Filter tasks: show tasks on or AFTER selected date
  const filteredTasks = upcomingTasks.filter(t => t.date >= selectedDate);

  if(filteredTasks.length === 0){
    list.innerHTML = `<p class="empty">No tasks scheduled. Enjoy the calm!</p>`;
    return;
  }

  list.innerHTML = filteredTasks.map(t=>`
    <li class="task-item">
      <p class="task-name">${t.name}</p>
      <div class="task-meta">
        <span>📅 ${fmt.format(t.date)}</span>
        <span>📍 ${t.location}</span>
      </div>
      <p class="task-desc">${t.description}</p>
    </li>
  `).join('');

  document.getElementById('taskCount').textContent = filteredTasks.length;
}

/* ---------- Completed table ---------- */
function renderTable(filter=""){
  const body = document.getElementById('tableBody');
  const emptyBlock = document.getElementById('emptyBlock');

  const q = filter.trim().toLowerCase();

  const rows = completedTasks.filter(t =>
    !q || t.name.toLowerCase().includes(q) || t.volunteer.toLowerCase().includes(q)
  );

  if(rows.length === 0){
    body.innerHTML = "";
    emptyBlock.hidden = false;
    return;
  }

  emptyBlock.hidden = true;

  body.innerHTML = rows.map(t=>`
    <tr>
      <td>${t.name}</td>
      <td class="num">${t.hours}</td>
      <td>${t.volunteer}</td>
      <td>${t.completedOn}</td>
      <td><span class="status-pill">Completed</span></td>
    </tr>
  `).join('');
}

document.getElementById('searchInput').addEventListener('input', e => renderTable(e.target.value));

// ----------- SET KPI DATA -----------
function setKPIData(data){
  kpiData = data;

  // Main values
  document.getElementById("volunteerHours").textContent = data.volunteerHours || 0;
  document.getElementById("tasksPending").textContent = data.tasksPending || 0;
  document.getElementById("tasksCompleted").textContent = data.tasksCompleted || 0;

  // Trends (IMPORTANT)
  document.getElementById("trendVolunteer").textContent = data.trendVolunteer || "";
  document.getElementById("trendPending").textContent = data.trendPending || "";
  document.getElementById("trendCompleted").textContent = data.trendCompleted || "";
}

// ----------- SET UPCOMING TASKS -----------
function setUpcomingTasks(tasks){
  upcomingTasks = tasks.map(t => ({
    ...t,
    date: new Date(t.date)
  }));

  renderCalendar();
  renderUpcoming();
}

// ----------- SET COMPLETED TASKS -----------
function setCompletedTasks(tasks){
  completedTasks = tasks;
  renderTable();
}

/* ---------- Init ---------- */
document.getElementById('year').textContent = new Date().getFullYear();
renderCalendar();
renderUpcoming();
renderTable();
loadVolunteerProfile();

async function loadVolunteerProfile() {
  const token = localStorage.getItem("jwtToken");

  if (!token) {
    window.location.href = "login.html";
    return;
  }

  try {
    const response = await fetch("http://localhost:8080/volunteer/profile", {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });

    if (!response.ok) {
      throw new Error("Failed to fetch volunteer profile");
    }

    const volunteer = await response.json();

    document.getElementById("volunteerName").textContent =
      volunteer.name || "Volunteer";

    document.getElementById("volunteerRole").textContent =
      "Volunteer";

    document.getElementById("volunteerAvatar").textContent =
      (volunteer.name?.charAt(0) || "V").toUpperCase();

  } catch (error) {
    console.error("Profile Load Error:", error);
  }
}

document.getElementById("logoutBtn").addEventListener("click", function () {
    localStorage.removeItem("jwtToken");

    alert("Logged out successfully");

    window.location.href = "index.html";
});
// setKPIData({
//   volunteerHours: 100,
//   tasksPending: 3,
//   tasksCompleted: 10,
//   trendVolunteer: "▲ 15% vs last month",
//   trendPending: "Next due in 2 days",
//   trendCompleted: "▲ 5 this month"
// });