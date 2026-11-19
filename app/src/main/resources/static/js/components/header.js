
function renderHeader() {
    // Check whether the current page is the homepage
    if (window.location.pathname.endsWith("/")) {
        localStorage.removeItem("userRole");
        localStorage.removeItem("token");
    }

    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");

    // Validate the login session
    if (
        (role === "loggedPatient" || role === "admin" || role === "doctor") &&
        !token
    ) {
        localStorage.removeItem("userRole");
        alert("Session expired or invalid login. Please log in again.");
        window.location.href = "/";
        return;
    }

    const headerDiv = document.getElementById("header");

    if (!headerDiv) {
        return;
    }

    let headerContent = "";

    // Admin Header
    if (role === "admin") {
        headerContent += `
            <header class="header">
                <h2>Smart Clinic</h2>
                <nav>
                    <button
                        id="addDocBtn"
                        class="adminBtn"
                        onclick="openModal('addDoctor')">
                        Add Doctor
                    </button>
                    <a href="#" id="logoutBtn">Logout</a>
                </nav>
            </header>
        `;
    }

    // Doctor Header
    else if (role === "doctor") {
        headerContent += `
            <header class="header">
                <h2>Smart Clinic</h2>
                <nav>
                    <a href="/">Home</a>
                    <a href="#" id="logoutBtn">Logout</a>
                </nav>
            </header>
        `;
    }

    // Patient Header (Not Logged In)
    else if (role === "patient") {
        headerContent += `
            <header class="header">
                <h2>Smart Clinic</h2>
                <nav>
                    <a href="#" id="loginBtn">Login</a>
                    <a href="#" id="signupBtn">Sign Up</a>
                </nav>
            </header>
        `;
    }

    // Logged-in Patient Header
    else if (role === "loggedPatient") {
        headerContent += `
            <header class="header">
                <h2>Smart Clinic</h2>
                <nav>
                    <a href="/">Home</a>
                    <a href="#" id="appointmentsBtn">Appointments</a>
                    <a href="#" id="logoutPatientBtn">Logout</a>
                </nav>
            </header>
        `;
    }

    // Inject header HTML
    headerDiv.innerHTML = headerContent;

    // Attach event listeners after inserting HTML
    attachHeaderButtonListeners();
}

function attachHeaderButtonListeners() {
    const addDocBtn = document.getElementById("addDocBtn");

    if (addDocBtn) {
        addDocBtn.addEventListener("click", () => {
            if (typeof openModal === "function") {
                openModal("addDoctor");
            }
        });
    }

    const logoutBtn = document.getElementById("logoutBtn");

    if (logoutBtn) {
        logoutBtn.addEventListener("click", (event) => {
            event.preventDefault();
            logout();
        });
    }

    const logoutPatientBtn = document.getElementById("logoutPatientBtn");

    if (logoutPatientBtn) {
        logoutPatientBtn.addEventListener("click", (event) => {
            event.preventDefault();
            logoutPatient();
        });
    }

    const loginBtn = document.getElementById("loginBtn");

    if (loginBtn) {
        loginBtn.addEventListener("click", (event) => {
            event.preventDefault();
            if (typeof openModal === "function") {
                openModal("login");
            }
        });
    }

    const signupBtn = document.getElementById("signupBtn");

    if (signupBtn) {
        signupBtn.addEventListener("click", (event) => {
            event.preventDefault();
            if (typeof openModal === "function") {
                openModal("signup");
            }
        });
    }
}

// Logout for Admin and Doctor
function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("userRole");

    window.location.href = "/";
}

// Logout for Patient
function logoutPatient() {
    localStorage.removeItem("token");
    localStorage.setItem("userRole", "patient");

    window.location.href = "/pages/patientDashboard.html";
}

// Render the header when the page loads
document.addEventListener("DOMContentLoaded", renderHeader);
