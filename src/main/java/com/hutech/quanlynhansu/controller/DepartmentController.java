package com.hutech.quanlynhansu.controller;

import com.hutech.quanlynhansu.entity.Department;
import com.hutech.quanlynhansu.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public String listDepartments(Model model) {
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("listDepartments", departments);
        return "department/list";
    }

    @GetMapping("/new")
    public String showNewDepartmentForm(Model model) {
        model.addAttribute("department", new Department());
        return "department/new_department";
    }

    @PostMapping("/save")
    public String saveDepartment(@ModelAttribute("department") @Valid Department department, BindingResult result) {
        if (result.hasErrors()) {
            return "department/new_department";
        }
        departmentService.saveDepartment(department);
        return "redirect:/departments";
    }

    @GetMapping("/edit/{id}")
    public String showEditDepartmentForm(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id);
        model.addAttribute("department", department);
        return "department/edit_department";
    }

    @PostMapping("/update/{id}")
    public String updateDepartment(@PathVariable Long id, @ModelAttribute("department") @Valid Department department, BindingResult result) {
        if (result.hasErrors()) {
            department.setDepartmentId(id);
            return "department/edit_department";
        }
        departmentService.saveDepartment(department);
        return "redirect:/departments";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/departments";
    }
}
