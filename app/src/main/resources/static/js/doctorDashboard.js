
import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";

// Initialize global variables
const patientTableBody = document.getElementById("patientTableBody");

let selectedDate = new Date().toISOString().split("T")[0];
let token = localStorage.getItem("token");
let patientName = null;

// Load appointments from the backend
async function loadAppointments() {
    try {
        // Fetch appointments using date, patient name, and token
        const appointments = await getAllAppointments(
            selectedDate,
            patientName,
            token
        );

        // Clear existing table rows
        patientTableBody.innerHTML = "";

        // Display message if no appointments are available
        if (!appointments || appointments.length === 0) {
            patientTableBody.innerHTML = `
                <tr>
                    <td colspan="5" class="noPatientRecord">
                        No Appointments found for today
                    </td>
                </tr>
            `;
            return;
        }

        // Create and append a row for each appointment
        appointments.forEach(appointment => {
            const row = createPatientRow(appointment);
            patientTableBody.appendChild(row);
        });

    } catch (error) {
        console.error("Error loading appointments:", error);

        // Display fallback error message
        patientTableBody.innerHTML = `
            <tr>
                <td colspan="5" class="noPatientRecord">
                    Unable to load appointments. Please try again.
                </td>
            </tr>
        `;
    }
}

// Initialize dashboard event listeners
document.addEventListener("DOMContentLoaded", () => {

    // Search patients by name
    const searchBar = document.getElementById("searchBar");

    if (searchBar) {
        searchBar.addEventListener("input", () => {
            patientName = searchBar.value.trim() || "null";
            loadAppointments();
        });
    }

    // Today's appointments button
    const todayButton = document.getElementById("todayButton");

    if (todayButton) {
        todayButton.addEventListener("click", () => {
            selectedDate = new Date().toISOString().split("T")[0];

            const datePicker = document.getElementById("datePicker");

            if (datePicker) {
                datePicker.value = selectedDate;
            }

            loadAppointments();
        });
    }

    // Filter appointments by selected date
    const datePicker = document.getElementById("datePicker");

    if (datePicker) {
        datePicker.addEventListener("change", () => {
            selectedDate = datePicker.value;
            loadAppointments();
        });
    }

    // Load today's appointments initially
    loadAppointments();
});
