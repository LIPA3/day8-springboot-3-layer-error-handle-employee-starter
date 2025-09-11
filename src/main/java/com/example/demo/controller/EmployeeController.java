package com.example.demo.controller;

import com.example.demo.dto.EmployeeReponse;
import com.example.demo.dto.mapper.EmployeeMapper;
import com.example.demo.empty.Employee;
import com.example.demo.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    public  EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @GetMapping
    public List<EmployeeReponse> getEmployees(@RequestParam(required = false) String gender, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        List<Employee> employees = employeeService.getEmployees(gender, page, size);
        return employeeMapper.toResponse(employees);
    }

    @GetMapping("/{id}")
    public EmployeeReponse getEmployeeById(@PathVariable int id) {
        Employee employeeById = employeeService.getEmployeeById(id);
        return employeeMapper.toResponse(employeeById);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeReponse createEmployee(@RequestBody Employee employee) {
        Employee employees = employeeService.createEmployee(employee);
        return employeeMapper.toResponse(employees);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeReponse updateEmployee(@PathVariable int id, @RequestBody Employee updatedEmployee) {
        Employee employee = employeeService.updateEmployee(id, updatedEmployee);
        return employeeMapper.toResponse(employee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
    }
}
