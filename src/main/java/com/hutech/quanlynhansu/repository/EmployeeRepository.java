package com.hutech.quanlynhansu.repository;

import com.hutech.quanlynhansu.entity.Department;
import com.hutech.quanlynhansu.entity.Employee;
import com.hutech.quanlynhansu.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartment(Department department);
    List<Employee> findByProject(Project project);
    Employee findByEmail(String email);

    // Truy vấn tùy chỉnh sử dụng JPQL
    @Query("SELECT e FROM Employee e WHERE e.fullName LIKE %:keyword% OR e.email LIKE %:keyword%")
    List<Employee> searchEmployees(@Param("keyword") String keyword);

    // Truy vấn để lấy danh sách nhân viên theo phòng ban và dự án
    @Query("SELECT e FROM Employee e WHERE (:departmentId IS NULL OR e.department.departmentId = :departmentId) AND (:projectId IS NULL OR e.project.projectId = :projectId)")
    List<Employee> findByDepartmentAndProject(@Param("departmentId") Long departmentId, @Param("projectId") Long projectId);

}
