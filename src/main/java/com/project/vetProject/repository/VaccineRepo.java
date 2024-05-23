package com.project.vetProject.repository;

import com.project.vetProject.entities.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccineRepo extends JpaRepository<Vaccine, Integer> {

    // Hayvan kimliğine göre aşıları bulur
    List<Vaccine> findByAnimalId(int id);

    // Belirli bir tarih aralığındaki aşıları bulur
    List<Vaccine> findByprotectionFnshDateBetween(LocalDate entryDate, LocalDate exitDate);

    // Kod ve isime göre aşıları bulur
    List<Vaccine> findByCodeAndName(String code, String name);
}
