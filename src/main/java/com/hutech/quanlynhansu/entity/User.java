package com.hutech.quanlynhansu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // id tự động tăng không cần check null

    @OneToOne
    @JoinColumn(name = "employee_id") // Liên kết one-to-one với entity Employee
    private Employee employee;

    private String username; // Tên đăng nhập
    private String password; // Mật khẩu (cần được mã hóa trước khi lưu trữ)

    @NotBlank(message = "Role is mandatory") // Đảm bảo role không được để trống
    private String role; // Vai trò (ví dụ: ADMIN, MANAGER, EMPLOYEE)
}

