package com.hutech.quanlynhansu.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "department")
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;
    private String departmentName;
}
