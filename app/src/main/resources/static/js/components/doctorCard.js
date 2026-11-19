
import { deleteDoctor } from "../services/doctorServices.js";
import { getPatientData } from "../services/patientServices.js";
import { showBookingOverlay } from "./modals.js";

export function createDoctorCard(doctor) {

    // Create the main card container
    const card = document.createElement("div");
    card.classList.add("doctor-card");

    // Get the user's role
    const role = localStorage.getItem("userRole");

    // Create doctor information section
    const infoDiv = document.createElement("div");
    infoDiv.classList.add("doctor-info");

    const name = document.createElement("h3");
    name.textContent = doctor.name;

    const specialization = document.createElement("p");
    specialization.textContent =
        `Specialization: ${doctor.specialization}`;

    const email = document.createElement("p");
    email.textContent = `Email: ${doctor.email}`;

    const availability = document.createElement("p");
    availability.textContent =
        `Availability: ${
            Array.isArray(doctor.availability)
                ? doctor.availability.join(", ")
                : doctor.availability
        }`;

    // Append doctor details
    infoDiv.appendChild(name);
    infoDiv.appendChild(specialization);
    infoDiv.appendChild(email);
    infoDiv.appendChild(availability);

    // Create button container
    const actionsDiv = document.createElement("div");
    actionsDiv.classList.add("card-actions");

    // Admin: Show Delete button
    if (role === "admin") {

        const removeBtn = document.createElement("button");
        removeBtn.textContent = "Delete";
        removeBtn.classList.add("delete-btn");

        removeBtn.addEventListener("click", async () => {

            const confirmed = confirm(
                "Are you sure you want to delete this doctor?"
            );

            if (!confirmed) {
                return;
            }

            try {
                const token = localStorage.getItem("token");

                await deleteDoctor(doctor.id, token);

                // Remove card after successful deletion
                card.remove();

                alert("Doctor deleted successfully.");

            } catch (error) {
                console.error("Error deleting doctor:", error);
                alert("Failed to delete doctor.");
            }
        });

        actionsDiv.appendChild(removeBtn);
    }

    // Patient: Login required before booking
    else if (role === "patient") {

        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";
        bookNow.classList.add("book-btn");

        bookNow.addEventListener("click", () => {
            alert("Patient needs to login first.");
        });

        actionsDiv.appendChild(bookNow);
    }

    // Logged-in Patient: Allow booking
    else if (role === "loggedPatient") {

        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";
        bookNow.classList.add("book-btn");

        bookNow.addEventListener("click", async (e) => {

            try {
                const token = localStorage.getItem("token");

                const patientData = await getPatientData(token);

                showBookingOverlay(e, doctor, patientData);

            } catch (error) {
                console.error("Error preparing booking:", error);
                alert("Unable to load patient details.");
            }
        });

        actionsDiv.appendChild(bookNow);
    }

    // Final assembly
    card.appendChild(infoDiv);
    card.appendChild(actionsDiv);

    return card;
}
