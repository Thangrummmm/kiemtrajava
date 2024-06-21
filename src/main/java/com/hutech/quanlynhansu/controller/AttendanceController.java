package com.hutech.quanlynhansu.controller;

import com.hutech.quanlynhansu.entity.Attendance;
import com.hutech.quanlynhansu.repository.AttendanceRepository;
import com.hutech.quanlynhansu.service.AttendanceService;
import com.hutech.quanlynhansu.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;
    private final AttendanceRepository attendanceRepository; // Inject AttendanceRepository


    @GetMapping
    public String listAttendances(Model model) {
        List<Attendance> attendances = attendanceService.getAllAttendances();
        model.addAttribute("listAttendances", attendances);
        return "attendance/list";
    }

    @GetMapping("/new")
    public String showNewAttendanceForm(Model model) {
        model.addAttribute("attendance", new Attendance());
        model.addAttribute("listEmployees", employeeService.getAllEmployees());
        return "attendance/new_attendance";
    }

    @PostMapping("/save")
    public String saveAttendance(@ModelAttribute("attendance") @Valid Attendance attendance, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("listEmployees", employeeService.getAllEmployees());
            return "attendance/new_attendance";
        }
        attendanceService.saveAttendance(attendance);
        return "redirect:/attendances";
    }

    @GetMapping("/edit/{id}")
    public String showEditAttendanceForm(@PathVariable Long id, Model model) {
        Attendance attendance = attendanceService.getAttendanceById(id);
        model.addAttribute("attendance", attendance);
        model.addAttribute("listEmployees", employeeService.getAllEmployees());
        return "attendance/edit_attendance";
    }

    @PostMapping("/update/{id}")
    public String updateAttendance(@PathVariable Long id, @ModelAttribute("attendance") @Valid Attendance attendance, BindingResult result, Model model) {
        if (result.hasErrors()) {
            attendance.setAttendanceId(id);
            model.addAttribute("listEmployees", employeeService.getAllEmployees());
            return "attendance/edit_attendance";
        }
        attendanceService.saveAttendance(attendance);
        return "redirect:/attendances";
    }

    @GetMapping("/delete/{id}")
    public String deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return "redirect:/attendances";
    }

    // Trong AttendanceController
    @GetMapping("/filter")
    public String filterAttendances(Model model,
                                    @RequestParam(value = "employeeId", required = false) Long employeeId,
                                    @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                    @RequestParam(value = "endDate", required = false) LocalDate endDate) {

        List<Attendance> attendances;
        Attendance latestAttendance = null;

        if (employeeId != null && startDate != null && endDate != null) {
            attendances = attendanceService.getAttendancesByDateRangeAndEmployeeId(startDate, endDate, employeeId);
            latestAttendance = attendances.stream().findFirst().orElse(null); // Lấy lần chấm công gần đây nhất
        } else if (employeeId != null) {
            latestAttendance = attendanceRepository.findFirstByEmployeeEmployeeIdOrderByAttendanceDateDesc(employeeId).orElse(null); // Sửa lỗi ở đây
            attendances = attendanceRepository.findByEmployeeEmployeeId(employeeId); // Lấy tất cả các lần chấm công của nhân viên
        } else if (startDate != null && endDate != null) {
            attendances = attendanceService.getAttendancesByDateRange(startDate, endDate);
            latestAttendance = attendances.stream().findFirst().orElse(null); // Lấy lần chấm công gần đây nhất
        } else {
            attendances = attendanceService.getAllAttendances();
            latestAttendance = attendances.stream().findFirst().orElse(null); // Lấy lần chấm công gần đây nhất
        }

        model.addAttribute("listAttendances", attendances);
        model.addAttribute("latestAttendance", latestAttendance); // Thêm lần chấm công gần đây nhất vào model
        model.addAttribute("listEmployees", employeeService.getAllEmployees());
        return "attendance/list";
    }


}
