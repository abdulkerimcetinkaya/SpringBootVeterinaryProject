package com.project.vetProject.service.abstracts;

import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.vaccine.VaccineSaveRequest;
import com.project.vetProject.dto.request.vaccine.VaccineUpdateRequest;
import com.project.vetProject.dto.response.vaccine.VaccineResponse;
import com.project.vetProject.entities.Vaccine;

import java.time.LocalDate;
import java.util.List;

public interface IVaccineService {

    // Yeni bir aşı kaydeder
    ResultData<VaccineResponse> save(VaccineSaveRequest vaccineSaveRequest);

    // Belirli bir kimliğe sahip aşıyı getirir
    Vaccine get(int id);

    // Sayfalama kullanarak aşıları getirir
    ResultData<CursorResponse<VaccineResponse>> cursor(int page, int pageSize);

    // Hayvan kimliğine göre aşıları bulur
    ResultData<List<VaccineResponse>> findByAnimalId(int id);

    // Belirli bir tarih aralığındaki aşıları bulur
    ResultData<List<VaccineResponse>> findByDate(LocalDate entryDate, LocalDate exitDate);

    // Kod ve isime göre aşıları bulur
    List<Vaccine> findByCodeAndName(String code, String name);

    // Aşıyı günceller
    ResultData<VaccineResponse> update(VaccineUpdateRequest vaccineUpdateRequest);

    // Belirli bir kimliğe sahip aşıyı siler
    boolean delete(int id);
}
