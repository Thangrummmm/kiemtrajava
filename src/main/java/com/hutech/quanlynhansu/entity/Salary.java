package com.hutech.quanlynhansu.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "salary")
@Data
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salaryId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private int month;
    private int year;
    private double basicSalary;
    private double allowance;
    private double bonus;
    private double deduction;
    private double netSalary;
}
