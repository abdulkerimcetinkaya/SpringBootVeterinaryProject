package com.project.vetProject.controller;

import com.project.vetProject.service.abstracts.IDoctorService;
import com.project.vetProject.core.result.Result;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.doctor.DoctorSaveRequest;
import com.project.vetProject.dto.request.doctor.DoctorUpdateRequest;
import com.project.vetProject.dto.response.doctor.DoctorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final IDoctorService doctorService;

    // Yeni bir doktor kaydetmek için endpoint
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<DoctorResponse> save(@Valid @RequestBody DoctorSaveRequest doctorSaveRequest){
        return this.doctorService.save(doctorSaveRequest);
    }

    // Bir doktoru güncellemek için endpoint
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<DoctorResponse> update(@Valid @RequestBody DoctorUpdateRequest doctorUpdateRequest){
        return this.doctorService.update(doctorUpdateRequest);
    }

    // Cursor tabanlı sayfalama kullanarak doktorların pagine edilmiş listesini almak için endpoint
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<DoctorResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return this.doctorService.cursor(page, pageSize);
    }

    // Belirli bir kimliğe sahip doktoru silmek için endpoint
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") int id){
        this.doctorService.delete(id);
        return ResultHelper.ok();
    }
}
