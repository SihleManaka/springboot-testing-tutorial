package com.example.springboot.testing.tutorial.controller;

import com.example.springboot.testing.tutorial.model.Employee;
import com.example.springboot.testing.tutorial.service.implentation.EmployeeServiceImplemantation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeServiceImplemantation employeeServiceImplemantation;

    //using constructor base injection
    public EmployeeController(EmployeeServiceImplemantation employeeServiceImplemantation) {
        this.employeeServiceImplemantation = employeeServiceImplemantation;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee){

        return employeeServiceImplemantation.saveEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees(){

        return employeeServiceImplemantation.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id){ //ResponseEntity<Employee>

        return  employeeServiceImplemantation.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}")
    public  ResponseEntity<Employee> updateEmployee(@PathVariable("id") long employeeId,
                                                    @RequestBody Employee employee){

        return employeeServiceImplemantation.getEmployeeById(employeeId)
                .map(savedEmployee ->{
                    savedEmployee.setFirstName(employee.getFirstName());
                    savedEmployee.setLastName(employee.getLastName());
                    savedEmployee.setEmail(employee.getEmail());

                    Employee updatedEmployee = employeeServiceImplemantation.updateEmployee(savedEmployee);
                    return new ResponseEntity<Employee>(updatedEmployee, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteEmployee(@PathVariable("id") long employeeID){

        employeeServiceImplemantation.deleteEmployee(employeeID);

        return new ResponseEntity<String>("Employee deleted ",HttpStatus.OK);

    }
}
