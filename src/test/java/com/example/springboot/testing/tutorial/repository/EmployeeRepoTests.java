package com.example.springboot.testing.tutorial.repository;

import com.example.springboot.testing.tutorial.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepoTests {

    @Autowired
    private EmployeeRepo employeeRepo;

    private Employee employee;

    @BeforeEach
    public  void setupEmployee(){

       employee = Employee.builder()
                .firstName("Sihle")
                .lastName("Manaka")
                .email("sihlemanaka@gmail.com")
                .build();


    }

    @BeforeEach
    public void setupEmployee1(){
         employee = Employee.builder()
                .firstName("Jimbu")
                .lastName("Manaka")
                .email("jimbumanaka@gmail.com")
                .build();
    }

    //JUnit test for save employee operation
    @DisplayName("saveEmployeeTest")
    @Test
    public void givenEmployeeObj_whenSave_thenReturnSavedEmployee(){

        //given - precondition or setup
        /*Employee employee = Employee.builder()
                .firstName("Sihle")
                .lastName("Manaka")
                .email("sihlemanaka@gmail.com")
                .build();
        */
        setupEmployee();

        //when - action or behaviour to be tested
        Employee savedEmployee = employeeRepo.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0); //once saved ID should be greater then 1
    }


    //JUnit test for retrieving all employees
    @DisplayName("getEmployeesListTest")
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList(){

        //given - precondition or setup
        setupEmployee();

        Employee employee1 = Employee.builder()
                .firstName("Zinhle")
                .lastName("Manaka")
                .email("zinhlemanaka@gmail.com")
                .build();

        employeeRepo.save(employee);
        employeeRepo.save(employee1);

        //when - action or behaviour to be tested
        List<Employee> employeeList = employeeRepo.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }

    //JUnit test for retrieving employee by ID
    @DisplayName("findByIdTest")
    @Test
    public void givenEmployeeObj_whenFindById_thenReturnEmployeeObj(){

        //given - precondition or setup
        setupEmployee();

        employeeRepo.save(employee);

        //when - action or behaviour to be tested
        Employee employeeInDb = employeeRepo.findById(employee.getId()).get();

        //then - verify the output
        assertThat(employeeInDb).isNotNull();
        assertThat(employeeInDb.getId()).isGreaterThan(0);
    }

    //JUnit test for retrieving employee by email
    @DisplayName("findByEmailTest")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployee(){

        //given - precondition or setup
        setupEmployee1();

        employeeRepo.save(employee);

        //when - action or behaviour to be tested
        Employee employeeDB = employeeRepo.findByEmail(employee.getEmail()).get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    //JUnit test for employee update
    @DisplayName("updateEmployeeTest")
    @Test
    public void givenEmployeeObj_whenUpdateEmployee_thenReturnUpdatedEmployeeObj(){

        //given - precondition or setup
        setupEmployee1();

        employeeRepo.save(employee);

        //when - action or behaviour to be tested
        //first retrieve saved employee
        Employee savedEmployee = employeeRepo.findById(employee.getId()).get();

        savedEmployee.setEmail("tatos@hotmail.com");
        Employee updatedEmployee = employeeRepo.save(savedEmployee);

        //then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("tatos@hotmail.com");

    }

    //JUnit test for delete
    @DisplayName("deleteEmployeeTest")
    @Test
    public void givenEmployee_whenDeleting_thenRemoveEmployee(){

        //given - precondition or setup
        setupEmployee1();

        employeeRepo.save(employee);

        //when - action or behaviour to be tested
        //employeeRepo.delete(employee);
        employeeRepo.deleteById(employee.getId());

        Optional<Employee> employeeOptional = employeeRepo.findById(employee.getId());

        //then - verify the output
        assertThat(employeeOptional).isEmpty();

    }

    //JUnit test for custom query using JPQL with param index
    @Test
    @DisplayName("saveCustomJPQLTest")
    public void givenFirstAndLastName_whenFindByJPQL_thenReturnEmployeeObj(){

        //given - precondition or setup
        setupEmployee1();

        employeeRepo.save(employee);

        String firstName = "Jimbu";
        String lastName = "Manaka";

        //when - action or behaviour to be tested
        Employee savedEmployee = employeeRepo.findByJPQL(firstName,lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for custom query using JPQL with named params
    @Test
    @DisplayName("saveCustomJPQLNamedParamTest")
    public void givenFirstAndLastName_whenFindByJPQLNamedParam_thenReturnEmployeeObj(){

        //given - precondition or setup
        setupEmployee1();

        employeeRepo.save(employee);

        String firstName = "Jimbu";
        String lastName = "Manaka";

        //when - action or behaviour to be tested
        Employee savedEmployee = employeeRepo.findByJPQLNamedParams(firstName,lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for custom query using native sql using index  para
    @Test
    @DisplayName("findByNativeQuery")
    public void givenFirstAndLastName_whenFindByNativeSQL_thenReturnEmployee(){

        //given - precondition or setup
        setupEmployee1();

        employeeRepo.save(employee);


        //when - action or behaviour to be tested
        Employee SavedEmployee = employeeRepo.findByNativeSQL(employee.getFirstName(),employee.getLastName());

        //then - verify the output
        assertThat(SavedEmployee).isNotNull();

    }

    //JUnit test for custom query using native sql using named  params
    @Test
    @DisplayName("findByNativeQueryNamedParams")
    public void givenFirstAndLastName_whenFindByNativeSQLNamed_thenReturnEmployee(){

        //given - precondition or setup
        setupEmployee1();

        employeeRepo.save(employee);


        //when - action or behaviour to be tested
        Employee SavedEmployee = employeeRepo.findByNativeSQLNamed(employee.getFirstName(),employee.getLastName());

        //then - verify the output
        assertThat(SavedEmployee).isNotNull();

    }

}
