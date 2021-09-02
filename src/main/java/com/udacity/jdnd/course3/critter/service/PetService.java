package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {
    final PetRepository petRepository;
    final CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public Pet save(Pet pet){
        if (pet.getCustomer() != null) {
            Customer customer = pet.getCustomer();
            customer.addPet(pet);
            customerRepository.save(customer);
        }
        return petRepository.save(pet);
    }

    public Pet getPet(Long petId){
        return petRepository.getOne(petId);
    }

    public List<Pet> getPetsByOwner(Long ownerId){
        Customer customer = customerRepository.getOne(ownerId);
        return petRepository.findByCustomer(customer);
    }
}
