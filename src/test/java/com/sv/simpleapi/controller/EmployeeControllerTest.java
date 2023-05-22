package com.sv.simpleapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sv.simpleapi.model.Employee;
import com.sv.simpleapi.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @MockBean
    EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    LocalDateTime localDateTime = LocalDateTime.of(1, Month.JANUARY, 12, 12, 0);



    @BeforeEach
    void setUp() {


        employee = Employee.builder()
                .id(1L)
                .name("Ivan")
                .lastName("Reyes")
                .email("ivan@email.com")
                .createdAt(localDateTime)
                .build();
    }

    @Test
    void getAllEmployees() {
    }

    @Test
    void getEmployee() {
    }

    @Test
    @DisplayName("POST /api/v1/employees")
    void createEmployee() throws Exception {

        given(employeeService.save(any()))
                .willAnswer((invocation -> invocation.getArgument(0)));

        employee.setId(null);

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("POST /api/v1/employees - Bad request")
    void createEmployeeFailed() throws Exception {

//        given(employeeService.save(any()))
//                .willAnswer((invocation -> invocation.getArgument(0)));


        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void deleteEmployee() {
    }

    @Test
    void updateEmployee() {
    }

    @Test
    void partialUpdateEmployee() {
    }
}