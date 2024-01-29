package com.example.hirdaramani.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Salary")
public class Salary implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer SalaryId;
    @Column(nullable = false, length = 255)
    private String Year;
    @Column(nullable = false, length = 255)
    private String Month;
    @Column(nullable = false, length = 255)
    private BigDecimal Salary;
    @Column(nullable = false)
    private LocalDateTime CreatedDateTime;

    @ManyToOne
    @JoinColumn(name = "EmpId", nullable = false, referencedColumnName = "EmpId")
    private Employee Employee;


    @PrePersist
    protected void onCreate(){
        this.CreatedDateTime = LocalDateTime.now();
    }
}
