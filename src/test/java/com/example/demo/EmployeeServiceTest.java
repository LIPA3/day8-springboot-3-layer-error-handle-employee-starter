package com.example.demo;

import com.example.demo.Exception.IllegalEmployeeException;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;
    @Test
    void should_return_employee_when_create_employee_of_greater_than_45_or_less_than_18() {
        Employee employee = new Employee("John Doe", 22, "MALE", 50000.0);
        when(employeeRepository.createEmployee(any(Employee.class))).thenReturn(employee);
        Employee employeeResult = employeeService.createEmployee(employee);
        assertEquals(employee.getAge(), employeeResult.getAge());
    }
    @Test
    void should_throw_exception_when_create_employee_of_greater_than_200000_or_less_than_0() {
        Employee employee = new Employee("John Doe", 16, "MALE", 250000.0);
        assertThrows(IllegalEmployeeException.class, () -> {
            employeeService.createEmployee(employee);
        });
    }

    @Test
    void should_throw_exception_when_create_employee_of_null_age() {
        Employee employee = new Employee("John Doe", 32,"MALE", 20000.0);
        assertThrows(IllegalEmployeeException.class, () -> {
            employeeService.createEmployee(employee);
        });
    }

    @Test
    void should_return_status_true_when_create_employee(){
        Employee employee = new Employee("John Doe",20,"MALE", 50000.0, true);
        when(employeeRepository.createEmployee(any(Employee.class))).thenReturn(employee);
        Employee employeeResult = employeeService.createEmployee(employee);
        assertEquals(employee.getStatus(), employeeResult.getStatus());
    }

}
