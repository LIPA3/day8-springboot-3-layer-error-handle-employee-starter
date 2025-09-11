package com.example.demo.repository;

import com.example.demo.empty.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICompanyRepository extends JpaRepository<Company, Integer> {

}
