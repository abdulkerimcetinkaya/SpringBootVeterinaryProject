package com.project.vetProject.repository;

import com.project.vetProject.entities.AvailableDate;
import com.project.vetProject.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailableDateRepo extends JpaRepository<AvailableDate, Integer> {

    // Belirli bir tarih ve doktora göre uygun tarihleri bulur
    List<AvailableDate> findByDateAndDoctor(LocalDate availableDate, Doctor doctor);
}
