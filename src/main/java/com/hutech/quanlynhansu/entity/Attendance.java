package com.hutech.quanlynhansu.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
@Data
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne // Mỗi chấm công thuộc về một nhân viên
    @JoinColumn(name = "employee_id") // Khóa ngoại (foreign key) tham chiếu đến bảng employee
    private Employee employee;

    private LocalDate attendanceDate;
    private LocalTime timeIn;
    private LocalTime timeOut;
    private double hoursWorked;
    private String status;
    private String attendanceMethod;
    @Lob
    private byte[] faceImage;
}
