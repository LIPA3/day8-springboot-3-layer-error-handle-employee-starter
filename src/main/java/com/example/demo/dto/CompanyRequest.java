package com.example.demo.dto;

public class CompanyRequest {
    public String name;

    public CompanyRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompanyRequest() {
    }
}
