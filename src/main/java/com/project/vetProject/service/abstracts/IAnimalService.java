package com.project.vetProject.service.abstracts;

import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.animal.AnimalSaveRequest;
import com.project.vetProject.dto.request.animal.AnimalUpdateRequest;
import com.project.vetProject.dto.response.animal.AnimalResponse;
import com.project.vetProject.entities.Animal;

import java.util.List;

public interface IAnimalService {

    // Yeni bir hayvan kaydeder
    ResultData<AnimalResponse> save(AnimalSaveRequest animalSaveRequest);

    // Belirli bir kimliğe sahip hayvanı getirir
    Animal get(int id);

    // Sayfalama kullanarak hayvanları getirir
    ResultData<CursorResponse<AnimalResponse>> cursor(int page, int pageSize);

    // İsme göre hayvanları bulur
    ResultData<List<AnimalResponse>> findByName(String name);

    // Müşteri kimliğine göre hayvanları bulur
    ResultData<List<AnimalResponse>> findByCustomerId(int id);

    // İsim, tür, cins ve cinsiyete göre hayvanları bulur
    List<Animal> findByNameAndSpeciesAndBreedAndGender(String name, String species, String breed, String gender);

    // Hayvanı günceller
    ResultData<AnimalResponse> update(AnimalUpdateRequest animalUpdateRequest);

    // Belirli bir kimliğe sahip hayvanı siler
    boolean delete(int id);
}
