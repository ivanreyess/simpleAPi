package com.sv.simpleapi.service;



import com.sv.simpleapi.DTO.EmployeeDTO;
import com.sv.simpleapi.model.Employee;
import com.sv.simpleapi.response.EmployeeResponse;

import java.util.List;
import java.util.Optional;


public interface EmployeeService {

    List<Employee> findAll();

    Employee save();

    Optional<Employee> findOne(Long id);

    Employee save(Employee employee);

    void delete(Long id);

    boolean exist(Long id);

    Optional<Employee> partialUpdate(Employee employee);


    EmployeeResponse findAll(int pageNo, int pageSize, String sortBy, String sortDir);
}
