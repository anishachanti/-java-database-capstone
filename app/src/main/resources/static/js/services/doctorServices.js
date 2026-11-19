
import { API_BASE_URL } from "../config/config.js";

// Base Doctor API endpoint
const DOCTOR_API = API_BASE_URL + "/doctor";

// Get all doctors
export async function getDoctors() {
    try {
        // Send GET request to fetch all doctors
        const response = await fetch(DOCTOR_API);

        if (!response.ok) {
            throw new Error("Failed to fetch doctors");
        }

        // Extract doctor list from response
        const doctors = await response.json();
        return doctors;

    } catch (error) {
        console.error("Error fetching doctors:", error);
        return [];
    }
}

// Delete a doctor
export async function deleteDoctor(id, token) {
    try {
        // Send DELETE request with doctor ID and authentication token
        const response = await fetch(`${DOCTOR_API}/${id}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        const data = await response.json();

        return {
            success: response.ok,
            message: data.message || (response.ok
                ? "Doctor deleted successfully"
                : "Failed to delete doctor")
        };

    } catch (error) {
        console.error("Error deleting doctor:", error);

        return {
            success: false,
            message: "An error occurred while deleting the doctor"
        };
    }
}

// Save (add) a new doctor
export async function saveDoctor(doctor, token) {
    try {
        // Send POST request with doctor details
        const response = await fetch(DOCTOR_API, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(doctor)
        });

        const data = await response.json();

        return {
            success: response.ok,
            message: data.message || (response.ok
                ? "Doctor saved successfully"
                : "Failed to save doctor")
        };

    } catch (error) {
        console.error("Error saving doctor:", error);

        return {
            success: false,
            message: "An error occurred while saving the doctor"
        };
    }
}

// Filter doctors by name, availability time, and specialty
export async function filterDoctors(name, time, specialty) {
    try {
        // Construct URL with filter parameters
        const params = new URLSearchParams();

        if (name) params.append("name", name);
        if (time) params.append("time", time);
        if (specialty) params.append("specialty", specialty);

        const url = `${DOCTOR_API}?${params.toString()}`;

        // Send GET request with filter parameters
        const response = await fetch(url);

        if (!response.ok) {
            throw new Error("Failed to filter doctors");
        }

        // Return filtered doctor list
        return await response.json();

    } catch (error) {
        console.error("Error filtering doctors:", error);
        alert("Unable to filter doctors. Please try again.");
        return [];
    }
}
