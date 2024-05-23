package com.project.vetProject.service.abstracts;

import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.appointment.AppointmentSaveRequest;
import com.project.vetProject.dto.request.appointment.AppointmentUpdateRequest;
import com.project.vetProject.dto.response.appointment.AppointmentResponse;
import com.project.vetProject.entities.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IAppointmentService {

    // Yeni bir randevu kaydeder
    ResultData<AppointmentResponse> save(AppointmentSaveRequest appointmentSaveRequest);

    // Belirli bir kimliğe sahip randevuyu getirir
    Appointment get(int id);

    // Sayfalama kullanarak randevuları getirir
    ResultData<CursorResponse<AppointmentResponse>> cursor(int page, int pageSize);

    // Randevuyu günceller
    ResultData<AppointmentResponse> update(AppointmentUpdateRequest appointmentUpdateRequest);

    // Belirli bir kimliğe sahip randevuyu siler
    boolean delete(int id);

    // Belirli bir tarih ve saatteki randevuları bulur
    List<Appointment> findByDateTime(LocalDateTime localDateTime);

    // Doktor kimliği ve tarih aralığına göre randevuları bulur
    ResultData<List<AppointmentResponse>> findByDoctorIdAndDateTimeBetween(int id, LocalDate entryDate, LocalDate exitDate);

    // Hayvan kimliği ve tarih aralığına göre randevuları bulur
    ResultData<List<AppointmentResponse>> findByAnimalIdAndDateTimeBetween(int id, LocalDate entryDate, LocalDate exitDate);

    // Belirli bir tarih, doktor kimliği ve hayvan kimliğine göre randevuyu bulur
    Optional<Appointment> findByValueForValid(LocalDateTime dateTime, Integer doctorId, Integer animalId);
}
