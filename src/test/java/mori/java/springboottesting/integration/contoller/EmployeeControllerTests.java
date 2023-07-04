package mori.java.springboottesting.integration.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import mori.java.springboottesting.model.Employee;
import mori.java.springboottesting.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @DisplayName("Integration test for Create Employee REST API")
    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        Employee employee = Employee.builder().firstName("Mori").lastName("Java").email("Mori@Java.com").build();

        //when
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employee)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }


    @DisplayName("Integration test for GetAll Employee REST API")
    @Test
    void givenGetAll_whenGetAllEmployee_thenListAllEmployee() throws Exception {
        //given
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Mori").lastName("Java").email("Mori@Java.com").build());
        listOfEmployees.add(Employee.builder().firstName("Behi").lastName("Java").email("Behi@Java.com").build());
        repository.saveAll(listOfEmployees);


        //when
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(listOfEmployees.size())));
    }

    @DisplayName("Integration test for GetEmployeeById REST API")
    @Test
    void givenEmloyeeId_whenGetEmployeeById_thenEmployeeObject() throws Exception {
        //given
        Employee employee = Employee.builder().firstName("Mori").lastName("Java").email("Mori@Java.com").build();
        repository.save(employee);

        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        //then

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())));
    }

    @DisplayName("Integration test for Negative Senario GetEmployeeById REST API")
    @Test
    void givenEmloyeeId_whenGetEmployeeById_thenEmpty() throws Exception {
        //given
        long emloyeeId = 1L;
        Employee employee = Employee.builder().firstName("Mori").lastName("Java").email("Mori@Java.com").build();
        repository.save(employee);

        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", emloyeeId));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Junit test for Update Employee REST API")
    @Test
    void givenEmloyeeId_whenUpdateEmloyee_thenUpdatedEmployeeObject() throws Exception {
        //given
        Employee savedEmployee = Employee.builder().firstName("Mori").lastName("Java").email("Mori@Java.com").build();
        repository.save(savedEmployee);
        Employee updatedEmployee = Employee.builder().firstName("LALA").lastName("HAHA").email("LALA@HAHA.com").build();

        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedEmployee)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(updatedEmployee.getEmail())));
    }

    @DisplayName("Interation test for Negative senario Update Employee REST API")
    @Test
    void givenEmloyeeId_whenUpdateEmloyee_thenEmpty() throws Exception {
        //given
        long emloyeeId = 1L;
        Employee savedEmployee = Employee.builder().firstName("Mori").lastName("Java").email("Mori@Java.com").build();
        repository.save(savedEmployee);
        Employee updatedEmployee = Employee.builder().firstName("LALA").lastName("HAHA").email("LALA@HAHA.com").build();

        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", emloyeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedEmployee)));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Junit test for DeleteEmployee REST API")
    @Test
    void givenEmloyeeId_whenDeleteEmloyee_thenOK() throws Exception {
        //given
        Employee savedEmployee = Employee.builder().firstName("Mori").lastName("Java").email("Mori@Java.com").build();
        repository.save(savedEmployee);
        //when
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        //then
        response.andDo(print())
                .andExpect(status().isOk());
    }

}
