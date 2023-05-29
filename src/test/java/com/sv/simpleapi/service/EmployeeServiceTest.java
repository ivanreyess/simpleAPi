package com.sv.simpleapi.service;

import com.sv.simpleapi.DTO.EmployeeDTO;
import com.sv.simpleapi.repository.EmployeeRepository;
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

//        //when
//        List<EmployeeDTO> result = employeeService.findAll();
//
//
//        //then
//        assertTrue(result.size() > 0);
//        assertEquals(2, result.size());

    }

    @Test
    void save() {

        Employee savedEmployee = Optional.of(employee).map(this::toEmployee).get();

        given(employeeRepository.save(savedEmployee)).willReturn(savedEmployee);

        Employee result = employeeService.save(savedEmployee);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findOne() {

        Optional<Employee> employeeOptional = Optional.of(toEmployee(employee));

        given(employeeRepository.findById(1L)).willReturn(employeeOptional);

        Optional<Employee> result = employeeService.findOne(1L);

        assertNotNull(result);
        assertEquals(1L, result.get().getId());
        assertEquals("Ivan", result.get().getName());


    }

    @Test
    void testSave() {

        Employee e = toEmployee(employee);

        given(employeeRepository.save(e)).willReturn(e);

        Employee result = employeeService.save(e);

        assertNotNull(result);


    }

    @Test
    void delete() {

        doNothing().when(employeeRepository).deleteById(1L);

        assertDoesNotThrow(() -> employeeService.delete(1L));
    }

    @Test
    void exist() {

        given(employeeRepository.existsById(1L)).willReturn(true);

        boolean result = employeeService.exist(1L);

        assertTrue(result);

    }

    @Test
    void partialUpdate() {

        Employee employee1 = toEmployee(employee);

        Optional<Employee> employeeOptional = Optional.of(employee1);

        given(employeeRepository.findById(1L)).willReturn(employeeOptional);

        given(employeeRepository.save(employee1)).willReturn(employee1);

        Optional<Employee> result = employeeService.partialUpdate(employee1);

        assertNotNull(result);

        assertEquals("Ivan", result.get().getName());

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