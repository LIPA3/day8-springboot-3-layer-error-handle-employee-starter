package com.example.demo;

import com.example.demo.controller.EmployeeController;
import com.example.demo.empty.Employee;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanEmployee() throws Exception {
        jdbcTemplate.execute("DELETE FROM employee;");
        jdbcTemplate.execute("ALTER TABLE employee AUTO_INCREMENT = 1;");
    }
    private void createJohnSmith() throws Exception {
        Gson gson = new Gson();
        String john = gson.toJson(new Employee(null, "John Smith", 28, "MALE", 60000.0)).toString();
        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(john));
    }

    private void createJaneDoe() throws Exception {
        Gson gson = new Gson();
        String jane = gson.toJson(new Employee(null, "Jane Doe", 22, "FEMALE", 60000.0));
        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(jane));
    }

    private String genEmployeeJsonString(Integer id, String name, int age, String gender, double salary) {
        Gson gson = new Gson();
        Employee employee = new Employee(id, name, age, gender, salary);
        return gson.toJson(employee);
    }
//
//    @BeforeEach
//    void setup() throws Exception {
//        mockMvc.perform(delete("/employees/all"));
//    }

    @Test
    void should_return_404_when_employee_not_found() throws Exception {
        mockMvc.perform(get("/employees/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_all_employee() throws Exception {
        createJohnSmith();
        createJaneDoe();

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_employee_when_employee_found() throws Exception {
        createJohnSmith();

        mockMvc.perform(get("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(60000.0));
    }

    @Test
    void should_return_male_employee_when_employee_found() throws Exception {
        createJohnSmith();
        createJaneDoe();

        mockMvc.perform(get("/employees?gender=male")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].age").value(28))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[0].salary").value(60000.0));
    }

    @Test
    void should_create_employee() throws Exception {
        String requestBody = genEmployeeJsonString(null, "John Smith", 28, "MALE", 60000.0);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(60000));
    }

    @Test
    void should_return_200_with_empty_body_when_no_employee() throws Exception {
        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void should_return_200_with_employee_list() throws Exception {
        createJohnSmith();
        createJaneDoe();

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].age").value(28))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[0].salary").value(60000.0))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"))
                .andExpect(jsonPath("$[1].age").value(22))
                .andExpect(jsonPath("$[1].gender").value("FEMALE"))
                .andExpect(jsonPath("$[1].salary").value(60000.0));
    }

    @Test
    void should_status_204_when_delete_employee() throws Exception {
        createJohnSmith();

        mockMvc.perform(delete("/employees/" + 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_status_200_when_update_employee() throws Exception {
        createJohnSmith();
        String requestBody = genEmployeeJsonString(null, "John Smith", 29, "MALE", 65000.0);

        mockMvc.perform(put("/employees/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.age").value(29))
                .andExpect(jsonPath("$.salary").value(65000.0));
    }

    @Test
    void should_status_200_and_return_paged_employee_list() throws Exception {
        createJohnSmith();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();

        mockMvc.perform(get("/employees?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void should_return_employee_is_not_active_when_delete_employee() throws Exception {
        createJohnSmith();
        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void should_update_employee_when_update_employee_is_active() throws Exception {
        createJohnSmith();

        String updateRequest = genEmployeeJsonString(null, "Lily", 30, "MALE", 70000.0);
        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Lily"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(70000));
    }

    @Test
    void should_return_400_for_invalid_age() throws Exception {
        String employee = genEmployeeJsonString(null, "Lily", 15, "MALE", 70000.0);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employee))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_for_age_salary_mismatch() throws Exception {
        String employee = genEmployeeJsonString(null, "Tom", 35, "MALE", 15000.0);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employee))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_throw_exception_when_create_employee_of_greater_than_65_or_less_than_18() throws Exception {
        Gson gson = new Gson();
        String john = gson.toJson(new Employee("John Smith",  66, "Male", 60000.0));
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON).content(john))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_throw_exception_when_update_employee_is_inactive() throws Exception {
        Gson gson = new Gson();
        String updateInfo = gson.toJson(new Employee("Tom", 28, "Male", 7000.0, false));
        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON).content(updateInfo))
                .andExpect(status().isNotFound());
    }
}
