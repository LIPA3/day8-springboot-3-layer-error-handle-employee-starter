package com.example.demo.dto;

import java.util.List;

public class CompanyResponse {
    public List<EmployeeReponse> employees;
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CompanyResponse(Integer id, String name, List<EmployeeReponse> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    public CompanyResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EmployeeReponse> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeReponse> employees) {
        this.employees = employees;
    }
}
