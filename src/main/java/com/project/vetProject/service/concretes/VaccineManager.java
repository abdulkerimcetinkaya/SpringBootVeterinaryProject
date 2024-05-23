package com.project.vetProject.service.concretes;

import com.project.vetProject.service.abstracts.IAnimalService;
import com.project.vetProject.service.abstracts.IVaccineService;
import com.project.vetProject.core.config.ConvertEntityToResponse;
import com.project.vetProject.core.config.modelMapper.IModelMapperService;
import com.project.vetProject.core.exception.DataAlreadyExistException;
import com.project.vetProject.core.exception.NotFoundException;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.Msg;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.repository.VaccineRepo;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.vaccine.VaccineSaveRequest;
import com.project.vetProject.dto.request.vaccine.VaccineUpdateRequest;
import com.project.vetProject.dto.response.vaccine.VaccineResponse;
import com.project.vetProject.entities.Animal;
import com.project.vetProject.entities.Vaccine;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VaccineManager implements IVaccineService {
    private final VaccineRepo vaccineRepo;
    private final IModelMapperService modelMapperService;
    private final IAnimalService animalService;
    private final ConvertEntityToResponse<Vaccine, VaccineResponse> convert;

    // Yeni bir aşı kaydeder
    @Override
    public ResultData<VaccineResponse> save(VaccineSaveRequest vaccineSaveRequest) {
        List<Vaccine> existVaccines = this.findByCodeAndName(vaccineSaveRequest.getCode(), vaccineSaveRequest.getName());
        if (!existVaccines.isEmpty() && existVaccines.get(0).getProtectionFnshDate().isAfter(LocalDate.now())) {
            return ResultHelper.error("Aynı koda sahip aşının bitiş tarihi bitmemiş! ");
        }
        if (!existVaccines.isEmpty()) {
            throw new DataAlreadyExistException(Msg.getEntityForMsg(Vaccine.class));
        }
        Animal animal = this.animalService.get(vaccineSaveRequest.getAnimalId());
        vaccineSaveRequest.setAnimalId(null);

        Vaccine saveVaccine = this.modelMapperService.forRequest().map(vaccineSaveRequest, Vaccine.class);
        saveVaccine.setAnimal(animal);

        return ResultHelper.created(this.modelMapperService.forResponse().map(this.vaccineRepo.save(saveVaccine), VaccineResponse.class));
    }

    // Belirli bir kimliğe sahip aşıyı getirir
    @Override
    public Vaccine get(int id) {
        return this.vaccineRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    // Sayfalama kullanarak aşıları getirir
    @Override
    public ResultData<CursorResponse<VaccineResponse>> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Vaccine> vaccinePage = this.vaccineRepo.findAll(pageable);
        Page<VaccineResponse> vaccineResponsePage = vaccinePage.map(vaccine -> this.modelMapperService.forResponse().map(vaccine, VaccineResponse.class));
        return ResultHelper.cursor(vaccineResponsePage);
    }

    // Hayvan kimliğine göre aşıları bulur
    @Override
    public ResultData<List<VaccineResponse>> findByAnimalId(int id) {
        List<Vaccine> vaccineList = this.vaccineRepo.findByAnimalId(id);
        List<VaccineResponse> vaccineResponseList = this.convert.convertToResponseList(vaccineList, VaccineResponse.class);
        return ResultHelper.success(vaccineResponseList);
    }

    // Belirli bir tarih aralığındaki aşıları bulur
    @Override
    public ResultData<List<VaccineResponse>> findByDate(LocalDate entryDate, LocalDate exitDate) {
        List<Vaccine> vaccineList = this.vaccineRepo.findByprotectionFnshDateBetween(entryDate, exitDate);
        List<VaccineResponse> vaccineResponseList = this.convert.convertToResponseList(vaccineList, VaccineResponse.class);
        return ResultHelper.success(vaccineResponseList);
    }

    // Kod ve isime göre aşıları bulur
    @Override
    public List<Vaccine> findByCodeAndName(String code, String name) {
        return this.vaccineRepo.findByCodeAndName(code, name);
    }

    // Aşıyı günceller
    @Override
    public ResultData<VaccineResponse> update(VaccineUpdateRequest vaccineUpdateRequest) {
        Vaccine existingVaccine = this.get(vaccineUpdateRequest.getId());
        this.modelMapperService.forRequest().map(vaccineUpdateRequest, existingVaccine);
        Vaccine savedVaccine = this.vaccineRepo.save(existingVaccine);
        return ResultHelper.success(this.modelMapperService.forResponse().map(savedVaccine, VaccineResponse.class));
    }

    // Belirli bir kimliğe sahip aşıyı siler
    @Override
    public boolean delete(int id) {
        Vaccine vaccine = this.get(id);
        this.vaccineRepo.delete(vaccine);
        return true;
    }

    // Test amaçlı true döner
    public boolean isTrue() {
        return true;
    }
}
