package com.example.demo.dto.mapper;

import com.example.demo.dto.CompanyRequest;
import com.example.demo.dto.CompanyResponse;
import com.example.demo.dto.EmployeeRequest;
import com.example.demo.empty.Company;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyMapper {
    public static CompanyResponse toResponse(Company company) {
        CompanyResponse companyResponse = new CompanyResponse();
        BeanUtils.copyProperties(company, companyResponse);
        return companyResponse;
    }
    public static List<CompanyResponse> toResponse(List<Company> employees) {
        return employees.stream().map(emp -> toResponse(emp)).toList();
    }
    public static Company toEntity(CompanyRequest companyRequest) {
        Company company = new Company();
        BeanUtils.copyProperties(companyRequest, company);
        return company;
    }
    public static Company toEntityTest(CompanyResponse companyResponse) {
        Company company = new Company();
        BeanUtils.copyProperties(companyResponse, company);
        return company;
    }
}
