package com.example.hirdaramani.api;

import com.example.hirdaramani.dto.EmployeeDto;
import com.example.hirdaramani.dto.SalaryDto;
import com.example.hirdaramani.service.EmployeeService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Pattern;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EmployeeHttpController {
    @Autowired
    private EmployeeService employeeService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public EmployeeDto createNewEmployee(@RequestBody @Validated EmployeeDto employeeDto){
        return employeeService.saveEmployee(employeeDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{employee-id}", consumes = "application/json")
    public void updateEmployee(@PathVariable("employee-id") Integer employeeId, @RequestBody @Validated EmployeeDto employeeReqDto){
        employeeReqDto.setId(employeeId);
        employeeService.updateEmployee(employeeReqDto);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/status/{employee-id}", consumes = "application/json")
    public void updateEmployeeActiveStatus(@PathVariable("employee-id") Integer employeeId, @RequestBody @Validated EmployeeDto employeeReqDto){
        employeeReqDto.setId(employeeId);
        employeeService.updateActiveStatus(employeeReqDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{employee-id}")
    public void deleteEmployee(@PathVariable("employee-id") Integer employeeId){
        employeeService.deleteEmployee(employeeId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json")
    public List<EmployeeDto> getActiveEmployees(){
        return employeeService.getActiveEmployees();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(params = "query" ,produces = "application/json")
    public List<SalaryDto> getSalaryByEmployeeAndYear(String query){
        String[] parameters = query.split("n");
        Integer employee = Integer.parseInt(parameters[0]);
        String year = parameters[1];
        return employeeService.getSalaryByEmployeeAndYear(employee, year);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/payment", produces = "application/json", consumes = "application/json")
    public void payEmployees(@RequestBody @Validated SalaryDto salaryDto){
        employeeService.payEmployee(salaryDto);
    }


//    private final SecretKey secretKey;
//
//    public EmployeeHttpController(SecretKey secretKey) {
//        this.secretKey = secretKey;
//    }

//    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
//    public String login(@RequestBody Map<String, String> credentials){
//        String username = credentials.get("username");
//        String password = credentials.get("password");
//
//        employeeService.findEmployeeByName(username, password);
//
//        JwtBuilder jwtBuilder = Jwts.builder();
//        jwtBuilder.issuer("hirdaramani");
//        jwtBuilder.issuedAt(new Date());
//        LocalDateTime tokenExpTime = LocalDateTime.now().plusMinutes(10);
//
//        Date expTime = Date.from(tokenExpTime.atZone(ZoneId.systemDefault()).toInstant());
//        jwtBuilder.expiration(expTime);
//
//        jwtBuilder.subject(username);
//
//        jwtBuilder.signWith(secretKey);
//
//        return jwtBuilder.compact();
//    }
}
