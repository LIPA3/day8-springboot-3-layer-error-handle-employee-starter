package com.example.demo.controller;

import com.example.demo.dto.EmployeeReponse;
import com.example.demo.dto.EmployeeRequest;
import com.example.demo.dto.mapper.EmployeeMapper;
import com.example.demo.empty.Employee;
import com.example.demo.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
        return employeeService.getEmployees(gender, page, size);
    }

    @GetMapping("/{id}")
    public EmployeeReponse getEmployeeById(@PathVariable int id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeReponse createEmployee(@RequestBody @Validated EmployeeRequest employeeRequest) {
        Employee employee = employeeMapper.toEntity(employeeRequest);
        return employeeService.createEmployee(employee);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeReponse updateEmployee(@PathVariable int id, @RequestBody @Validated EmployeeRequest updatedEmployee) {
        Employee employee = employeeMapper.toEntity(updatedEmployee);
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
    }
}
