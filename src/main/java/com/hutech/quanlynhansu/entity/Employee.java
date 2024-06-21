package com.hutech.quanlynhansu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "employee")
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String phoneNumber;
    private String email;
    private String position;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private LocalDate startDate;
    private String contractType;
    private double salary;
    private String status;
}
