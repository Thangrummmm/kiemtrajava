package com.hutech.quanlynhansu.config;

import com.hutech.quanlynhansu.entity.Employee;
import com.hutech.quanlynhansu.entity.User;
import com.hutech.quanlynhansu.repository.EmployeeRepository;
import com.hutech.quanlynhansu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository, EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin") == null) {
            Employee employee = new Employee();
            employee.setFullName("Admin");
            // ... (Các thông tin khác của nhân viên admin)

            employeeRepository.save(employee); // Lưu thông tin nhân viên admin

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Mã hóa mật khẩu
            admin.setRole("ADMIN");
            admin.setEmployee(employee); // Liên kết với nhân viên admin

            userRepository.save(admin); // Lưu tài khoản admin
        }
    }
}
