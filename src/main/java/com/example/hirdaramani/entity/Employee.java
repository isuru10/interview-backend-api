package com.example.hirdaramani.entity;

import lombok.*;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Employee")
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer EmpId;
    @Column(nullable = false, length = 255)
    private String IdNumber;
    @Column(nullable = false, length = 100)
    private String ContactNumber;
    @Column(nullable = false, length = 500)
    private String Address;
    @Column(nullable = false)
    private boolean ActiveStatus;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "Employee")
    private Set<Salary> salarySet;

    public Employee(Integer empId, String idNumber, String contactNumber, String address, boolean activeStatus) {
        EmpId = empId;
        IdNumber = idNumber;
        ContactNumber = contactNumber;
        Address = address;
        ActiveStatus = activeStatus;
    }

    public void paySalary(Salary salary){
        salarySet.add(salary);
        salary.setEmployee(this);
    }

    public void removeSalary(Salary salary){
        throw new RuntimeException("Unsupported Operation");
    }
}
