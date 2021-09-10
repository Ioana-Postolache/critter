package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;

@Service
public class EmployeeService {
    final
    EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee save(Employee employee){
        return employeeRepository.save(employee);
    }

    public List<Employee> findEmployeesForService(DayOfWeek availableDay, Set<EmployeeSkill> skills){
        List<Employee> employeesForService = new ArrayList<>();
        List<Employee> employeesAvailable = employeeRepository.findEmployeesByDaysAvailable(availableDay);
        for (Employee e : employeesAvailable){
            if(e.getSkills().containsAll(skills)){
                employeesForService.add(e);
            }
        }
        return employeesForService;
    }

    public Employee getEmployeeById(Long id){
        return employeeRepository.getOne(id);
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId){
        Employee employee = employeeRepository.getOne(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }
}
