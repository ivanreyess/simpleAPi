package com.sv.simpleapi.controller;

import com.sv.simpleapi.model.Employee;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeRestTemplateTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    LocalDateTime localDateTime = LocalDateTime.of(1, Month.JANUARY, 12, 12, 0);

    @Test
    @Order(1)
    void testSaveEmployee() {

        Employee employee = Employee.builder().name("Simon").lastName("Ramirez").email("simon@email.com").createdAt(localDateTime).build();

        ResponseEntity<Employee> employeeResponse = testRestTemplate.postForEntity("http://localhost:8080/api/v1/employees", employee, Employee.class);
        assertNotNull(employeeResponse);
        assertEquals(HttpStatus.CREATED, employeeResponse.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, employeeResponse.getHeaders().getContentType());

        Employee createdEmployee = employeeResponse.getBody();

        assertNotNull(createdEmployee);
        assertEquals("Simon", createdEmployee.getName());
    }

    @Test
    @Order(1)
    void getEmployees() {
        ResponseEntity<Employee[]> employeesResponse = testRestTemplate.getForEntity("http://localhost:8080/api/v1/employees", Employee[].class);

        assertNotNull(employeesResponse);
        assertNotNull(employeesResponse.getBody());
        List<Employee> employees = Arrays.asList(employeesResponse.getBody());

        assertEquals(1, employees.size());
    }


}
