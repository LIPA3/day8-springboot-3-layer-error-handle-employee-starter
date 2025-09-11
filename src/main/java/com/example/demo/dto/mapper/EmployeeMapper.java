package com.example.demo.dto.mapper;

import com.example.demo.dto.EmployeeReponse;
import com.example.demo.empty.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeMapper {
    public EmployeeReponse toResponse(Employee employee) {
        EmployeeReponse employeeReponse = new EmployeeReponse();
                BeanUtils.copyProperties(employee, employeeReponse);
                return employeeReponse;
    }
    public List<EmployeeReponse> toResponse(List<Employee> employees) {
        return employees.stream().map(this::toResponse).toList();
    }
}
