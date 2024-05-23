package com.project.vetProject.service.concretes;

import com.project.vetProject.service.abstracts.IAnimalService;
import com.project.vetProject.service.abstracts.ICustomerService;
import com.project.vetProject.core.config.modelMapper.IModelMapperService;
import com.project.vetProject.core.exception.DataAlreadyExistException;
import com.project.vetProject.core.exception.NotFoundException;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.config.ConvertEntityToResponse;
import com.project.vetProject.core.utilies.Msg;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.repository.AnimalRepo;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.animal.AnimalSaveRequest;
import com.project.vetProject.dto.request.animal.AnimalUpdateRequest;
import com.project.vetProject.dto.response.animal.AnimalResponse;
import com.project.vetProject.entities.Animal;
import com.project.vetProject.entities.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalManager implements IAnimalService {
    private final ConvertEntityToResponse<Animal, AnimalResponse> convert;
    private final AnimalRepo animalRepo;
    private final IModelMapperService modelMapperService;
    private final ICustomerService customerService;


    // Hayvan kaydetme metodudur. Öncelikle müşteri bilgilerini alır,
    // hayvan nesnesine dönüştürür ve eğer benzer bir hayvan mevcut değilse veritabanına kaydeder.
    @Override
    public ResultData<AnimalResponse> save(AnimalSaveRequest animalSaveRequest) {
        Customer customer = this.customerService.get(animalSaveRequest.getCustomerId());
        animalSaveRequest.setCustomerId(null);
        Animal saveAnimal = this.modelMapperService.forRequest().map(animalSaveRequest, Animal.class);
        saveAnimal.setCustomer(customer);
        List<Animal> animalList = this.findByNameAndSpeciesAndBreedAndGender(
                animalSaveRequest.getName(),
                animalSaveRequest.getSpecies(),
                animalSaveRequest.getBreed(),
                animalSaveRequest.getGender()
        );
        if (!animalList.isEmpty()){
            throw new DataAlreadyExistException(Msg.getEntityForMsg(Animal.class));
        }
        return ResultHelper.created(this.modelMapperService.forResponse().map(this.animalRepo.save(saveAnimal), AnimalResponse.class));
    }

    // Verilen ID'ye göre hayvanı getirir. Eğer hayvan bulunamazsa NotFoundException fırlatır.
    @Override
    public Animal get(int id) {
        return this.animalRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }


    // Sayfalama yaparak hayvanları getirir ve cursor tabanlı yanıt döner.
    @Override
    public ResultData<CursorResponse<AnimalResponse>> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Animal> animalPage = this.animalRepo.findAll(pageable);
        Page<AnimalResponse> animalResponsePage = animalPage.map(animal -> this.modelMapperService.forResponse().map(animal, AnimalResponse.class));
        return ResultHelper.cursor(animalResponsePage);
    }

    // İsme göre hayvanları arar ve yanıt olarak hayvan listesi döner.
    @Override
    public ResultData<List<AnimalResponse>> findByName(String name) {
        List<Animal> animalList = this.animalRepo.findByName(name);
        List<AnimalResponse> animalResponseList = this.convert.convertToResponseList(animalList, AnimalResponse.class);
        return ResultHelper.success(animalResponseList);
    }

    // Müşteri ID'sine göre hayvanları arar ve yanıt olarak hayvan listesi döner.
    @Override
    public ResultData<List<AnimalResponse>> findByCustomerId(int id) {
        List<Animal> animalList = this.animalRepo.findByCustomerId(id);
        List<AnimalResponse> animalResponseList = this.convert.convertToResponseList(animalList, AnimalResponse.class);
        return ResultHelper.success(animalResponseList);
    }

    // Belirtilen isim, tür, cins ve cinsiyete göre hayvanları arar.
    @Override
    public List<Animal> findByNameAndSpeciesAndBreedAndGender(String name, String species, String breed, String gender) {
        return this.animalRepo.findByNameAndSpeciesAndBreedAndGender(name, species, breed, gender);
    }

    // Hayvan güncelleme metodudur. Öncelikle mevcut hayvanı getirir,
    // güncellenmiş bilgilerle kaydeder ve yanıt olarak güncellenmiş hayvan döner.
    @Override
    public ResultData<AnimalResponse> update(AnimalUpdateRequest animalUpdateRequest) {
        this.get(animalUpdateRequest.getId());
        Animal updateAnimal = this.modelMapperService.forRequest().map(animalUpdateRequest, Animal.class);
        this.animalRepo.save(updateAnimal);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateAnimal, AnimalResponse.class));
    }


    // Verilen ID'ye göre hayvanı siler. Başarılı olduğunda true döner.
    @Override
    public boolean delete(int id) {
        Animal animal = this.get(id);
        this.animalRepo.delete(animal);
        return true;
    }
}
