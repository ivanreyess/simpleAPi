package com.sv.simpleapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sv.simpleapi.DTO.EmployeeDTO;
import com.sv.simpleapi.model.Department;
import com.sv.simpleapi.model.Employee;
import com.sv.simpleapi.response.EmployeeResponse;
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
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    @DisplayName("GET /api/v1/employees")
    void getAllEmployees() throws Exception {
        Employee employee1 = Employee.builder()
                .id(2L)
                .name("Karen")
                .lastName("Recinos")
                .email("karen@email.com")
                .createdAt(localDateTime)
                .build();

        EmployeeResponse employeeResponse = EmployeeResponse
                .builder()
                .employees(Stream.of(employee, employee1).map(this::toEmployeeDTO).toList())
                .last(true)
                .pageNo(0)
                .pageSize(10)
                .totalPages(1)
                .totalElements(2)
                .build();

        given(employeeService.findAll(0, 10, "id", "asc")).willReturn(employeeResponse);

        ResultActions perform = mockMvc.perform(get("/api/v1/employees"));

        perform.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.employees[0].id", is(1)))
        .andExpect(jsonPath("$.employees[1].id", is(2)))
        .andExpect(jsonPath("$.employees[1].name", is("Karen")));

    }

    @Test
    @DisplayName("GET /api/v1/employees with id")
    void getEmployee() throws Exception {

        long employeeId = 1L;
        given(employeeService.findOne(employeeId)).willReturn(Optional.of(employee));

        mockMvc.perform(get("/api/v1/employees/{id}", employeeId))
                .andDo(print())
                .andExpect(status().isOk());
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
    @DisplayName("DELETE /api/v1/employee/{id} no content ")
    void deleteEmployee() throws Exception {
        doNothing().when(employeeService).delete(1L);

        mockMvc.perform(delete("/api/v1/employees/{id}", 1L))
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("PUT /api/v1/employee/{id} success ")
    void updateEmployee() throws Exception {

        employee.setDepartment(Department.DATA_ENGINEER);

        given(employeeService.save(any(Employee.class))).willAnswer((invocation -> invocation.getArgument(0)));
        given(employeeService.exist(employee.getId())).willReturn(true);


        mockMvc.perform(put("/api/v1/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee))
                ).andExpect(status().isOk());

    }

    @Test
    @DisplayName("PUT /api/v1/employee/{id} bad request ")
    void updateEmployeeFailed() throws Exception {

        employee.setDepartment(Department.DATA_ENGINEER);
        employee.setId(null);

        given(employeeService.save(any(Employee.class))).willAnswer((invocation -> invocation.getArgument(0)));
        given(employeeService.exist(employee.getId())).willReturn(true);


        mockMvc.perform(put("/api/v1/employees/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee))
        ).andExpect(status().isBadRequest());

    }

    @Test
    void partialUpdateEmployee() throws Exception {

        given(employeeService.partialUpdate(any(Employee.class))).willReturn(Optional.of(employee));

        given(employeeService.exist(1L)).willReturn(true);

        mockMvc.perform(patch("/api/v1/employees/{id}", 1L)
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk());

    }

    Employee toEmployee(EmployeeDTO employeeDTO) {
        return Employee.builder()
                .name(employeeDTO.name())
                .lastName(employeeDTO.lastName())
                .id(employeeDTO.id())
                .email(employeeDTO.email())
                .build();

    }

    EmployeeDTO toEmployeeDTO(Employee employee) {
        return EmployeeDTO.builder()
                .name(employee.getName())
                .lastName(employee.getLastName())
                .id(employee.getId())
                .email(employee.getEmail())
                .build();

    }

}