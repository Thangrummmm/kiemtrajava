package com.hutech.quanlynhansu.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "performance_review")
@Data
public class PerformanceReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private String reviewPeriod;
    private int rating;
    private String comments;
}
