package com.example.hirdaramani.service.util;

import com.example.hirdaramani.dto.EmployeeDto;
import com.example.hirdaramani.dto.SalaryDto;
import com.example.hirdaramani.entity.Employee;
import com.example.hirdaramani.entity.Salary;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Transformer {

    private final ModelMapper mapper;

    public Transformer(ModelMapper mapper) {
        this.mapper = mapper;
        mapper.typeMap(String.class, Integer.class)
                .setConverter(ctx -> ctx.getSource() != null ?  Integer.parseInt(ctx.getSource().split("v")[0]) : null);

//        mapper.typeMap(Employee.class, EmployeeDto.class)
//                .addMapping(Employee::getEmpId, (employeeDto, emp) ->
//                        employeeDto.setId(Integer.valueOf((String) emp)));

    }

    public Employee fromEmployeeDto(EmployeeDto employeeDto){
        Employee employee = mapper.map(employeeDto, Employee.class);
        return employee;
    }

    public EmployeeDto toEmployeeDto(Employee employee){
        EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
        return employeeDto;
    }

    public Salary fromSalaryDto(SalaryDto salaryDto){
        Salary salary = mapper.map(salaryDto, Salary.class);
        return salary;
    }

    public SalaryDto toSalaryDto(Salary salary){
        SalaryDto salaryDto = mapper.map(salary, SalaryDto.class);
        return salaryDto;
    }
}
