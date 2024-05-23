package com.project.vetProject.service.abstracts;

import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.availableDate.AvailableDateSaveRequest;
import com.project.vetProject.dto.request.availableDate.AvailableDateUpdateRequest;
import com.project.vetProject.dto.response.availableDate.AvailableDateResponse;
import com.project.vetProject.entities.AvailableDate;

public interface IAvailableDateService {

    // Yeni bir uygun tarih kaydeder
    ResultData<AvailableDateResponse> save(AvailableDateSaveRequest availableDateSaveRequest);

    // Belirli bir kimliğe sahip uygun tarihi getirir
    AvailableDate get(int id);

    // Sayfalama kullanarak uygun tarihleri getirir
    ResultData<CursorResponse<AvailableDateResponse>> cursor(int page, int pageSize);

    // Uygun tarihi günceller
    ResultData<AvailableDateResponse> update(AvailableDateUpdateRequest availableDateUpdateRequest);

    // Belirli bir kimliğe sahip uygun tarihi siler
    boolean delete(int id);
}
