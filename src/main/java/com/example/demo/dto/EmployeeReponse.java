package com.example.demo.dto;

public class EmployeeReponse {
    private Integer id;
    private String name;
    private Integer age;
    private String gender;
    private Boolean active;

    public EmployeeReponse(Integer id, String name, Integer age, String gender, Boolean active) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.active = active;
    }

    public EmployeeReponse() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
