package com.hutech.quanlynhansu.repository;

import com.hutech.quanlynhansu.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    @Query("SELECT s FROM Salary s WHERE s.employee.employeeId = :employeeId")
    List<Salary> findByEmployeeId(@Param("employeeId") Long employeeId);
    List<Salary> findByMonthAndYear(int month, int year);
}
