package com.sv.simpleapi.service;

import com.sv.simpleapi.DTO.EmployeeDTO;
import com.sv.simpleapi.EmployeeRepository;
import com.sv.simpleapi.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;


    EmployeeDTO employee;

    LocalDateTime localDateTime = LocalDateTime.of(1, Month.JANUARY, 12, 12, 0);



    @BeforeEach
    void setUp() {
        employee = EmployeeDTO.builder()
                .id(1L)
                .name("Ivan")
                .lastName("Reyes")
                .email("ivan@email.com")
                .createdAt(localDateTime)
                .build();
    }

    @Test
    void findAll() {


        EmployeeDTO employee1 = EmployeeDTO.builder()
                .id(2L)
                .name("Maria")
                .lastName("Suarez")
                .email("maria@email.com")
                .createdAt(localDateTime)
                .build();

        //given
        given(employeeRepository.findAll()).willReturn(Stream.of(employee, employee1).map(this::toEmployee).toList());

        //when
        List<EmployeeDTO> result = employeeService.findAll();


        //then
        assertTrue(result.size() > 0);
        assertEquals(2, result.size());

    }

    @Test
    void save() {

        Employee savedEmployee = Optional.of(employee).map(this::toEmployee).get();

        given(employeeRepository.save(savedEmployee)).willReturn(savedEmployee);

        Employee result= employeeService.save(savedEmployee);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findOne() {



    }

    @Test
    void testSave() {
    }

    @Test
    void delete() {
    }

    @Test
    void exist() {
    }

    @Test
    void partialUpdate() {
    }

    @Test
    void testFindAll() {
    }

    public Employee toEmployee(EmployeeDTO employeeDTO) {
        return Employee.builder()
                .id(employeeDTO.id())
                .name(employeeDTO.name())
                .lastName(employeeDTO.lastName())
                .email(employeeDTO.email())
                .createdAt(employeeDTO.createdAt())
                .build();
    }

    public EmployeeDTO toEmployeeDto(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .createdAt(employee.getCreatedAt())
                .build();
    }

}