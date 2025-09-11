package com.example.demo.dto.mapper;

import com.example.demo.dto.CompanyResponse;
import com.example.demo.dto.EmployeeRequest;
import com.example.demo.empty.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyMapper {
    public static CompanyResponse toResponse(Employee employee) {
        CompanyResponse companyResponse = new CompanyResponse();
        BeanUtils.copyProperties(employee, companyResponse);
        return companyResponse;
    }
    public static List<CompanyResponse> toResponse(List<Employee> employees) {
        return employees.stream().map(emp -> toResponse(emp)).toList();
    }
    public static Employee toEntity(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeRequest, employee);
        return employee;
    }
    public static Employee toEntityTest(CompanyResponse companyResponse) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(companyResponse, employee);
        return employee;
    }
}
