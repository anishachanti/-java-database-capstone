
import { API_BASE_URL } from "../config/config.js";

// Base Patient API endpoint
const PATIENT_API = API_BASE_URL + "/patient";

// Patient signup
export async function patientSignup(data) {
    try {
        // Send patient details to the signup endpoint
        const response = await fetch(`${PATIENT_API}/signup`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();

        return {
            success: response.ok,
            message: result.message || (response.ok
                ? "Signup successful"
                : "Signup failed")
        };

    } catch (error) {
        console.error("Patient signup error:", error);

        return {
            success: false,
            message: "An error occurred during signup"
        };
    }
}

// Patient login
export async function patientLogin(data) {
    try {
        // Send login credentials to the login endpoint
        const response = await fetch(`${PATIENT_API}/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        // Return the full response for frontend processing
        return response;

    } catch (error) {
        console.error("Patient login error:", error);
        return null;
    }
}

// Fetch logged-in patient details
export async function getPatientData(token) {
    try {
        // Fetch patient details using the authentication token
        const response = await fetch(`${PATIENT_API}/profile`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error("Failed to fetch patient data");
        }

        return await response.json();

    } catch (error) {
        console.error("Error fetching patient data:", error);
        return null;
    }
}

// Fetch patient appointments
export async function getPatientAppointments(id, token, user) {
    try {
        // Construct the appointment URL based on the requesting user
        const params = new URLSearchParams({ user: user });

        const response = await fetch(
            `${PATIENT_API}/appointments/${id}?${params.toString()}`,
            {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        if (!response.ok) {
            throw new Error("Failed to fetch appointments");
        }

        return await response.json();

    } catch (error) {
        console.error("Error fetching appointments:", error);
        return null;
    }
}

// Filter appointments
export async function filterAppointments(condition, name, token) {
    try {
        // Construct query parameters for appointment filtering
        const params = new URLSearchParams();

        if (condition) params.append("condition", condition);
        if (name) params.append("name", name);

        const response = await fetch(
            `${PATIENT_API}/appointments/filter?${params.toString()}`,
            {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        if (!response.ok) {
            throw new Error("Failed to filter appointments");
        }

        return await response.json();

    } catch (error) {
        console.error("Error filtering appointments:", error);
        alert("Unable to filter appointments. Please try again.");
        return [];
    }
}
