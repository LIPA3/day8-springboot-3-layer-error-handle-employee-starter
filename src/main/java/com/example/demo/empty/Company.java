package com.example.demo.empty;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "companyId")
    private List<Employee> employees;

    public List<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    public Company(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Company() {

    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
}
