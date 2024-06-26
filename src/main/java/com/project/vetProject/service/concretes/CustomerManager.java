package com.project.vetProject.service.concretes;

import com.project.vetProject.service.abstracts.ICustomerService;
import com.project.vetProject.core.config.ConvertEntityToResponse;
import com.project.vetProject.core.config.modelMapper.IModelMapperService;
import com.project.vetProject.core.exception.DataAlreadyExistException;
import com.project.vetProject.core.exception.NotFoundException;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.Msg;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.repository.CustomerRepo;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.customer.CustomerSaveRequest;
import com.project.vetProject.dto.request.customer.CustomerUpdateRequest;
import com.project.vetProject.dto.response.customer.CustomerResponse;
import com.project.vetProject.entities.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerManager implements ICustomerService {
    private final CustomerRepo customerRepo;
    private final IModelMapperService modelMapperService;
    private final ConvertEntityToResponse<Customer, CustomerResponse> convert;

    // Yeni bir müşteri kaydeder
    @Override
    public ResultData<CustomerResponse> save(CustomerSaveRequest customerSaveRequest) {
        Customer saveCustomer = this.modelMapperService.forRequest().map(customerSaveRequest, Customer.class);
        List<Customer> getByNamePhoneMail = this.findByNameAndMailAndPhone(
                saveCustomer.getName(),
                saveCustomer.getMail(),
                saveCustomer.getPhone());
        if (!getByNamePhoneMail.isEmpty()) {
            throw new DataAlreadyExistException(Msg.getEntityForMsg(Customer.class));
        }
        return ResultHelper.created(this.modelMapperService.forResponse().map(this.customerRepo.save(saveCustomer), CustomerResponse.class));
    }

    // Belirli bir kimliğe sahip müşteriyi getirir
    @Override
    public Customer get(int id) {
        return this.customerRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    // Sayfalama kullanarak müşterileri getirir
    @Override
    public ResultData<CursorResponse<CustomerResponse>> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Customer> customerPage = this.customerRepo.findAll(pageable);
        Page<CustomerResponse> customerResponsePage = customerPage.map(customer -> this.modelMapperService.forResponse().map(customer, CustomerResponse.class));
        return ResultHelper.cursor(customerResponsePage);
    }

    // Müşteriyi günceller
    @Override
    public ResultData<CustomerResponse> update(CustomerUpdateRequest customerUpdateRequest) {
        this.get(customerUpdateRequest.getId());
        Customer updateCustomer = this.modelMapperService.forRequest().map(customerUpdateRequest, Customer.class);
        return ResultHelper.success(this.modelMapperService.forResponse().map(this.customerRepo.save(updateCustomer), CustomerResponse.class));
    }

    // İsme göre müşterileri bulur
    @Override
    public ResultData<List<CustomerResponse>> findByName(String name) {
        List<Customer> customerList = this.customerRepo.findByName(name);
        List<CustomerResponse> customerResponseList = this.convert.convertToResponseList(customerList, CustomerResponse.class);
        return ResultHelper.success(customerResponseList);
    }

    // İsim, e-posta ve telefon numarasına göre müşterileri bulur
    @Override
    public List<Customer> findByNameAndMailAndPhone(String name, String mail, String phone) {
        return this.customerRepo.findByNameAndMailAndPhone(name, mail, phone);
    }

    // Belirli bir kimliğe sahip müşteriyi siler
    @Override
    public boolean delete(int id) {
        Customer customer = this.get(id);
        this.customerRepo.delete(customer);
        return true;
    }
}
