package com.example.demo;

import com.example.demo.Exception.IllegalEmployeeException;
import com.example.demo.Exception.InvalidAgeException;
import com.example.demo.empty.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.IEmployeeRepository;
import com.example.demo.services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private IEmployeeRepository employeeRepository;

    @Test
    void should_return_employee_when_create_employee_of_greater_than_45_or_less_than_18() {
        Employee employee = new Employee("John Doe", 22, "MALE", 50000.0);
        when(employeeRepository.save(any())).thenReturn(employee);
        Employee employeeResult = employeeService.createEmployee(employee);
        assertEquals(employee.getAge(), employeeResult.getAge());
    }

    @Test
    void should_throw_exception_when_create_employee_of_greater_than_200000_or_less_than_0() {
        Employee employee = new Employee("John Doe", 16, "MALE", 250000.0);
        assertThrows(InvalidAgeException.class, () -> {
            employeeService.createEmployee(employee);
        });
    }

    @Test
    void should_throw_exception_when_create_employee_of_null_age() {
        Employee employee = new Employee("John Doe", null, "MALE", 20000.0);
        assertThrows(IllegalEmployeeException.class, () -> {
            employeeService.createEmployee(employee);
        });
    }

    @Test
    void should_return_status_true_when_create_employee() {
        Employee employee = new Employee("John Doe", 20, "MALE", 50000.0, true);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        Employee employeeResult = employeeService.createEmployee(employee);
        assertEquals(employee.getActive(), employeeResult.getActive());
    }

    @Test
    void should_return_status_false_when_delete_employee() {
        Employee employee = new Employee("John Doe", 20, "MALE", 50000.0, true);
        employee.setId(1);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        employeeService.deleteEmployee(1);
        verify(employeeRepository).save(argThat(e->e.getActive() == false));
    }

    @Test
    void should_throw_exception_when_update_employee() {
        Employee employee = new Employee("John Doe", 20, "MALE", 50000.0, true);
        employee.setId(1);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1);

        assertThrows(IllegalEmployeeException.class, () -> {
            employeeService.updateEmployee(1, employee);
        });
    }
}
