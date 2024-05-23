package com.project.vetProject.service.abstracts;

import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.customer.CustomerSaveRequest;
import com.project.vetProject.dto.request.customer.CustomerUpdateRequest;
import com.project.vetProject.dto.response.customer.CustomerResponse;
import com.project.vetProject.entities.Customer;

import java.util.List;

public interface ICustomerService {

    // Yeni bir müşteri kaydeder
    ResultData<CustomerResponse> save(CustomerSaveRequest customerSaveRequest);

    // Belirli bir kimliğe sahip müşteriyi getirir
    Customer get(int id);

    // Sayfalama kullanarak müşterileri getirir
    ResultData<CursorResponse<CustomerResponse>> cursor(int page, int pageSize);

    // Müşteriyi günceller
    ResultData<CustomerResponse> update(CustomerUpdateRequest customerUpdateRequest);

    // İsme göre müşterileri bulur
    ResultData<List<CustomerResponse>> findByName(String name);

    // İsim, e-posta ve telefon numarasına göre müşterileri bulur
    List<Customer> findByNameAndMailAndPhone(String name, String mail, String phone);

    // Belirli bir kimliğe sahip müşteriyi siler
    boolean delete(int id);
}
