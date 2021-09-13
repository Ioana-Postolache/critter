package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.stereotype.Service;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public Pet save(Pet pet){
        Pet newPet = petRepository.save(pet);
        if (newPet.getCustomer() != null) {
            Customer customer = newPet.getCustomer();
            customer.addPet(newPet);
            customerRepository.save(customer);
        }
        return newPet;
    }

    public Pet getPet(Long petId){
        return petRepository.getOne(petId);
    }

    public Customer getOwnerByPet(Long petId){
        return petRepository.getOne(petId).getCustomer();
    }
}
