package com.project.vetProject.repository;

import com.project.vetProject.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    // Müşterileri isme göre bulur
    List<Customer> findByName(String name);

    // Müşterileri isim, e-posta ve telefon numarasına göre bulur
    List<Customer> findByNameAndMailAndPhone(String name, String mail, String phone);
}
