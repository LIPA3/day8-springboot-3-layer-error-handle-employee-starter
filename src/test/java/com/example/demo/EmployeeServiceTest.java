package com.example.demo;

import com.example.demo.Exception.IllegalEmployeeException;
import com.example.demo.Exception.InvalidAgeException;
import com.example.demo.dto.EmployeeReponse;
import com.example.demo.dto.EmployeeRequest;
import com.example.demo.dto.mapper.EmployeeMapper;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private IEmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper = new EmployeeMapper();

    @Test
    void should_return_employee_when_create_employee_of_greater_than_45_or_less_than_18() {
        Employee employee = new Employee(1, "Tom", 20, "MALE", 20000.0);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        Employee employeeResult = employeeMapper.toEntityTest(employeeService.createEmployee(employee));
        assertEquals(employeeResult.getAge(),employee.getAge());
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
        EmployeeReponse expectedResponse = new EmployeeReponse();
        expectedResponse.setActive(true);

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        EmployeeReponse employeeResult = employeeService.createEmployee(employee);
        assertEquals(employee.getActive(), employeeResult.getActive());
    }

    @Test
    void should_return_status_false_when_delete_employee() {
        Employee employee = new Employee(1, "Mike", 20, "MALE", 10000.0);
        assertTrue(employee.getActive());
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1);
        verify(employeeRepository).save(employee);
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
