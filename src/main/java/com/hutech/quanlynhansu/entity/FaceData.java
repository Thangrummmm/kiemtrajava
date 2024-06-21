package com.hutech.quanlynhansu.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "face_data")
@Data
public class FaceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long faceId;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Lob
    private byte[] faceFeatures;
}
