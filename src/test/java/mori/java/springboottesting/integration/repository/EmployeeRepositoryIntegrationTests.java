package mori.java.springboottesting.integration.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mori.java.springboottesting.model.Employee;
import mori.java.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryIntegrationTests {
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
        employee = Employee.builder()
                .firstName("Mori")
                .lastName("Java")
                .email("Mori@Mori.mo")
                .build();
    }

    @DisplayName("Integration test for save employee operation")
    @Test
    void givenEmployeeObject_whenSave_thenReturnSavedEmployees() {

        //when
        Employee savedEmployee = employeeRepository.save(employee);

        //then
        assertNotNull(savedEmployee);
        assertThat(savedEmployee.getId()).isPositive();
        assertEquals("Mori", savedEmployee.getFirstName());
        assertEquals(employee.getLastName(), savedEmployee.getLastName());
    }


    @DisplayName("Integration test for get all employee by findAll operation")
    @Test
    void givenEmployeeList_whenFindAll_thenReturnEmployeesList() {
        //given
        Employee employee2 = Employee.builder()
                .firstName("Behi")
                .lastName("Java")
                .email("Behi@Behi.bi")
                .build();
        Employee employee3 = Employee.builder()
                .firstName("LopLop")
                .lastName("Java")
                .email("Lop@lop.lop")
                .build();

        List<Employee> employeesList = new ArrayList<Employee>(List.of(employee, employee2, employee3));

        employeeRepository.saveAll(employeesList);

        //when
        List<Employee> allEmployees = employeeRepository.findAll();

        //then
        assertNotNull(allEmployees);
        assertEquals(3, allEmployees.size());
    }

    @DisplayName("Integration test for get employee by id operation")
    @Test
    void givenEmployeeObjext_whenGetById_thenReturnEmployeeFound() {
        //given
        employeeRepository.save(employee);

        //when
        Employee employeeFound = employeeRepository.findById(employee.getId()).get();

        //then
        assertNotNull(employeeFound);
        assertEquals(employee.getId(), employeeFound.getId());
    }

    @DisplayName("Integration test for get employee by email operation")
    @Test
    void givenEmployeeEmail_whenfindByEmail_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee);

        //when
        Employee employeeFound = employeeRepository.findByEmail(employee.getEmail()).get();

        //then
        assertNotNull(employeeFound);
        assertEquals(employee.getEmail(), employeeFound.getEmail());
    }

    @DisplayName("Integration test for update employee operation")
    @Test
    void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given

        employeeRepository.save(employee);

        //when
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();

        savedEmployee.setFirstName("Loplop");
        savedEmployee.setEmail("Loplop@Mori.com");

        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then
        assertNotNull(employee);
        assertNotNull(savedEmployee);
        assertNotNull(updatedEmployee);
        assertThat(updatedEmployee.getEmail()).isEqualTo("Loplop@Mori.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Loplop");
    }


    @DisplayName("Integration test for delete employee operation")
    @Test
    void givenEmployeeObject_whenDeleteEmployee_thenRemoveEmployee() {
        //given

        employeeRepository.save(employee);

        //when
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());


        //then
        assertThat(employeeRepository.findAll().size()).isZero();
        assertThat(employeeOptional).isEmpty();

    }

    @DisplayName("Integration test for custom query using JPQl with index ")
    @Test
    void givenFirstNameAndLastName_whenFindByJPQL_thenEmployeeObject() {
        //given
        employeeRepository.save(employee);

        String firstName = "Mori";
        String lasttName = "Java";

        //when
        Employee employeeDb = employeeRepository.findByJPQL(firstName, lasttName);

        //then
        assertNotNull(employeeDb);
        assertEquals(employeeDb.getFirstName(), employee.getFirstName());
        assertEquals(employeeDb.getLastName(), employee.getLastName());
        assertEquals(employeeDb.getEmail(), employee.getEmail());

    }

    @DisplayName("Integration test for custom query using JPQl with Named params ")
    @Test
    void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenEmployeeObject() {
        //given
        employeeRepository.save(employee);

        String firstName = "Mori";
        String lasttName = "Java";

        //when
        Employee employeeDb = employeeRepository.findByJPQLNamedParams(firstName, lasttName);

        //then
        assertNotNull(employeeDb);
        assertEquals(employeeDb.getFirstName(), employee.getFirstName());
        assertEquals(employeeDb.getLastName(), employee.getLastName());
        assertEquals(employeeDb.getEmail(), employee.getEmail());

    }

    @DisplayName("Integration test for custom query using Native SQL index param  ")
    @Test
    void givenFirstNameAndLastName_whenFindByNativeSql_thenEmployeeObject() {
        //given

        employeeRepository.save(employee);

        String firstName = "Mori";
        String lasttName = "Java";

        //when
        Employee employeeDb = employeeRepository.findByNativeSql(firstName, lasttName);

        //then
        assertNotNull(employeeDb);
        assertEquals(employeeDb.getFirstName(), employee.getFirstName());
        assertEquals(employeeDb.getLastName(), employee.getLastName());
        assertEquals(employeeDb.getEmail(), employee.getEmail());
    }

    @DisplayName("Integration test for custom query using Native Sql with Named params ")
    @Test
    void givenFirstNameAndLastName_whenFindByByNativeSqlNamedParams_thenEmployeeObject() {
        //given
        employeeRepository.save(employee);

        String firstName = "Mori";
        String lasttName = "Java";

        //when
        Employee employeeDb = employeeRepository.findByNativeSqlNamedParams(firstName, lasttName);

        //then
        assertNotNull(employeeDb);
        assertEquals(employeeDb.getFirstName(), employee.getFirstName());
        assertEquals(employeeDb.getLastName(), employee.getLastName());
        assertEquals(employeeDb.getEmail(), employee.getEmail());

    }
}
