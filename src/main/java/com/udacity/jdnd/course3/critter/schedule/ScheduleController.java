package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final PetService petService;
    private final EmployeeService employeeService;
    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    private final Converter<List<Long>, List<Pet>> petsIdsToPetsConverter = new Converter<List<Long>, List<Pet>>() {
        public List<Pet> convert(MappingContext<List<Long>, List<Pet>> context) {
            List<Long> petIds = context.getSource();
            return petIds != null ? petIds.stream().map(petService::getPet).collect(Collectors.toList()) : null;
        }
    };

    private final Converter<List<Pet>, List<Long>> petsToPetsIdsConverter = context -> {
        List<Pet> pets = context.getSource();
        return pets != null ? pets.stream().map(Pet::getId).collect(Collectors.toList()) : null;
    };

    private final Converter<List<Long>, List<Employee>> employeesIdsToEmployeesConverter = new Converter<List<Long>, List<Employee>>() {
        public List<Employee> convert(MappingContext<List<Long>, List<Employee>> context) {
            List<Long> employeeIds = context.getSource();
            return employeeIds != null ? employeeIds.stream().map(employeeService::getEmployee).collect(Collectors.toList()) : null;
        }
    };

    private final Converter<List<Employee>, List<Long>> employeesToEmployeesIdsConverter = context -> {
        List<Employee> pets = context.getSource();
        return pets != null ? pets.stream().map(Employee::getId).collect(Collectors.toList()) : null;
    };

    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {
        return modelMapper.map(schedule, ScheduleDTO.class);
    }

    private Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO) {
        return modelMapper.map(scheduleDTO, Schedule.class);
    }

    public ScheduleController(ScheduleService scheduleService, ModelMapper modelMapper,
                              PetService petService, EmployeeService employeeService, CustomerService customerService) {
        this.scheduleService = scheduleService;
        this.petService = petService;
        this.employeeService = employeeService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
        modelMapper.createTypeMap(ScheduleDTO.class, Schedule.class)
                .addMappings(mapper -> mapper.using(petsIdsToPetsConverter)
                        .map(ScheduleDTO::getPetIds, Schedule::setPets))
        .addMappings(mapper->mapper.using(employeesIdsToEmployeesConverter)
                .map(ScheduleDTO::getEmployeeIds, Schedule::setEmployees));
        modelMapper.createTypeMap(Schedule.class, ScheduleDTO.class)
                .addMappings(mapper -> mapper.using(petsToPetsIdsConverter)
                        .map(Schedule::getPets, ScheduleDTO::setPetIds))
                .addMappings(mapper -> mapper.using(employeesToEmployeesIdsConverter)
                        .map(Schedule::getEmployees, ScheduleDTO::setEmployeeIds));
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertScheduleToScheduleDTO(scheduleService.save(convertScheduleDTOToSchedule(scheduleDTO)));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getAllSchedules().stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return scheduleService.getScheduleForPet(petService.getPet(petId)).stream()
                .map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return scheduleService.getScheduleForEmployee(employeeService.getEmployee(employeeId)).stream()
                .map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Pet> pets = customerService.getPetsByOwner(customerId);
        List<Schedule> schedulesForCustomer = new ArrayList<>();
        pets.forEach(pet ->{
            schedulesForCustomer.addAll(scheduleService.getScheduleForPet(petService.getPet(pet.getId())));
        });
        return schedulesForCustomer.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }
}
