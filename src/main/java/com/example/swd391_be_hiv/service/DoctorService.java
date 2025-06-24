package com.example.swd391_be_hiv.service;

import com.example.swd391_be_hiv.entity.Doctor;
import com.example.swd391_be_hiv.exception.DuplicateEntity;
import com.example.swd391_be_hiv.exception.NotFoundException;
import com.example.swd391_be_hiv.model.request.DoctorRequest;
import com.example.swd391_be_hiv.repository.DoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {



    @Autowired
    ModelMapper modelMapper;

    @Autowired
    DoctorRepository doctorRepository;

    public Doctor createNewDoctor(DoctorRequest doctorRequest) {

        if (doctorRepository.findByCustomerIdAndDeletedFalse(doctorRequest.getCustomerId()).isPresent()) {
            throw new DuplicateEntity("Doctor with this customer ID already exists");
        }

        Doctor doctor = modelMapper.map(doctorRequest, Doctor.class);

        try {
            Doctor newDoctor = doctorRepository.save(doctor);
            return newDoctor;
        } catch (Exception e) {
            throw new DuplicateEntity("Failed to create doctor: " + e.getMessage());
        }
    }
    public List<Doctor> getAllDoctor() {
        List<Doctor> doctors = doctorRepository.findByDeletedFalse();
        return doctors;
    }
    public Doctor getDoctorId(Long customerId) {
        return doctorRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Doctor not found"));

    }

    public Doctor updateDoctor(Doctor doctor, Long doctorId) {
        Doctor oldDoctor = doctorRepository.findByIdAndDeletedFalse(doctorId)
                .orElseThrow(() -> new NotFoundException("Doctor not found with ID: " + doctorId));


        oldDoctor.setCustomerId(doctor.getCustomerId());
        oldDoctor.setQualifications(doctor.getQualifications());
        oldDoctor.setSpecialization(doctor.getSpecialization());
        oldDoctor.setWorkSchedule(doctor.getWorkSchedule());
        oldDoctor.setName(doctor.getName());



        return doctorRepository.save(oldDoctor);
    }
    public Doctor deleteDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findByIdAndDeletedFalse(doctorId)
                .orElseThrow(() -> new NotFoundException("Doctor not found with ID: " + doctorId));

        doctor.setDeleted(true);
        return doctorRepository.save(doctor);
    }
}

