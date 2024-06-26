package com.project.vetProject.controller;

import com.project.vetProject.service.abstracts.IAppointmentService;
import com.project.vetProject.core.result.Result;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.appointment.AppointmentSaveRequest;
import com.project.vetProject.dto.request.appointment.AppointmentUpdateRequest;
import com.project.vetProject.dto.response.appointment.AppointmentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final IAppointmentService appointmentService;

    // Yeni bir randevu kaydetmek için endpoint
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save(@Valid @RequestBody AppointmentSaveRequest appointmentSaveRequest){
        return this.appointmentService.save(appointmentSaveRequest);
    }

    // Cursor tabanlı sayfalama kullanarak randevuların pagine edilmiş listesini almak için endpoint
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AppointmentResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize){
        return this.appointmentService.cursor(page, pageSize);
    }

    // Bir randevuyu güncellemek için endpoint
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> update(@Valid @RequestBody AppointmentUpdateRequest appointmentUpdateRequest){
        return this.appointmentService.update(appointmentUpdateRequest);
    }

    // Belirli bir kimliğe sahip randevuyu silmek için endpoint
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable int id){
        this.appointmentService.delete(id);
        return ResultHelper.ok();
    }

    // Belirli bir doktor kimliği ve tarih aralığına sahip randevuları almak için endpoint
    @GetMapping("/filterByDrDate/{doctorId}-{findByDate}")
    public ResultData<List<AppointmentResponse>> getDoctorIdAndDate(
            @PathVariable("doctorId") int id,
            @RequestParam(name = "entryDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate entryDate,
            @RequestParam(name = "exitDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate exitDate
    ){
        return this.appointmentService.findByDoctorIdAndDateTimeBetween(id, entryDate, exitDate);
    }

    // Belirli bir hayvan kimliği ve tarih aralığına sahip randevuları almak için endpoint
    @GetMapping("/filterByAnmlDate/{animalId}-{findByDate}")
    public ResultData<List<AppointmentResponse>> getAnimalIdAndDate(
            @PathVariable("animalId") int id,
            @RequestParam(name = "entryDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate entryDate,
            @RequestParam(name = "exitDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate exitDate
    ){
        return this.appointmentService.findByAnimalIdAndDateTimeBetween(id, entryDate, exitDate);
    }
}
