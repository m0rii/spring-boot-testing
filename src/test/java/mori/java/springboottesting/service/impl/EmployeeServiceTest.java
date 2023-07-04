package mori.java.springboottesting.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import mori.java.springboottesting.exception.ResourceNotFoundException;
import mori.java.springboottesting.model.Employee;
import mori.java.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee, employee2;


    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Mori")
                .lastName("Java")
                .email("Mori@java.com")
                .build();
        employee2 = Employee.builder()
                .id(2L)
                .firstName("Aha")
                .lastName("Java")
                .email("aha@java.com")
                .build();
    }

    @DisplayName("Junit test for Employee Service to Save Employee")
    @Test
    void saveEmployee() {
        given(repository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        when(repository.save(employee)).thenReturn(employee);

        Employee savedEmployee = employeeService.saveEmployee(employee);

        assertNotNull(savedEmployee);
        verify(repository, times(1)).save(any());
    }

    @DisplayName("Junit test for Employee Service to Save Employee with existing email")
    @Test
    void saveEmployeeThrowsException() {
        when(repository.findByEmail(employee.getEmail())).thenReturn(Optional.ofNullable(employee));

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });
        verify(repository, never()).save(any(Employee.class));
    }


    @DisplayName("Junit test for Employee Service to getAllEmployees method")
    @Test
    void getAllEmployee() {
        when(repository.findAll()).thenReturn(List.of(employee, employee2));

        List<Employee> allEmployees = employeeService.getAllEmployees();
        assertNotNull(allEmployees);
        assertEquals(2, allEmployees.size());
        verify(repository, times(1)).findAll();
    }

    @DisplayName("Junit test for Employee Service to getAllEmployees method with empty list")
    @Test
    void getAllEmployeeEmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Employee> allEmployees = employeeService.getAllEmployees();
        assertThat(allEmployees).isEmpty();
    }

    @DisplayName("Junit test for  getById method ")
    @Test
    void getById() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> employeeFound = employeeService.getEmployeeById(employee.getId());
        assertNotNull(employeeFound);
        verify(repository, times(1)).findById(anyLong());
    }

    @DisplayName("Junit test for updateEmployee method ")
    @Test
    void updateEmployee() {
        when(repository.save(employee)).thenReturn(employee);
        String newEmail = "update@update.com";
        String newName = "Baby";
        employee.setFirstName(newName);
        employee.setEmail(newEmail);
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        assertNotNull(updatedEmployee);
        assertEquals(newName, updatedEmployee.getFirstName());
        assertEquals(newEmail, updatedEmployee.getEmail());
    }

    @DisplayName("Junit test for deleteEmployee method ")
    @Test
    void deleteEmployee() {
        long emloyeeId = 1L;
        willDoNothing().given(repository).deleteById(emloyeeId);

        employeeService.deleteEmployee(emloyeeId);
        verify(repository, times(1)).deleteById(emloyeeId);

    }
}