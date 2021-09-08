package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 * <p>
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    final
    EmployeeService employeeService;
    final
    CustomerService customerService;
    final PetService petService;
    private final ModelMapper modelMapper;

    Converter<List<Long>, List<Pet>> petsIdsToPetsConverter = new Converter<List<Long>, List<Pet>>() {
        public List<Pet> convert(MappingContext<List<Long>, List<Pet>> context) {
            List<Long> petIds = context.getSource();
            return petIds != null ? petIds.stream().map(petService::getPet).collect(Collectors.toList()) : null;
        }
    };

    Converter<List<Pet>, List<Long>> petsToPetsIdsConverter = context -> {
        List<Pet> pets = context.getSource();
        return pets != null ? pets.stream().map(Pet::getId).collect(Collectors.toList()) : null;
    };

    public UserController(ModelMapper modelMapper, CustomerService customerService,
                          EmployeeService employeeService, PetService petService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.petService = petService;
        this.modelMapper = modelMapper;
        this.modelMapper.createTypeMap(CustomerDTO.class, Customer.class)
                .addMappings(mapper -> mapper.using(petsIdsToPetsConverter).map(CustomerDTO::getPetIds, Customer::setPets));
        this.modelMapper.createTypeMap(Customer.class, CustomerDTO.class)
                .addMappings(mapper -> mapper.using(petsToPetsIdsConverter).map(Customer::getPets, CustomerDTO::setPetIds));
    }

    @PostMapping("/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = customerService.save(convertCustomerDTOToCustomer(customerDTO));
        return convertCustomerToCustomerDTO(customer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerDTO> customerDTOs = customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
        return customerDTOs;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        return convertCustomerToCustomerDTO(petService.getOwnerByPet(petId));
    }

    @PostMapping("/employee")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeService.save(convertEmployeeDTOToEmployee(employeeDTO));
        return convertEmployeeToEmployeeDTO(employee);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertEmployeeToEmployeeDTO(employeeService.getEmployeeById(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        throw new UnsupportedOperationException();
    }

    private CustomerDTO convertCustomerToCustomerDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

    private Customer convertCustomerDTOToCustomer(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }

    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee) {
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    private Employee convertEmployeeDTOToEmployee(EmployeeDTO employeeDTO) {
        return modelMapper.map(employeeDTO, Employee.class);
    }

}
