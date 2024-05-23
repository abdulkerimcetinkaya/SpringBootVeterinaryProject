package com.project.vetProject.repository;

import com.project.vetProject.entities.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepo extends JpaRepository<Animal, Integer> {

    // Hayvanları isme göre bulur
    List<Animal> findByName(String name);

    // Hayvanları müşteri kimliğine göre bulur
    List<Animal> findByCustomerId(int id);

    // Hayvanları isim, tür, cins ve cinsiyete göre bulur
    List<Animal> findByNameAndSpeciesAndBreedAndGender(String name, String species, String breed, String gender);
}
