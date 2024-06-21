package com.hutech.quanlynhansu.service;

import com.hutech.quanlynhansu.entity.Employee;
import com.hutech.quanlynhansu.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok sẽ tự động tạo constructor cho các trường final
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    // Trong EmployeeService
    public List<Employee> findByDepartmentAndProject(Long departmentId, Long projectId) {
        return employeeRepository.findByDepartmentAndProject(departmentId, projectId);
    }

}
