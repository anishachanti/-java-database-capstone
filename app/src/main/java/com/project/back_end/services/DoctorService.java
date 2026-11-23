package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.AppointmentRepository;

import org.springframework.stereotype.Service;

import com.project.back_end.models.Appointment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.project.back_end.dto.Login;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService) {

        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

        if (doctor == null) {
            return new ArrayList<>();
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Appointment> appointments =
                appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                        doctorId, start, end);

        List<String> availableSlots = new ArrayList<>(doctor.getAvailableTimes());

        for (Appointment appointment : appointments) {
            String bookedSlot = appointment.getAppointmentTimeOnly().toString();
            availableSlots.remove(bookedSlot);
        }

        return availableSlots;
    }

    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    public int saveDoctor(Doctor doctor) {
        try {
            Doctor existingDoctor = doctorRepository.findByEmail(doctor.getEmail());
    
            if (existingDoctor != null) {
                return 0;
            }
    
            doctorRepository.save(doctor);
            return 1;
    
        } catch (Exception e) {
            return -1;
        }
    }

    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {

        Map<String, String> response = new HashMap<>();
    
        Doctor doctor = doctorRepository.findByEmail(login.getIdentifier());
    
        if (doctor == null || !doctor.getPassword().equals(login.getPassword())) {
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    
        String token = tokenService.generateToken(doctor.getEmail());
    
        response.put("message", "Login successful");
        response.put("token", token);
    
        return ResponseEntity.ok(response);
    }


    public int updateDoctor(Doctor doctor) {
        try {
            Doctor existingDoctor = doctorRepository.findById(doctor.getId()).orElse(null);
    
            if (existingDoctor == null) {
                return 0;
            }
    
            existingDoctor.setName(doctor.getName());
            existingDoctor.setSpecialty(doctor.getSpecialty());
            existingDoctor.setEmail(doctor.getEmail());
            existingDoctor.setPassword(doctor.getPassword());
            existingDoctor.setPhone(doctor.getPhone());
            existingDoctor.setAvailableTimes(doctor.getAvailableTimes());
    
            doctorRepository.save(existingDoctor);
            return 1;
    
        } catch (Exception e) {
            return -1;
        }
    }


    public int deleteDoctor(Long id) {
        try {
            if (!doctorRepository.existsById(id)) {
                return 0;
            }
    
            doctorRepository.deleteById(id);
            return 1;
    
        } catch (Exception e) {
            return -1;
        }
    }


    public List<Doctor> filterDoctorsByNameSpecilityandTime(
        String name, String speciality, String time) {

        return doctorRepository.findAll().stream()
                .filter(doctor -> doctor.getName() != null &&
                        doctor.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(doctor -> doctor.getSpecialty() != null &&
                        doctor.getSpecialty().equalsIgnoreCase(speciality))
                .filter(doctor -> doctor.getAvailableTimes() != null &&
                        doctor.getAvailableTimes().contains(time))
                .toList();
    }


    public List<Doctor> filterDoctorByNameAndSpecility(
        String name, String speciality) {

        return doctorRepository.findAll().stream()
                .filter(doctor -> doctor.getName() != null &&
                        doctor.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(doctor -> doctor.getSpecialty() != null &&
                        doctor.getSpecialty().equalsIgnoreCase(speciality))
                .toList();
    }


    public List<Doctor> filterDoctorByNameAndTime(String name, String time) {

        return doctorRepository.findAll().stream()
                .filter(doctor -> doctor.getName() != null &&
                        doctor.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(doctor -> doctor.getAvailableTimes() != null &&
                        doctor.getAvailableTimes().contains(time))
                .toList();
    }


    public List<Doctor> filterDoctorByTimeAndSpecility(
        String time, String speciality) {

        return doctorRepository.findAll().stream()
                .filter(doctor -> doctor.getAvailableTimes() != null &&
                        doctor.getAvailableTimes().contains(time))
                .filter(doctor -> doctor.getSpecialty() != null &&
                        doctor.getSpecialty().equalsIgnoreCase(speciality))
                .toList();
    }

    public List<Doctor> findDoctorByName(String name) {

        return doctorRepository.findAll().stream()
                .filter(doctor -> doctor.getName() != null &&
                        doctor.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public List<Doctor> filterDoctorBySpecility(String speciality) {

        return doctorRepository.findAll().stream()
                .filter(doctor -> doctor.getSpecialty() != null &&
                        doctor.getSpecialty().equalsIgnoreCase(speciality))
                .toList();
    }


    public List<Doctor> filterDoctorsByTime(String time) {

        return doctorRepository.findAll().stream()
                .filter(doctor -> doctor.getAvailableTimes() != null &&
                        doctor.getAvailableTimes().contains(time))
                .toList();
    }
}