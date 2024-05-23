package com.project.vetProject.controller;

import com.project.vetProject.service.abstracts.ICustomerService;
import com.project.vetProject.core.result.Result;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.customer.CustomerSaveRequest;
import com.project.vetProject.dto.request.customer.CustomerUpdateRequest;
import com.project.vetProject.dto.response.customer.CustomerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final ICustomerService customerService;

    // Yeni bir müşteri kaydetmek için endpoint
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<CustomerResponse> save(@Valid @RequestBody CustomerSaveRequest customerSaveRequest){
        return this.customerService.save(customerSaveRequest);
    }

    // Bir müşteriyi güncellemek için endpoint
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CustomerResponse> update(@Valid @RequestBody CustomerUpdateRequest customerUpdateRequest){
        return this.customerService.update(customerUpdateRequest);
    }

    // Cursor tabanlı sayfalama kullanarak müşterilerin pagine edilmiş listesini almak için endpoint
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<CustomerResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return this.customerService.cursor(page, pageSize);
    }

    // Belirli bir kimliğe sahip müşteriyi silmek için endpoint
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") int id){
        this.customerService.delete(id);
        return ResultHelper.ok();
    }

    // Belirli bir isme sahip müşterileri almak için endpoint
    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<CustomerResponse>> get(@PathVariable("name") String name){
        return this.customerService.findByName(name);
    }
}
