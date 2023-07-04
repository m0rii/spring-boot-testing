package mori.java.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mori.java.springboottesting.model.Employee;
import mori.java.springboottesting.service.impl.EmployeeServiceImpl;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

;


@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeServiceImpl employeeService;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("Junit test for Create Employee REST API")
    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        Employee employee = Employee.builder().firstName("Mori").lastName("Java").email("Mori@Java.com").build();

/*        given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
        .willAnswer((inocation) -> inocation.getArgument(0));*/

        when(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class))).then(returnsFirstArg());

        //when
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employee)));
        ;

        //then

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @DisplayName("Junit test for GetAll Employee REST API")
    @Test
    void givenGetAll_whenGetAllEmployee_thenListAllEmployee() throws Exception {
        //given
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Mori").lastName("Java").email("Mori@Java.com").build());
        listOfEmployees.add(Employee.builder().firstName("Behi").lastName("Java").email("Behi@Java.com").build());


        when(employeeService.getAllEmployees()).thenReturn(listOfEmployees);

        //when
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(listOfEmployees.size())));
    }

    @DisplayName("Junit test for GetEmployeeById REST API")
    @Test
    void givenEmloyeeId_whenGetEmployeeById_thenEmployeeObject() throws Exception {
        //given
        long employeeId = 1L;
        Employee employee = Employee.builder().id(employeeId).firstName("Mori").lastName("Java").email("Mori@Java.com").build();


        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(employee));


        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
        ;
    }

    @DisplayName("Junit test for Negative Senario GetEmployeeById REST API")
    @Test
    void givenEmloyeeId_whenGetEmployeeById_thenEmpty() throws Exception {
        //given
        long employeeId = 2L;
        Employee employee = Employee.builder().id(employeeId).firstName("Mori").lastName("Java").email("Mori@Java.com").build();

        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());

        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound());
        ;
    }

    @DisplayName("Junit test for Update Employee REST API")
    @Test
    void givenEmloyeeId_whenUpdateEmloyee_thenUpdatedEmployeeObject() throws Exception {
        //given
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder().id(employeeId).firstName("Mori").lastName("Java").email("Mori@Java.com").build();
        Employee updatedEmployee = Employee.builder().id(employeeId).firstName("LALA").lastName("HAHA").email("LALA@HAHA.com").build();

        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(savedEmployee));
        when(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class))).then(returnsFirstArg());
        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedEmployee)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(updatedEmployee.getEmail())));
        ;
    }

    @DisplayName("Junit test for Negative senario Update Employee REST API")
    @Test
    void givenEmloyeeId_whenUpdateEmloyee_thenEmpty() throws Exception {
        //given
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder().id(employeeId).firstName("Mori").lastName("Java").email("Mori@Java.com").build();
        Employee updatedEmployee = Employee.builder().id(employeeId).firstName("LALA").lastName("HAHA").email("LALA@HAHA.com").build();

        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());
        when(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class))).then(returnsFirstArg());
        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
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
        long employeeId = 1L;
        doNothing().when(employeeService).deleteEmployee(employeeId);
        //when
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        //then
        response.andDo(print())
                .andExpect(status().isOk());
    }


}
