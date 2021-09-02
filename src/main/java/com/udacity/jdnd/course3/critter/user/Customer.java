package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Customer extends User {

    private String phoneNumber;
    private String notes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets;

    public Customer(Long id, String name, String phoneNumber, String notes, List<Pet> pets) {
        super(id, name);
        this.phoneNumber = phoneNumber;
        this.notes = notes;
        this.pets = pets;
    }

    public void addPet(Pet pet) {
        if (this.pets == null) {
            this.setPets(new ArrayList<>());
        }
        this.pets.add(pet);
    }
}
