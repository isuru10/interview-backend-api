package com.example.hirdaramani.service;

import com.example.hirdaramani.dto.EmployeeDto;
import com.example.hirdaramani.dto.SalaryDto;
import com.example.hirdaramani.entity.Salary;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeService {

    EmployeeDto saveEmployee(EmployeeDto employeeDto);
    void updateEmployee(EmployeeDto employeeDto);
    void updateActiveStatus(EmployeeDto employeeDto);
    void deleteEmployee(Integer employeeId);
    List<EmployeeDto> getActiveEmployees();
    List<SalaryDto> getSalaryByEmployeeAndYear(Integer employee, String year);

    void payEmployee(SalaryDto salaryDto);

}
