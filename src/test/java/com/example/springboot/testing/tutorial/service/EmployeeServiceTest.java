package com.example.springboot.testing.tutorial.service;

import com.example.springboot.testing.tutorial.exception.ResourceNotFoundException;
import com.example.springboot.testing.tutorial.model.Employee;
import com.example.springboot.testing.tutorial.repository.EmployeeRepo;
import com.example.springboot.testing.tutorial.service.implentation.EmployeeServiceImplemantation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepo employeeRepo;

    @InjectMocks
    private EmployeeServiceImplemantation employeeServiceImplemantation;

    private Employee employee;


    @BeforeEach
    public  void setup(){

        //employeeRepo = Mockito.mock(EmployeeRepo.class);
        //employeeServiceImplemantation = new EmployeeServiceImplemantation(employeeRepo);

         employee = Employee.builder()
                .id(1L)
                .firstName("Sihle")
                .lastName("Manaka")
                .email("sihlemanaka@gmail.com")
                .build();

    }

    //JUnit test for save employee method
    @Test
    @DisplayName("ServiceTestSaveEmployee")
    public void givenEmployeeObj_whenSave_thenReturnEmployeeObj(){

        //given - precondition or setup
        setup();

        //stubbing findBy method
        BDDMockito.given(employeeRepo.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        System.out.println("Repo: " + employeeRepo);
        System.out.println("employeeServiceImplemantation: " + employeeServiceImplemantation);
        //stubbing save method from service
        BDDMockito.given(employeeRepo.save(employee)).willReturn(employee);

        //when - action or behaviour to be tested
        Employee savedEmployee = employeeServiceImplemantation.saveEmployee(employee);
        System.out.println("savedEmployee: " + savedEmployee.toString());
        //then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for save employee method which throws exception
    @Test
    @DisplayName("ServiceTestSaveEmployeeExistEmailException")
    public void givenExistingEmail_whenSaveEmployee_thenThrowException(){

        //given - precondition or setup
        setup();

        //stubbing findBy method
        BDDMockito.given(employeeRepo.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when - action or behaviour to be tested
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,() ->{
            employeeServiceImplemantation.saveEmployee(employee);
        });

        //then - verify the output
        Mockito.verify(employeeRepo,Mockito.never()).save(Mockito.any(Employee.class)); // after throwing exception we verfiying that our method does not save
    }

    //JUnit test for get all employees
    @Test
    @DisplayName("serviceTestGetAllEmployees")
    public void givenEmployeesList_whenRetrieving_thenReturnsList(){

        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Zinhle")
                .lastName("Manaka")
                .email("Zinhlemanaka@gmail.com")
                .build();

        BDDMockito.given(employeeRepo.findAll()).willReturn(List.of(employee,employee1));

        //when - action or behaviour to be tested
        List<Employee> employeeList = employeeServiceImplemantation.getAllEmployees();

        //then - verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isGreaterThan(1);
    }

    //JUnit test for get all empty list
    @Test
    @DisplayName("serviceTestNegativeGetAllEmployees")
    public void givenEmptyEmployeesList_whenRetrieving_thenReturnsEmptyList(){

        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Zinhle")
                .lastName("Manaka")
                .email("Zinhlemanaka@gmail.com")
                .build();

        BDDMockito.given(employeeRepo.findAll()).willReturn(Collections.emptyList()); //returning an empty list

        //when - action or behaviour to be tested
        List<Employee> employeeList = employeeServiceImplemantation.getAllEmployees();

        //then - verify the output
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }

    //JUnit test for get employee by ID
    @Test
    @DisplayName("serviceTestGetEmployeeByID")
    public void givenEmployeeID_whenRetrieving_thenEmployee(){

        //given - precondition or setup
        BDDMockito.given(employeeRepo.findById(1L)).willReturn(Optional.of(employee));

        //when - action or behaviour to be tested
        Employee savedEmployee = employeeServiceImplemantation.getEmployeeById(employee.getId()).get();

        //then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for Employee update
    @Test
    @DisplayName("serviceTestUpdateEmployee")
    public void givenEmployee_whenUpdating_thenUpdatedEmployee(){

        //given - precondition or setup

        BDDMockito.given(employeeRepo.save(employee)).willReturn(employee);
        //update employee
        employee.setFirstName("Tatos");
        employee.setEmail("tatos@stout.com");

        //when - action or behaviour to be tested
        Employee updatedEmployee = employeeServiceImplemantation.updateEmployee(employee);

        //then - verify the output
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Tatos");
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("tatos@stout.com");
    }

    //JUnit test for delete employee
    @Test
    @DisplayName("serviceTestDeleteEmployee")
    public void givenEmployeeId_whenDelete_thenVoid(){

        //given - precondition or setup
        BDDMockito.willDoNothing().given(employeeRepo).deleteById(1L); //for void stubbing we must use BDDMockito.willDoNothing()
        //when - action or behaviour to be tested
        employeeServiceImplemantation.deleteEmployee(1L);

        //then - verify the output
        //we can verify that deleteById only gets called 1
        Mockito.verify(employeeRepo,Mockito.times(1)).deleteById(1L);

    }


}
