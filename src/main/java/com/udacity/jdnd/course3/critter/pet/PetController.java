package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private final ModelMapper modelMapper;
    final PetService petService;
    final CustomerService customerService;

    public PetController(ModelMapper modelMapper, PetService petService, CustomerService customerService) {
        this.modelMapper = modelMapper;
        this.modelMapper.addMappings(petFieldMapping);
        this.modelMapper.addMappings(petMapping);
        this.petService = petService;
        this.customerService = customerService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet newPet = petService.save(convertPetDTOToPet(petDTO));
        return convertPetToPetDTO(newPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertPetToPetDTO(petService.getPet(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        throw new UnsupportedOperationException();
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<PetDTO> petsDTO = new ArrayList<>();
        customerService.getPetsByOwner(ownerId).forEach(pet -> petsDTO.add(convertPetToPetDTO(pet)));
        return petsDTO;
    }

    private PetDTO convertPetToPetDTO(Pet pet) {
        return modelMapper.map(pet, PetDTO.class);
    }

    private Pet convertPetDTOToPet(PetDTO petDTO) {
        return modelMapper.map(petDTO, Pet.class);
    }

    PropertyMap<PetDTO, Pet> petMapping = new PropertyMap<PetDTO, Pet>() {
        protected void configure() {
            map().getCustomer().setId(source.getOwnerId());
        }
    };

    PropertyMap<Pet, PetDTO> petFieldMapping = new PropertyMap<Pet, PetDTO>() {
        protected void configure() {
            map().setOwnerId(source.getCustomer().getId());
        }
    };
}
