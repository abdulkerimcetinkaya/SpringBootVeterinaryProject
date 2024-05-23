package com.project.vetProject.repository;

import com.project.vetProject.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Integer> {

    // Doktorları kimlik ve uygun tarihine göre bulur
    List<Doctor> findByIdAndAvailableDateDate(int id, LocalDate localDate);

    // Doktorları isim, e-posta ve telefon numarasına göre bulur
    List<Doctor> findByNameAndMailAndPhone(String name, String mail, String phone);
}
