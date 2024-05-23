package com.project.vetProject.service.abstracts;

import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.doctor.DoctorSaveRequest;
import com.project.vetProject.dto.request.doctor.DoctorUpdateRequest;
import com.project.vetProject.dto.response.doctor.DoctorResponse;
import com.project.vetProject.entities.Doctor;

import java.time.LocalDate;
import java.util.List;

public interface IDoctorService {

    // Yeni bir doktor kaydeder
    ResultData<DoctorResponse> save(DoctorSaveRequest doctorSaveRequest);

    // Belirli bir kimliğe sahip doktoru getirir
    Doctor get(int id);

    // Sayfalama kullanarak doktorları getirir
    ResultData<CursorResponse<DoctorResponse>> cursor(int page, int pageSize);

    // Doktoru günceller
    ResultData<DoctorResponse> update(DoctorUpdateRequest doctorUpdateRequest);

    // Belirli bir kimliğe sahip doktoru siler
    boolean delete(int id);

    // Kimlik ve uygun tarihine göre doktorları bulur
    List<Doctor> findByIdAndAvailableDateDate(int id, LocalDate localDate);

    // İsim, e-posta ve telefon numarasına göre doktorları bulur
    List<Doctor> findByNameAndMailAndPhone(String name, String mail, String phone);
}
