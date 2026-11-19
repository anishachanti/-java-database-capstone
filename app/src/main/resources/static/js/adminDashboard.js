
import { openModal } from "../components/modals.js";

import {
    getDoctors,
    filterDoctors,
    saveDoctor
} from "./services/doctorServices.js";

import { createDoctorCard } from "../components/doctorCard.js";

// Get the dashboard content container
const contentDiv = document.getElementById("content");

// Load all doctors when the page loads
document.addEventListener("DOMContentLoaded", () => {

    // Add Doctor button
    const addDocBtn = document.getElementById("addDocBtn");

    if (addDocBtn) {
        addDocBtn.addEventListener("click", () => {
            openModal("addDoctor");
        });
    }

    // Search and filter event listeners
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

    // Load doctors initially
    loadDoctorCards();
});

// Fetch and render all doctors
async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();
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
        contentDiv.innerHTML = "<p>No doctors found</p>";
        return;
    }

    doctors.forEach(doctor => {
        const card = createDoctorCard(doctor);
        contentDiv.appendChild(card);
    });
}

// Search and filter doctors
async function filterDoctorsOnChange() {
    const name = document.getElementById("searchBar").value;
    const time = document.getElementById("filterTime").value;
    const specialty = document.getElementById("filterSpecialty").value;

    try {
        const doctors = await filterDoctors(name, time, specialty);
        renderDoctorCards(doctors);

    } catch (error) {
        console.error("Error filtering doctors:", error);
        contentDiv.innerHTML = "<p>No doctors found</p>";
    }
}

// Handle Add Doctor form submission
window.adminAddDoctor = async function () {
    const name = document.getElementById("doctorName").value;
    const specialty = document.getElementById("doctorSpecialty").value;
    const email = document.getElementById("doctorEmail").value;
    const password = document.getElementById("doctorPassword").value;
    const mobileNo = document.getElementById("doctorMobile").value;
    const availabilityTime = document.getElementById("availabilityTime").value;

    // Collect doctor availability checkbox values
    const availability = Array.from(
        document.querySelectorAll('input[name="availability"]:checked')
    ).map(input => input.value);

    // Prepare doctor data
    const doctor = {
        name,
        specialty,
        email,
        password,
        mobileNo,
        availabilityTime,
        availability
    };

    // Retrieve admin authentication token
    const token = localStorage.getItem("token");

    if (!token) {
        alert("Admin authentication required. Please login again.");
        return;
    }

    try {
        // Save doctor through the service module
        const result = await saveDoctor(doctor, token);

        if (result.success) {
            alert(result.message);

            // Close modal and refresh doctor list
            document.getElementById("modal").style.display = "none";
            await loadDoctorCards();

        } else {
            alert(result.message);
        }

    } catch (error) {
        console.error("Error adding doctor:", error);
        alert("Unable to add doctor. Please try again.");
    }
};
