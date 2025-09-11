package com.example.demo.services;

import com.example.demo.dto.CompanyResponse;
import com.example.demo.dto.mapper.CompanyMapper;
import com.example.demo.dto.mapper.EmployeeMapper;
import com.example.demo.empty.Company;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.ICompanyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final ICompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyService(ICompanyRepository companyRepository,  CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    public List<Company> getCompanies(Integer page, Integer size) {
        if(page == null && size == null) {
            return companyRepository.findAll();
        }else {
            Pageable pageable = PageRequest.of(page - 1, size);
            return companyRepository.findAll(pageable).toList();
        }
    }

    public CompanyResponse createCompany(Company company) {
        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    public CompanyResponse updateCompany(int id, Company updatedCompany) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        Company company = companyOptional.get();
        company.setName(updatedCompany.getName());
        company.setEmployees(updatedCompany.getEmployees());
        return companyMapper.toResponse(companyRepository.save(company));
    }

    public Company getCompanyById(int id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return company.get();
    }

//    public void deleteCompany(int id) {
//        Company company = getCompanyById(id);
//        if (company == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
//        }
//        companyRepository.deleteCompany(id);
//    }

//    public void deleteAllCompanies() {
//        companyRepository.deleteAllCompanies();
//    }


}
