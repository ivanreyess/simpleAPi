package com.sv.simpleapi.controller;


import com.sv.simpleapi.config.AppConstants;
import com.sv.simpleapi.model.Employee;
import com.sv.simpleapi.response.EmployeeResponse;
import com.sv.simpleapi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(@Qualifier("EmployeeServiceImpl") EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<EmployeeResponse> getAllEmployees(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok().body(employeeService.findAll(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name = "id") Long id) {
        return employeeService.findOne(id).map(employee -> ResponseEntity.ok().body(employee)).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) throws URISyntaxException {
        if (employee.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Employee result = employeeService.save(employee);
        return ResponseEntity
                .created(new URI("api/employees/" + result.getId()))
                .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable(name = "id") Long id) {
        employeeService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody Employee employee) {
        if (employee.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!Objects.equals(id, employee.getId())) {
            return ResponseEntity.badRequest().build();
        }

        if (!employeeService.exist(id)) {
            return ResponseEntity.notFound().build();
        }

        Employee result = employeeService.save(employee);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Employee> partialUpdateEmployee(@PathVariable(value = "id", required = false) final Long id, @RequestBody Employee employee){

        if (employee.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!Objects.equals(id, employee.getId())) {
            return ResponseEntity.badRequest().build();
        }

        if (!employeeService.exist(id)) {
            return ResponseEntity.badRequest().build();
        }

        return employeeService.partialUpdate(employee).map(e -> ResponseEntity.ok().body(employee)).orElse(ResponseEntity.badRequest().build());
    }

}
