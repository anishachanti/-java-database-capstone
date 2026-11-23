package com.project.back_end.services;

import com.project.back_end.models.Patient;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.dto.AppointmentDTO;
import com.project.back_end.models.Appointment;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public PatientService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService) {

        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {
        Map<String, Object> response = new HashMap<>();
    
        if (!tokenService.validateToken(token, "patient")) {
            response.put("message", "Invalid or expired token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String email = tokenService.extractIdentifier(token);
        Patient patient = patientRepository.findByEmail(email);
    
        if (patient == null || !patient.getId().equals(id)) {
            response.put("message", "Unauthorized access");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    
        List<Appointment> appointments = appointmentRepository.findByPatientId(id);
    
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(a -> new AppointmentDTO(
                        a.getId(),
                        a.getDoctor().getId(),
                        a.getDoctor().getName(),
                        a.getPatient().getId(),
                        a.getPatient().getName(),
                        a.getPatient().getEmail(),
                        a.getPatient().getPhone(),
                        a.getPatient().getAddress(),
                        a.getAppointmentTime(),
                        a.getStatus()))
                .collect(Collectors.toList());
    
        response.put("appointments", appointmentDTOs);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long id) {
        Map<String, Object> response = new HashMap<>();
    
        int status;
    
        if ("past".equalsIgnoreCase(condition)) {
            status = 1;
        } else if ("future".equalsIgnoreCase(condition)) {
            status = 0;
        } else {
            response.put("message", "Invalid condition. Use past or future.");
            return ResponseEntity.badRequest().body(response);
        }
    
        List<Appointment> appointments =
                appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(id, status);
    
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(a -> new AppointmentDTO(
                        a.getId(),
                        a.getDoctor().getId(),
                        a.getDoctor().getName(),
                        a.getPatient().getId(),
                        a.getPatient().getName(),
                        a.getPatient().getEmail(),
                        a.getPatient().getPhone(),
                        a.getPatient().getAddress(),
                        a.getAppointmentTime(),
                        a.getStatus()))
                .collect(Collectors.toList());
    
        response.put("appointments", appointmentDTOs);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long patientId) {
        Map<String, Object> response = new HashMap<>();
    
        List<Appointment> appointments =
                appointmentRepository.filterByDoctorNameAndPatientId(name, patientId);
    
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(a -> new AppointmentDTO(
                        a.getId(),
                        a.getDoctor().getId(),
                        a.getDoctor().getName(),
                        a.getPatient().getId(),
                        a.getPatient().getName(),
                        a.getPatient().getEmail(),
                        a.getPatient().getPhone(),
                        a.getPatient().getAddress(),
                        a.getAppointmentTime(),
                        a.getStatus()))
                .collect(Collectors.toList());
    
        response.put("appointments", appointmentDTOs);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(
        String condition, String name, long patientId) {

        Map<String, Object> response = new HashMap<>();

        int status;

        if ("past".equalsIgnoreCase(condition)) {
            status = 1;
        } else if ("future".equalsIgnoreCase(condition)) {
            status = 0;
        } else {
            response.put("message", "Invalid condition. Use past or future.");
            return ResponseEntity.badRequest().body(response);
        }

        List<Appointment> appointments =
                appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(
                        name, patientId, status);

        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(a -> new AppointmentDTO(
                        a.getId(),
                        a.getDoctor().getId(),
                        a.getDoctor().getName(),
                        a.getPatient().getId(),
                        a.getPatient().getName(),
                        a.getPatient().getEmail(),
                        a.getPatient().getPhone(),
                        a.getPatient().getAddress(),
                        a.getAppointmentTime(),
                        a.getStatus()))
                .collect(Collectors.toList());

        response.put("appointments", appointmentDTOs);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
        Map<String, Object> response = new HashMap<>();
    
        if (!tokenService.validateToken(token, "patient")) {
            response.put("message", "Invalid or expired token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String email = tokenService.extractIdentifier(token);
        Patient patient = patientRepository.findByEmail(email);
    
        if (patient == null) {
            response.put("message", "Patient not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    
        response.put("patient", patient);
        return ResponseEntity.ok(response);
    }
}