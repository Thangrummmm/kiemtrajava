package com.hutech.quanlynhansu.repository;

import com.hutech.quanlynhansu.entity.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {
    @Query("SELECT p FROM PerformanceReview p WHERE p.employee.employeeId = :employeeId")
    List<PerformanceReview> findByEmployeeId(@Param("employeeId") Long employeeId);
}


