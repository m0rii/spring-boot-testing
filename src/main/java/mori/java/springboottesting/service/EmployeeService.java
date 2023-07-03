package mori.java.springboottesting.service;

import java.util.List;
import java.util.Optional;
import mori.java.springboottesting.model.Employee;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Optional<Employee> getEmployeeById(long id);

    Employee updateEmployee(Employee updatedEmployee);

    void deleteEmployee(long id);

}
