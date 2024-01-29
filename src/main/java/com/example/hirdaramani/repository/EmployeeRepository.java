package com.example.hirdaramani.repository;

import com.example.hirdaramani.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE Employee e SET e.ActiveStatus = :newActiveStatus WHERE e.EmpId = :id")
    void updateActiveStatus(@Param("newActiveStatus") Boolean newActiveStatus, @Param("id") Integer id);

    @Query("SELECT e FROM Employee e WHERE e.ActiveStatus = true ")
    List<Employee> findActiveEmployees();

    @Query("SELECT e FROM Employee e WHERE e.IdNumber LIKE :query OR e.ContactNumber LIKE :query OR e.Address LIKE :query ")
    List<Employee> findByQuery(@Param("query") String query);

//    @Query("SELECT DISTINCT e FROM Employee e WHERE e.IdNumber LIKE :query")
//    Employee findById(@Param("query") String query);


}
