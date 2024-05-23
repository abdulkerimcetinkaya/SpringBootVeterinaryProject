package com.project.vetProject.controller;

import com.project.vetProject.service.abstracts.IAvailableDateService;
import com.project.vetProject.core.result.Result;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.availableDate.AvailableDateSaveRequest;
import com.project.vetProject.dto.request.availableDate.AvailableDateUpdateRequest;
import com.project.vetProject.dto.response.availableDate.AvailableDateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/available-dates")
@RequiredArgsConstructor
public class AvailableDateController {
    private final IAvailableDateService availableDateService;

    // Yeni bir uygun tarih kaydetmek için endpoint
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AvailableDateResponse> save(@Valid @RequestBody AvailableDateSaveRequest availableDateSaveRequest){
        return this.availableDateService.save(availableDateSaveRequest);
    }

    // Cursor tabanlı sayfalama kullanarak uygun tarihlerin pagine edilmiş listesini almak için endpoint
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AvailableDateResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize){
        return this.availableDateService.cursor(page, pageSize);
    }

    // Bir uygun tarihi güncellemek için endpoint
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> update(@Valid @RequestBody AvailableDateUpdateRequest availableDateUpdateRequest){
        return this.availableDateService.update(availableDateUpdateRequest);
    }

    // Belirli bir kimliğe sahip uygun tarihi silmek için endpoint
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable int id){
        this.availableDateService.delete(id);
        return ResultHelper.ok();
    }
}
