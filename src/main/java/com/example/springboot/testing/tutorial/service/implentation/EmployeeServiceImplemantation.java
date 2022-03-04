package com.example.springboot.testing.tutorial.service.implentation;

import com.example.springboot.testing.tutorial.exception.ResourceNotFoundException;
import com.example.springboot.testing.tutorial.model.Employee;
import com.example.springboot.testing.tutorial.repository.EmployeeRepo;
import com.example.springboot.testing.tutorial.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImplemantation implements IEmployeeService {

    //@Autowired
    private EmployeeRepo employeeRepo;

    public EmployeeServiceImplemantation(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepo.findByEmail(employee.getEmail());

        if(savedEmployee.isPresent()){
            throw  new ResourceNotFoundException("Employee already exist with email: " + employee.getEmail());
        }

        return employeeRepo.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {

        return employeeRepo.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {

        return employeeRepo.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {

        return employeeRepo.save(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {

        employeeRepo.deleteById(id);
    }
}
