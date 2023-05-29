package com.sv.simpleapi.repository;

import com.sv.simpleapi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

//    @Query(value = " FROM Employee e JOIN FETCH  e.reviews r")
    List<Employee> findAll();
}
