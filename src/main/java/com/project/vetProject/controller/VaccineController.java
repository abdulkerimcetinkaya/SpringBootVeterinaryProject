package com.project.vetProject.controller;

import com.project.vetProject.service.abstracts.IVaccineService;
import com.project.vetProject.core.result.Result;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.vaccine.VaccineSaveRequest;
import com.project.vetProject.dto.request.vaccine.VaccineUpdateRequest;
import com.project.vetProject.dto.response.vaccine.VaccineResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/vaccines")
@RequiredArgsConstructor
public class VaccineController {
    private final IVaccineService vaccineService;

    // Yeni bir aşı kaydetmek için endpoint
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<VaccineResponse> save(@Valid @RequestBody VaccineSaveRequest vaccineSaveRequest){
        return this.vaccineService.save(vaccineSaveRequest);
    }

    // Cursor tabanlı sayfalama kullanarak aşıların pagine edilmiş listesini almak için endpoint
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<VaccineResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return this.vaccineService.cursor(page, pageSize);
    }

    // Bir aşıyı güncellemek için endpoint
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> update(@Valid @RequestBody VaccineUpdateRequest vaccineUpdateRequest){
        return this.vaccineService.update(vaccineUpdateRequest);
    }

    // Belirli bir kimliğe sahip aşıyı silmek için endpoint
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable int id){
        this.vaccineService.delete(id);
        return ResultHelper.ok();
    }

    // Belirli bir hayvan kimliğine sahip aşıları almak için endpoint
    @GetMapping("/animal/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccineByAnimalId(@PathVariable("id") int animalId){
        return this.vaccineService.findByAnimalId(animalId);
    }

    // Belirli bir tarih aralığındaki aşıları almak için endpoint
    @GetMapping("/findByDate")
    public ResultData<List<VaccineResponse>> getVaccinesByDate(
            @RequestParam(name = "entryDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate entryDate,
            @RequestParam(name = "exitDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate exitDate){
        return this.vaccineService.findByDate(entryDate, exitDate);
    }
}
