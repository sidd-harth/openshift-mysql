package com.example.employee.controller;

import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

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
        return "Hello, MySql DB has been populated with 1 Million records with id's from 10 to 1000009";
    }
    
    @GetMapping("/employees")
    public List<Employee> getAllemployees() {
        return employeeRepository.findAll();
    }

    
    /** 
   original code for getting specific record
   
    @GetMapping("/employees/{id}")
   // @HystrixCommand(fallbackMethod = "getDataFallBack")
    public ResponseEntity<Employee> getemployeeById(@PathVariable(value = "id") Long employeeId) {
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(employee);
    } **/
    
    @GetMapping("/employees/{id}")
    @HystrixCommand(fallbackMethod = "getDataFallBack")
    public ResponseEntity<Employee> getemployeeById(@PathVariable(value = "id") Long employeeId) {
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee == null) {
            return ResponseEntity.notFound().build();
        }
        
       // String response1 = ResponseEntity.ok().body(employee);
        System.out.println("response of db " + ResponseEntity.ok().body(employee));
        
        
        String baseUrl = "http://browser-service-nodejs-mongdb.7e14.starter-us-west-2.openshiftapps.com/specific/"+employeeId;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response=null;
		try{
		response=restTemplate.exchange(baseUrl,
				HttpMethod.GET, getHeaders(),String.class);
		}catch (Exception ex)
		{
			System.out.println(ex);
		}
		System.out.println(response.getBody());
		
        return("[" + response.getBody() + "," + ResponseEntity.ok().body(employee) + "]");
        
        //return ResponseEntity.ok().body(employee);
    }
public ResponseEntity<Employee> getDataFallBack(@PathVariable(value = "id") Long employeeId) {
       // Employee employee = employeeRepository.findOne(employeeId);
      //  if(employee == null) {
     //      return ResponseEntity.notFound().build();
      //  }
        return ("okkkkkkkkk");
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
