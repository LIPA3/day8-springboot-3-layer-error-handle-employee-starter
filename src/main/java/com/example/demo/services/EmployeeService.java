package com.example.demo.services;

import com.example.demo.Employee;
import com.example.demo.Exception.IllegalEmployeeException;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        return employeeRepository.getEmployees(gender, page, size);
    }
    public Employee getEmployeeById(int id) {
        Employee employee = employeeRepository.getEmployeeById(id);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employee;
    }
    public Employee createEmployee(Employee employee) {
        if(employee.getAge() == null){
            throw new IllegalEmployeeException("Employee age cannot be null");
        }
        if (employee.getAge() < 18 || employee.getAge() > 45) {
            throw new IllegalEmployeeException("Employee age must be between 18 and 45");
        }
        if(employee.getAge()>30 && employee.getSalary()<200000){
            throw new IllegalEmployeeException("Employee older than 30 cannot have salary less than 200000");
        }
        return employeeRepository.createEmployee(employee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        return employeeRepository.updateEmployee(id, updatedEmployee);
    }

    public void deleteEmployee(int id) {
        Employee employeeById = employeeRepository.getEmployeeById(id);
        // Check existence
        if (employeeById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        employeeRepository.deleteEmployee(employeeById);
    }

    public void empty() {
        employeeRepository.empty();
    }
}
