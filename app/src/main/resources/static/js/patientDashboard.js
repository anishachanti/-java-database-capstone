
import { createDoctorCard } from "./components/doctorCard.js";
import { openModal } from "./components/modals.js";
import {
    getDoctors,
    filterDoctors
} from "./services/doctorServices.js";
import {
    patientLogin,
    patientSignup
} from "./services/patientServices.js";

// Get the content container
const contentDiv = document.getElementById("content");

// Load all doctor cards
async function loadDoctorCards() {
    try {
        // Fetch all doctors from the backend
        const doctors = await getDoctors();

        // Render the doctor cards
        renderDoctorCards(doctors);

    } catch (error) {
        console.error("Error loading doctors:", error);
        contentDiv.innerHTML = "<p>Unable to load doctors.</p>";
    }
}

// Render doctor cards
function renderDoctorCards(doctors) {
    contentDiv.innerHTML = "";

    if (!doctors || doctors.length === 0) {
        contentDiv.innerHTML = "<p>No doctors found.</p>";
        return;
    }

    doctors.forEach(doctor => {
        const card = createDoctorCard(doctor);
        contentDiv.appendChild(card);
    });
}

// Search and filter event handling
async function filterDoctorsOnChange() {
    const name = document.getElementById("searchBar").value;
    const time = document.getElementById("filterTime").value;
    const specialty = document.getElementById("filterSpecialty").value;

    try {
        // Fetch doctors matching the selected filters
        const doctors = await filterDoctors(name, time, specialty);

        // Display filtered doctor cards
        renderDoctorCards(doctors);

    } catch (error) {
        console.error("Error filtering doctors:", error);
        contentDiv.innerHTML = "<p>No doctors found with the given filters.</p>";
    }
}

// Patient signup handler
window.signupPatient = async function () {
    // Collect patient registration details
    const name = document.getElementById("patientName").value;
    const email = document.getElementById("patientEmail").value;
    const password = document.getElementById("patientPassword").value;
    const phone = document.getElementById("patientPhone").value;
    const address = document.getElementById("patientAddress").value;

    const data = {
        name,
        email,
        password,
        phone,
        address
    };

    try {
        // Send signup request
        const result = await patientSignup(data);

        if (result.success) {
            alert(result.message);
            document.getElementById("modal").style.display = "none";
            window.location.reload();
        } else {
            alert(result.message);
        }

    } catch (error) {
        console.error("Patient signup error:", error);
        alert("Unable to register. Please try again.");
    }
};

// Patient login handler
window.loginPatient = async function () {
    // Collect patient login credentials
    const email = document.getElementById("patientLoginEmail").value;
    const password = document.getElementById("patientLoginPassword").value;

    const data = {
        email,
        password
    };

    try {
        // Authenticate patient
        const response = await patientLogin(data);

        if (response && response.ok) {
            const result = await response.json();

            // Store authentication token
            localStorage.setItem("token", result.token);
            localStorage.setItem("userRole", "loggedPatient");

            // Redirect to logged-in patient dashboard
            window.location.href = "/pages/loggedPatientDashboard.html";

        } else {
            alert("Invalid email or password!");
        }

    } catch (error) {
        console.error("Patient login error:", error);
        alert("Unable to login. Please try again.");
    }
};

// Initialize dashboard functionality
document.addEventListener("DOMContentLoaded", () => {

    // Load doctors when the page opens
    loadDoctorCards();

    // Signup modal trigger
    const signupBtn = document.getElementById("patientSignup");

    if (signupBtn) {
        signupBtn.addEventListener("click", () => {
            openModal("patientSignup");
        });
    }

    // Login modal trigger
    const loginBtn = document.getElementById("patientLogin");

    if (loginBtn) {
        loginBtn.addEventListener("click", () => {
            openModal("patientLogin");
        });
    }

    // Search and filter listeners
    const searchBar = document.getElementById("searchBar");
    const filterTime = document.getElementById("filterTime");
    const filterSpecialty = document.getElementById("filterSpecialty");

    if (searchBar) {
        searchBar.addEventListener("input", filterDoctorsOnChange);
    }

    if (filterTime) {
        filterTime.addEventListener("change", filterDoctorsOnChange);
    }

    if (filterSpecialty) {
        filterSpecialty.addEventListener("change", filterDoctorsOnChange);
    }
});
