package com.project.back_end.services;

import com.project.back_end.models.*;
import com.project.back_end.repositories.AdminRepository;
import com.project.back_end.repositories.DoctorRepository;
import com.project.back_end.repositories.PatientRepository;
import com.project.back_end.dto.Login;

import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@org.springframework.stereotype.Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Service(
            TokenService tokenService,
            AdminRepository adminRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            DoctorService doctorService,
            PatientService patientService) {

        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        Map<String, String> response = new HashMap<>();

        Map<String, Object> tokenData = tokenService.validateToken(token, user);

        if (tokenData == null) {
            response.put("message", "Invalid or expired token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> response = new HashMap<>();

        Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());

        if (admin == null || !admin.getPassword().equals(receivedAdmin.getPassword())) {
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = tokenService.generateToken(admin.getUsername(), "admin");

        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    public Map<String, Object> filterDoctor(String name, String specialty, String time) {

        if (name != null && !name.isBlank()
                && specialty != null && !specialty.isBlank()
                && time != null && !time.isBlank()) {

            return doctorService.filterDoctorsByNameSpecilityandTime(
                    name, specialty, time);

        } else if (name != null && !name.isBlank()
                && specialty != null && !specialty.isBlank()) {

            return doctorService.filterDoctorByNameAndSpecility(name, specialty);

        } else if (name != null && !name.isBlank()
                && time != null && !time.isBlank()) {

            return doctorService.filterDoctorByNameAndTime(name, time);

        } else if (specialty != null && !specialty.isBlank()
                && time != null && !time.isBlank()) {

            return doctorService.filterDoctorByTimeAndSpecility(specialty, time);

        } else if (name != null && !name.isBlank()) {

            return doctorService.findDoctorByName(name);

        } else if (specialty != null && !specialty.isBlank()) {

            return doctorService.filterDoctorBySpecility(specialty);

        } else if (time != null && !time.isBlank()) {

            return doctorService.filterDoctorsByTime(time);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctorRepository.findAll());
        return response;
    }


    public int validateAppointment(Appointment appointment) {

        Long doctorId = appointment.getDoctor().getId();

        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

        if (doctor == null) {
            return -1;
        }

        LocalDate date = appointment.getAppointmentTime().toLocalDate();

        List<String> availableSlots =
                doctorService.getDoctorAvailability(doctorId, date);

        String requestedTime =
                appointment.getAppointmentTime().toLocalTime().toString();

        if (availableSlots.contains(requestedTime)) {
            return 1;
        }

        return 0;
    }


    public boolean validatePatient(Patient patient) {
        Patient existingPatient = patientRepository.findByEmailOrPhone(
                patient.getEmail(),
                patient.getPhone()
        );

        return existingPatient == null;
    }


    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Map<String, String> response = new HashMap<>();
    
        Patient patient = patientRepository.findByEmail(login.getIdentifier());
    
        if (patient == null || !patient.getPassword().equals(login.getPassword())) {
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    
        String token = tokenService.generateToken(patient.getEmail(), "patient");
    
        response.put("token", token);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<Map<String, Object>> filterPatient(
        String condition, String name, String token) {

        Map<String, Object> response = new HashMap<>();

        Map<String, Object> tokenData =
                tokenService.validateToken(token, "patient");

        if (tokenData == null || !tokenData.containsKey("email")) {
            response.put("message", "Invalid or expired token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String email = tokenData.get("email").toString();
        Patient patient = patientRepository.findByEmail(email);

        if (patient == null) {
            response.put("message", "Patient not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Long patientId = patient.getId();

        if (condition != null && !condition.isBlank()
                && name != null && !name.isBlank()) {

            return patientService.filterByDoctorAndCondition(
                    condition, name, patientId);

        } else if (condition != null && !condition.isBlank()) {

            return patientService.filterByCondition(condition, patientId);

        } else if (name != null && !name.isBlank()) {

            return patientService.filterByDoctor(name, patientId);
        }

        return patientService.getPatientAppointment(patientId, token);
    }









}