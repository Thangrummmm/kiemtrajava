package com.hutech.quanlynhansu.controller;

import com.hutech.quanlynhansu.entity.Department;
import com.hutech.quanlynhansu.entity.Employee;
import com.hutech.quanlynhansu.entity.Project;
import com.hutech.quanlynhansu.service.DepartmentService;
import com.hutech.quanlynhansu.service.EmployeeService;
import com.hutech.quanlynhansu.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final ProjectService projectService;

    @GetMapping
    public String listEmployees(Model model,
                                @RequestParam(value = "departmentId", required = false) Long departmentId,
                                @RequestParam(value = "projectId", required = false) Long projectId) {
        List<Employee> employees = employeeService.findByDepartmentAndProject(departmentId, projectId);
        model.addAttribute("listEmployees", employees);
        model.addAttribute("listDepartments", departmentService.getAllDepartments());
        model.addAttribute("listProjects", projectService.getAllProjects());
        return "employee/list";
    }

    @GetMapping("/new")
    public String showNewEmployeeForm(Model model) {
        try {
            model.addAttribute("employee", new Employee());
            model.addAttribute("listDepartments", departmentService.getAllDepartments());
            model.addAttribute("listProjects", projectService.getAllProjects());
            return "employee/new_employee";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // Trả về trang lỗi nếu có
        }
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") @Valid Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Nếu có lỗi validation, trả về form và hiển thị thông báo lỗi
            model.addAttribute("listDepartments", departmentService.getAllDepartments());
            model.addAttribute("listProjects", projectService.getAllProjects());
            return "employee/new_employee";
        }

        // Nếu không có lỗi, lưu nhân viên và chuyển hướng
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }



    @GetMapping("/edit/{id}")
    public String showEditEmployeeForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        model.addAttribute("listDepartments", departmentService.getAllDepartments());
        model.addAttribute("listProjects", projectService.getAllProjects());
        return "employee/edit_employee";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute("employee") @Valid Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            employee.setEmployeeId(id); // Đảm bảo ID được giữ lại khi trả về form
            model.addAttribute("listDepartments", departmentService.getAllDepartments());
            model.addAttribute("listProjects", projectService.getAllProjects());
            return "employee/edit_employee";
        }
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }
}
