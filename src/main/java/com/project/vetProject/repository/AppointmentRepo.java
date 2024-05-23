package com.project.vetProject.repository;

import com.project.vetProject.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Integer> {

    // Randevuları tarih ve saatine göre bulur
    List<Appointment> findByDateTime(LocalDateTime localDateTime);

    // Randevuları doktor kimliği ve tarih aralığına göre bulur
    List<Appointment> findByDoctorIdAndDateTimeBetween(int id, LocalDateTime entryDate, LocalDateTime exitDate);

    // Randevuları hayvan kimliği ve tarih aralığına göre bulur
    List<Appointment> findByAnimalIdAndDateTimeBetween(int id, LocalDateTime entryDate, LocalDateTime exitDate);

    // Randevuyu tarih, doktor kimliği ve hayvan kimliğine göre bulur
    Optional<Appointment> findByDateTimeAndDoctorIdAndAnimalId(LocalDateTime dateTime, Integer doctorId, Integer animalId);
}
