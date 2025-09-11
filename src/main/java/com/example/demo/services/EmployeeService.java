package com.example.demo.services;

import com.example.demo.Exception.InvalidAgeException;
import com.example.demo.Exception.InvalidSalaryException;
import com.example.demo.dto.EmployeeReponse;
import com.example.demo.dto.mapper.EmployeeMapper;
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
    private final EmployeeMapper employeeMapper;
    public EmployeeService(IEmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public List<EmployeeReponse> getEmployees(String gender, Integer page, Integer size) {
        if(gender == null){
            if(page == null && size == null) {
                return employeeMapper.toResponse(employeeRepository.findAll());
            }else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeMapper.toResponse(employeeRepository.findAll(pageable).toList());
            }
        }else{
            if(page == null || size == null) {
                return employeeMapper.toResponse(employeeRepository.findEmployeesByGender(gender));
            }else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeMapper.toResponse(employeeRepository.findEmployeesByGender(gender, pageable));
            }
        }
    }
    public EmployeeReponse getEmployeeById(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employeeMapper.toResponse(employee.get());
    }
    public EmployeeReponse createEmployee(Employee employee) {
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
        return EmployeeMapper.toResponse(employeeRepository.save(employee));
    }

    public EmployeeReponse updateEmployee(int id, Employee updatedEmployee) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        Employee employee = employeeOptional.get();
        if(!employee.getActive()){
            throw new IllegalEmployeeException("employee status is false!");
        }
        employee.setName(updatedEmployee.getName());
        employee.setAge(updatedEmployee.getAge());
        employee.setGender(updatedEmployee.getGender());
        employee.setSalary(updatedEmployee.getSalary());
        employee.setActive(true);
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    public void deleteEmployee(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        employee.get().setActive(false);
        employeeRepository.save(employee.get());
    }

}
