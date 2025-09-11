package com.example.demo.services;

import com.example.demo.Exception.InvalidAgeException;
import com.example.demo.Exception.InvalidSalaryException;
import com.example.demo.empty.Employee;
import com.example.demo.Exception.IllegalEmployeeException;
import com.example.demo.repository.IEmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final IEmployeeRepository employeeRepository;
    public EmployeeService(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        if(gender == null){
            if(page == null && size == null) {
                return employeeRepository.findAll();
            }else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeRepository.findAll(pageable).toList();
            }
        }else{
            if(page == null || size == null) {
                return employeeRepository.findEmployeesByGender(gender);
            }else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeRepository.findEmployeesByGender(gender, pageable);
            }
        }
    }
    public Employee getEmployeeById(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employee.get();
    }
    public Employee createEmployee(Employee employee) {
        employee.setActive(true);
        if(employee.getAge() == null){
            throw new IllegalEmployeeException("Employee age cannot be null");
        }
        if (employee.getAge() < 18 || employee.getAge() > 45) {
            throw new InvalidAgeException("Employee age must be between 18 and 45");
        }
        if(employee.getAge()>30 && employee.getSalary()<200000){
            throw new InvalidSalaryException("Employee salary must be greater than 200000 if age is greater than 30");
        }
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        if(!employee.get().getActive()){
            throw new IllegalEmployeeException("He/She is already left");
        }
        updatedEmployee.setId(id);
        updatedEmployee.setActive(true);
        return employeeRepository.save(updatedEmployee);
    }

    public void deleteEmployee(int id) {
        Employee employee = getEmployeeById(id);
        employee.setActive(false);
        employeeRepository.save(employee);
    }

}
