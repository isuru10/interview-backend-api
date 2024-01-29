package com.example.hirdaramani.repository;

import com.example.hirdaramani.entity.Employee;
import com.example.hirdaramani.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalaryRepository extends JpaRepository<Salary, Integer> {
    @Query("SELECT s FROM Salary s WHERE s.Employee = :employee AND s.Year = :year")
    List<Salary> findSalaryByEmployeeAndYear(@Param("employee") Employee employee, @Param("year") String year);
}
