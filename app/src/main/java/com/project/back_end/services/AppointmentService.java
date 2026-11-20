
package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.repo.DoctorRepository;

import org.springframework.stereotype.Service;

import com.project.back_end.services.TokenService;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            TokenService tokenService) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public ResponseEntity<Map<String, String>> updateAppointment(
        Appointment appointment) {

        Map<String, String> response = new HashMap<>();

        Optional<Appointment> existingAppointment =
                appointmentRepository.findById(appointment.getId());

        if (existingAppointment.isEmpty()) {
            response.put("message", "Appointment not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (appointment.getDoctor() == null ||
                doctorRepository.findById(appointment.getDoctor().getId()).isEmpty()) {
            response.put("message", "Invalid doctor ID");
            return ResponseEntity.badRequest().body(response);
        }

        if (appointment.getPatient() == null ||
                patientRepository.findById(appointment.getPatient().getId()).isEmpty()) {
            response.put("message", "Invalid patient ID");
            return ResponseEntity.badRequest().body(response);
        }

        Appointment existing = existingAppointment.get();

        existing.setDoctor(appointment.getDoctor());
        existing.setPatient(appointment.getPatient());
        existing.setAppointmentTime(appointment.getAppointmentTime());
        existing.setStatus(appointment.getStatus());

        appointmentRepository.save(existing);

        response.put("message", "Appointment updated successfully");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> cancelAppointment(
        long id, String token) {

        Map<String, String> response = new HashMap<>();

        Optional<Appointment> appointmentOptional =
                appointmentRepository.findById(id);

        if (appointmentOptional.isEmpty()) {
            response.put("message", "Appointment not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Appointment appointment = appointmentOptional.get();

        // Verify that the patient owns this appointment
        Map<String, Object> tokenData = tokenService.validateToken(token, "patient");

        if (tokenData == null || !tokenData.containsKey("id")) {
            response.put("message", "Invalid or expired token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Long patientId = Long.valueOf(tokenData.get("id").toString());

        if (!appointment.getPatient().getId().equals(patientId)) {
            response.put("message", "You are not authorized to cancel this appointment");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        appointmentRepository.delete(appointment);

        response.put("message", "Appointment cancelled successfully");
        return ResponseEntity.ok(response);
    }



public Map<String, Object> getAppointment(
        String pname, LocalDate date, String token) {

        Map<String, Object> response = new HashMap<>();

        Map<String, Object> tokenData =
                tokenService.validateToken(token, "doctor");

        if (tokenData == null || !tokenData.containsKey("id")) {
            response.put("message", "Invalid or expired token");
            return response;
        }

        Long doctorId = Long.valueOf(tokenData.get("id").toString());

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Appointment> appointments =
                appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                        doctorId, start, end);

        if (pname != null && !pname.trim().isEmpty()) {
            appointments = appointments.stream()
                    .filter(a -> a.getPatient().getName()
                            .toLowerCase()
                            .contains(pname.toLowerCase()))
                    .collect(Collectors.toList());
        }

        response.put("appointments", appointments);
        return response;
    }

}
