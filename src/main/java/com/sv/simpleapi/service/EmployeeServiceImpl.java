package com.sv.simpleapi.service;


import com.sv.simpleapi.DTO.EmployeeDTO;
import com.sv.simpleapi.repository.EmployeeRepository;
import com.sv.simpleapi.model.Employee;
import com.sv.simpleapi.response.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("EmployeeServiceImpl")
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();

//                .stream().map(employee -> EmployeeDTO.builder().id(employee.getId())
//                        .name(employee.getName())
//                        .email(employee.getEmail())
//                        .lastName(employee.getLastName()).build())
//                .toList();
    }

    @Override
    public Employee save() {
        return null;
    }

    @Override
    public Optional<Employee> findOne(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
        public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public boolean exist(Long id) {
        return employeeRepository.existsById(id);
    }

    @Override
    public Optional<Employee> partialUpdate(Employee employee) {
        return employeeRepository.findById(employee.getId()).map(existingEmployee -> {
            if (employee.getName() != null) {
                existingEmployee.setName(employee.getName());
            }
            if (employee.getLastName() != null) {
                existingEmployee.setLastName(employee.getLastName());
            }
            if (employee.getEmail() != null) {
                existingEmployee.setEmail(employee.getEmail());
            }
            if (employee.getDepartment() != null) {
                existingEmployee.setDepartment(employee.getDepartment());
            }
            return existingEmployee;
        }).map(employeeRepository::save);
    }

    @Override
    public EmployeeResponse findAll(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Employee> result = employeeRepository.findAll(pageable);

        return EmployeeResponse.builder()
                .employees(result.getContent().stream().map(e -> EmployeeDTO.builder().name(e.getName()).lastName(e.getLastName()).id(e.getId()).build()).toList())
                .totalPages(result.getTotalPages())
                .pageSize(result.getSize())
                .pageNo(result.getNumber())
                .last(result.isLast())
                .totalElements(result.getTotalElements())
                .build();

    }

}
