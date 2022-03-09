package com.example.springboot.testing.tutorial.integration;

import com.example.springboot.testing.tutorial.model.Employee;
import com.example.springboot.testing.tutorial.repository.EmployeeRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(){
        employeeRepo.deleteAll();
    }

    //JUnit test for employee post rest endpoint
    @Test
    @DisplayName("controllerCreateEmployeeIntegrationTest")
    public void givenEmployeeObj_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Sihle")
                .lastName("Manaka")
                .email("jay@gmail.com")
                .build();

        //when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
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
    @DisplayName("controllerGetAllEmployeeIntegrationTest")
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

        //save employees to db
        employeeRepo.saveAll(employeeList);

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
    @DisplayName("controllerGetEmployeeByIdIntegrationTest")
    public void givenEmployeeId_whenGetEmployeeID_thenReturnEmployee() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Sihle")
                .lastName("Manaka")
                .email("jay@gmail.com")
                .build();


        //save employee
        employeeRepo.save(employee);

        //when - action or behaviour to be tested
        ResultActions response =  mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}",employee.getId()));

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

    //JUnit Negative test for get employee by ID REST API
    @Test
    @DisplayName("controllerGetEmployeeByIdNegativeIntegrationTest")
    public void givenInvalidEmployeeId_whenGetEmployeeID_thenReturnEmpty() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Sihle")
                .lastName("Manaka")
                .email("jay@gmail.com")
                .build();
        //save employee
        employeeRepo.save(employee);
        //when - action or behaviour to be tested
        // employeeId 1L does not exist in DB
        ResultActions response =  mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}",employeeId));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    //JUnit test for update REST API - positive scenario
    @Test
    @DisplayName("controllerUpdateEmployeeIntegrationTest")
    public void givenUpdateEmployee_whenUpdating_thenReturnUpdatedEmployee() throws Exception {

        //given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Sihle")
                .lastName("Manaka")
                .email("jay@gmail.com")
                .build();

        //save employee
        employeeRepo.save(savedEmployee);

        //simulate update
        Employee updatedEmployee = Employee.builder()
                .firstName("Zinhle")
                .lastName("Manaka")
                .email("Zinhle@gmail.com")
                .build();

        //when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}",savedEmployee.getId())
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
    @DisplayName("controllerUpdateEmployeeNegativeIntegrationTest")
    public void givenUpdateEmployee_whenUpdating_thenReturn404() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Sihle")
                .lastName("Manaka")
                .email("jay@gmail.com")
                .build();

        //save employee
        employeeRepo.save(savedEmployee);

        //simulate update
        Employee updatedEmployee = Employee.builder()
                .firstName("Zinhle")
                .lastName("Manaka")
                .email("Zinhle@gmail.com")
                .build();

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
    @DisplayName("controllerDeleteEmployeeIntegrationTest")
    public void givenEmployeeID_whenDelete_thenReturn200() throws Exception {

        //given - precondition or setup
        //save employee first
        Employee savedEmployee = Employee.builder()
                .firstName("Sihle")
                .lastName("Manaka")
                .email("jay@gmail.com")
                .build();

        employeeRepo.save(savedEmployee);

        //when - action or behaviour to be tested
        ResultActions resp = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}",savedEmployee.getId()));

        //then - verify the output
        resp.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
}
