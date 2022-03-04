package com.example.springboot.testing.tutorial.service;

import com.example.springboot.testing.tutorial.model.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {

    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Optional<Employee> getEmployeeById(Long id);

    Employee updateEmployee(Employee updatedEmployee);

    void deleteEmployee(Long id);
}
