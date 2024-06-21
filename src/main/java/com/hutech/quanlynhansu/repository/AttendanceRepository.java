package com.hutech.quanlynhansu.repository;

import com.hutech.quanlynhansu.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query("SELECT a FROM Attendance a WHERE a.employee.employeeId = :employeeId")
    List<Attendance> findByEmployeeEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT a FROM Attendance a WHERE a.employee.employeeId = :employeeId AND a.attendanceDate = :date")
    Attendance findByEmployeeIdAndDate(@Param("employeeId") Long employeeId, @Param("date") LocalDate date);

    List<Attendance> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM Attendance a WHERE a.employee.employeeId = :employeeId AND a.attendanceDate BETWEEN :startDate AND :endDate")
    List<Attendance> findByAttendanceDateBetweenAndEmployeeId(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("employeeId") Long employeeId);

    @Query("SELECT a FROM Attendance a WHERE a.employee.employeeId = :employeeId ORDER BY a.attendanceDate DESC") // sắp xếp giảm dần theo ngày chấm công
    Optional<Attendance> findFirstByEmployeeEmployeeIdOrderByAttendanceDateDesc(@Param("employeeId") Long employeeId); // Sửa lỗi ở đây
}
