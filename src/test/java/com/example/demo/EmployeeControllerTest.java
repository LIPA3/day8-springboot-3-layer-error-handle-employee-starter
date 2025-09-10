package com.example.demo;

import com.example.demo.services.EmployeeService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeService employeeService;
//    @Autowired
//    private EmployeeController employeeController;
//
//    private static Employee employee(String name, int age, String gender, double salary) {
//        Employee e = new Employee();
//        e.setName(name);
//        e.setAge(age);
//        e.setGender(gender);
//        e.setSalary(salary);
//        return e;
//    }
//
    private void crateJohnSmith() throws Exception {
        Gson gson = new Gson();
        String john=gson.toJson(new Employee("John Smith", 28, "MALE", 60000.0));
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(john));
    }

    private void createJaneDoe() throws Exception {
        Gson gson = new Gson();
        String john=gson.toJson(new Employee("Jane Doe", 48, "FEMALE", 60000.0));
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(john));
    }

    @BeforeEach
    void cleanEmployees() throws Exception {
        mockMvc.perform(delete("/employees/all"));
    }

    @Test
    void should_return_404_when_employee_not_found() throws Exception {
        mockMvc.perform(get("/employees/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_all_employee() throws Exception {
        crateJohnSmith();
        createJaneDoe();
        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_employee_when_employee_found() throws Exception {
        crateJohnSmith();

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
        crateJohnSmith();
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
        String requestBody = """
                        {
                            "name": "John Smith",
                            "age": 28,
                            "gender": "MALE",
                            "salary": 60000
                        }
                """;

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
        crateJohnSmith();

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].age").value(28))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[0].salary").value(60000.0));
    }

    @Test
    void should_status_204_when_delete_employee() throws Exception {
        crateJohnSmith();

        mockMvc.perform(delete("/employees/" + 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_status_200_when_update_employee() throws Exception {
        crateJohnSmith();
        String requestBody = """
                        {
                            "name": "John Smith",
                            "age": 29,
                            "gender": "MALE",
                            "salary": 65000.0
                        }
                """;

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
        crateJohnSmith();
        createJaneDoe();
        crateJohnSmith();
        createJaneDoe();
        crateJohnSmith();
        createJaneDoe();
        crateJohnSmith();

        mockMvc.perform(get("/employees?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }
    @Test
    void should_return_status_true_when_create_employee(){
            Employee employee = new Employee("John Doe", 20, "MALE", 50000.0, true);
            Employee created = employeeService.createEmployee(employee);
            assertTrue(created.getActive());
            assertNotNull(created.getId());

    }
    @Test
    void should_return_status_false_when_delete_employee() {
        Employee employee = new Employee("Jane Doe", 25, "FEMALE", 60000.0, true);
        Employee created = employeeService.createEmployee(employee);

        employeeService.deleteEmployee(created.getId());
        Employee afterDelete = employeeService.getEmployeeById(created.getId());
        assertFalse(afterDelete.getActive());
    }
}
