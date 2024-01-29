package com.example.hirdaramani.service.impl;

import com.example.hirdaramani.dto.EmployeeDto;
import com.example.hirdaramani.dto.SalaryDto;
import com.example.hirdaramani.entity.Employee;
import com.example.hirdaramani.entity.Salary;
import com.example.hirdaramani.repository.EmployeeRepository;
import com.example.hirdaramani.repository.SalaryRepository;
import com.example.hirdaramani.service.EmployeeService;
import com.example.hirdaramani.service.util.Transformer;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final SalaryRepository salaryRepository;

    private final Transformer transformer;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, Transformer transformer, Bucket bucket, SalaryRepository salaryRepository) {
        this.employeeRepository = employeeRepository;
        this.salaryRepository = salaryRepository;
        this.transformer = transformer;
    }

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Employee employee = transformer.fromEmployeeDto(employeeDto);
        try {
            employeeRepository.save(employee);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        employeeDto.setEmpId(employee.getEmpId());
        return employeeDto;
    }

    @Override
    public void updateEmployee(EmployeeDto employeeDto) {
        Employee currEmployee = employeeRepository.findById(employeeDto.getEmpId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No employees found for the given Id" ));
        try {
            currEmployee.setIdNumber(employeeDto.getIdNumber());
            currEmployee.setContactNumber(employeeDto.getContactNumber());
            currEmployee.setAddress(employeeDto.getAddress());
            currEmployee.setActiveStatus(employeeDto.getActiveStatus());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void updateActiveStatus(EmployeeDto employeeDto) {
        Employee currEmployee = employeeRepository.findById(employeeDto.getEmpId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No employees found for the given Id" ));
        try {
            employeeRepository.updateActiveStatus(employeeDto.getActiveStatus(), employeeDto.getEmpId());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void deleteEmployee(Integer employeeId) {
        if(!employeeRepository.existsById(employeeId)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No employees found for the given Id");
        try {
            employeeRepository.deleteById(employeeId);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public List<EmployeeDto> getActiveEmployees() {
        List<Employee> activeEmployees;
        List<EmployeeDto> activeEmployeeDtos = new ArrayList<>();

        try {
            activeEmployees = employeeRepository.findActiveEmployees();
            if(activeEmployees.isEmpty()){
                return new ArrayList<>();
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        for (Employee activeEmployee : activeEmployees) {
            EmployeeDto employeeDto = transformer.toEmployeeDto(activeEmployee);
            activeEmployeeDtos.add(employeeDto);
        }
        return activeEmployeeDtos;
    }

    @Override
    public List<SalaryDto> getSalaryByEmployeeAndYear(Integer emp, String year) {
        List<Salary> salaryByEmployeeAndYear;
        List<SalaryDto> salaryByEmployeeAndYearDtos = new ArrayList<>();

        Optional<Employee> employee = employeeRepository.findById(emp);

        try{
            salaryByEmployeeAndYear = salaryRepository.findSalaryByEmployeeAndYear(employee.get(), year);
            if(salaryByEmployeeAndYear.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No records available for given parameters");
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        for (Salary salary : salaryByEmployeeAndYear) {
            SalaryDto sDto = transformer.toSalaryDto(salary);
            salaryByEmployeeAndYearDtos.add(sDto);
        }

        return salaryByEmployeeAndYearDtos;
    }

    @Override
    public void payEmployee(SalaryDto salaryDto) {
        Optional<Employee> optEmployee = employeeRepository.findById(salaryDto.getEmployeeId());

        if(optEmployee.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No employees found");

        Salary salary = transformer.fromSalaryDto(salaryDto);

        try {
            optEmployee.get().paySalary(salary);
            salaryRepository.save(salary);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
