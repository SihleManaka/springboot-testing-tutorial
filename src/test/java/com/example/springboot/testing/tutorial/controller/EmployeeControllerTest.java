package com.example.springboot.testing.tutorial.controller;

import com.example.springboot.testing.tutorial.model.Employee;
import com.example.springboot.testing.tutorial.service.implentation.EmployeeServiceImplemantation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeServiceImplemantation employeeServiceImplemantation;

    @Autowired
    private ObjectMapper objectMapper;

    //JUnit test for employee post rest endpoint
    @Test
    @DisplayName("controllerCreateEmployeeTest")
    public void givenEmployeeObj_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Sihle")
                .lastName("Manaka")
                .email("jay@gmail.com")
                .build();

        //BDDMockito.given(employeeServiceImplemantation.saveEmployee(employee)).willReturn(employee);
        BDDMockito.given(employeeServiceImplemantation.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation)->invocation.getArgument(0));

        //when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }

    //JUnit test for get all employees REST API
    @Test
    @DisplayName("controllerGetAllEmployeeTest")
    public void givenListOfEmployees_whenGetAll_thenEmployeeList() throws Exception {

        //given - precondition or setup
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder()
                .firstName("Sihle")
                .lastName("Manaka")
                .email("jay@gmail.com")
                .build());
        employeeList.add(Employee.builder()
                .firstName("Zinhle")
                .lastName("Manaka")
                .email("zinhle@gmail.com")
                .build());

        BDDMockito.given(employeeServiceImplemantation.getAllEmployees()).willReturn(employeeList);

        //when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk()) //we checking if the status is 200/ok
                .andDo(MockMvcResultHandlers.print()) //to print the response
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",CoreMatchers.is(employeeList.size())));
                //we verifying the size of the array
    }

    //JUnit positive test for get employee by ID REST API
    @Test
    @DisplayName("controllerGetEmployeeByIdTest")
    public void givenEmployeeId_whenGetEmployeeID_thenReturnEmployee() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Sihle")
                .lastName("Manaka")
                .email("jay@gmail.com")
                .build();

        BDDMockito.given(employeeServiceImplemantation.getEmployeeById(employeeId)).willReturn(Optional.of(employee));


        //when - action or behaviour to be tested
        ResultActions response =  mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}",employeeId));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.firstName", Matchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.lastName", Matchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.email", Matchers.is(employee.getEmail())));

    }

    //JUnit Nagative test for get employee by ID REST API
    @Test
    @DisplayName("controllerGetEmployeeByIdNegativeTest")
    public void givenInvalidEmployeeId_whenGetEmployeeID_thenReturnEmpty() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Sihle")
                .lastName("Manaka")
                .email("jay@gmail.com")
                .build();

        BDDMockito.given(employeeServiceImplemantation.getEmployeeById(employeeId)).willReturn(Optional.empty());


        //when - action or behaviour to be tested
        ResultActions response =  mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}",employeeId));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    //JUnit test for update REST API - positive scenario
    @Test
    @DisplayName("controllerUpdateEmployeeTest")
    public void givenUpdateEmployee_whenUpdating_thenReturnUpdatedEmployee() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Sihle")
                .lastName("Manaka")
                .email("jay@gmail.com")
                .build();

        //simulate update
        Employee updatedEmployee = Employee.builder()
                .firstName("Zinhle")
                .lastName("Manaka")
                .email("Zinhle@gmail.com")
                .build();

        //stub get by id
        BDDMockito.given(employeeServiceImplemantation.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));

        BDDMockito.given(employeeServiceImplemantation.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation)->invocation.getArgument(0));

        //when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.firstName", Matchers.is(updatedEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.lastName", Matchers.is(updatedEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.email", Matchers.is(updatedEmployee.getEmail())));

    }

    //JUnit test for update REST API - negative scenario
    @Test
    @DisplayName("controllerUpdateEmployeeNegativeTest")
    public void givenUpdateEmployee_whenUpdating_thenReturn404() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Sihle")
                .lastName("Manaka")
                .email("jay@gmail.com")
                .build();

        //simulate update
        Employee updatedEmployee = Employee.builder()
                .firstName("Zinhle")
                .lastName("Manaka")
                .email("Zinhle@gmail.com")
                .build();

        //stub get by id
        BDDMockito.given(employeeServiceImplemantation.getEmployeeById(employeeId)).willReturn(Optional.empty());

        BDDMockito.given(employeeServiceImplemantation.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation)->invocation.getArgument(0));

        //when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    //JUnit test for delete employee REST API
    @Test
    @DisplayName("controllerDeleteEmployeeTest")
    public void givenEmployeeID_whenDelete_thenReturn200() throws Exception {

        //given - precondition or setup
        long employeeID = 1L;
        BDDMockito.willDoNothing().given(employeeServiceImplemantation).deleteEmployee(employeeID);

        //when - action or behaviour to be tested
        ResultActions resp = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}",employeeID));

        //then - verify the output
        resp.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
}
