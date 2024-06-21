package com.hutech.quanlynhansu.repository;

import com.hutech.quanlynhansu.entity.FaceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FaceDataRepository extends JpaRepository<FaceData, Long> {
    @Query("SELECT fd FROM FaceData fd WHERE fd.employee.employeeId = :employeeId")
    FaceData findByEmployeeEmployeeId(@Param("employeeId") Long employeeId); // Sửa tên trường ở đây
}

