package com.example.demo;

import com.example.demo.controller.CompanyController;
import com.example.demo.empty.Company;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyController companyController;

    @BeforeEach
    void cleanCompanies() {
        companyController.empty();
    }

    private void createCompany1() throws Exception {
        Gson gson = new Gson();
        String company1 = gson.toJson(new Company(null, "Spring")).toString();

        mockMvc.perform(post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(company1));
    }
    private void createCompany2() throws Exception {
        Gson gson = new Gson();
        String company2 = gson.toJson(new Company(null, "Spring2")).toString();

        mockMvc.perform(post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(company2));
    }
    @Test
    void should_return_created_company_when_post_companies() throws Exception {
        String requestBody = """
                {
                    "name": "Spring"
                }
                """;
        MockHttpServletRequestBuilder request = post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Spring"));
    }

    @Test
    void should_return_all_companies_when_no_param() throws Exception {
        createCompany1();
        createCompany2();

        mockMvc.perform(get("/companies").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_company_when_get_id_found() throws Exception {
        Company spring = new Company(null, "Spring");
        Company company = companyController.createCompany(spring);

        MockHttpServletRequestBuilder request = get("/companies/" + company.getId())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company.getId()))
                .andExpect(jsonPath("$.name").value(company.getName()));
    }

    @Test
    void should_return_company_when_put_with_id_found() throws Exception {
        Company spring = new Company(null, "Spring");
        spring.setName("Spring");
        Company company = companyController.createCompany(spring);
        String requestBody = """
                {
                    "name": "Spring2"
                }
                """;
        MockHttpServletRequestBuilder request = put("/companies/" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company.getId()))
                .andExpect(jsonPath("$.name").value("Spring2"));
    }

    @Test
    void should_return_no_content_when_delete_id_found() throws Exception {
        Company spring = new Company(null, "Spring");
        Company company = companyController.createCompany(spring);

        MockHttpServletRequestBuilder request = delete("/companies/" + company.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void should_return_truncated_companies_when_page_size_is_limit() throws Exception {
        createCompany1();
        createCompany2();
        createCompany1();
        createCompany2();
        createCompany1();
        createCompany2();
        MockHttpServletRequestBuilder request = get("/companies?page=1&size=5")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void should_status_404_when_get_company_by_id_not_found() throws Exception {
        MockHttpServletRequestBuilder request = get("/companies/999")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void should_status_404_when_put_company_by_id_not_found() throws Exception {
        String requestBody = """
                {
                    "name": "Spring2"
                }
                """;
        MockHttpServletRequestBuilder request = put("/companies/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
