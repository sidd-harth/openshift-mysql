package com.example.employee.controller;

import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping("/info")
    public String info() {
        return "Hello, first make an POST call & then call GET, PUT, DELETE methods";
    }
    
    @GetMapping("/employees")
    public List<Employee> getAllemployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getemployeeById(@PathVariable(value = "id") Long employeeId) {
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employees")
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateemployee(@PathVariable(value = "id") Long employeeId,
                                           @Valid @RequestBody Employee employeeDetails) {
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee == null) {
            return ResponseEntity.notFound().build();
        }
        employee.setName(employeeDetails.getName());
        employee.setCity(employeeDetails.getCity());

        Employee updatedemployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedemployee);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Employee> deleteemployee(@PathVariable(value = "id") Long employeeId) {
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee == null) {
            return ResponseEntity.notFound().build();
        }

        employeeRepository.delete(employee);
        return ResponseEntity.ok().build();
    }
}
