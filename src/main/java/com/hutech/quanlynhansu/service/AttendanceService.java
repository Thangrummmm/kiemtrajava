package com.hutech.quanlynhansu.service;

import com.hutech.quanlynhansu.entity.Attendance;
import com.hutech.quanlynhansu.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }

    public Attendance getAttendanceById(Long id) {
        return attendanceRepository.findById(id).orElse(null);
    }

    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }


    public List<Attendance> getAttendancesByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByAttendanceDateBetween(startDate, endDate);
    }

    public List<Attendance> getAttendancesByDateRangeAndEmployeeId(LocalDate startDate, LocalDate endDate, Long employeeId) {
        return attendanceRepository.findByAttendanceDateBetweenAndEmployeeId(startDate, endDate, employeeId);
    }

}
