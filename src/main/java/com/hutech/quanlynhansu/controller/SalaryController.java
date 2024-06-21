package com.hutech.quanlynhansu.controller;

import com.hutech.quanlynhansu.entity.Employee;
import com.hutech.quanlynhansu.entity.Salary;
import com.hutech.quanlynhansu.service.EmployeeService;
import com.hutech.quanlynhansu.service.SalaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/salaries")
@RequiredArgsConstructor
public class SalaryController {

    private final SalaryService salaryService;
    private final EmployeeService employeeService;

    @GetMapping
    public String listSalaries(Model model) {
        List<Salary> salaries = salaryService.getAllSalaries();
        model.addAttribute("listSalaries", salaries);
        return "salary/list";
    }

    @GetMapping("/new")
    public String showNewSalaryForm(Model model) {
        model.addAttribute("salary", new Salary());
        model.addAttribute("listEmployees", employeeService.getAllEmployees());
        return "salary/new_salary";
    }

    @PostMapping("/save")
    public String saveSalary(@ModelAttribute("salary") @Valid Salary salary, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("listEmployees", employeeService.getAllEmployees());
            return "salary/new_salary";
        }
        salaryService.saveSalary(salary);
        return "redirect:/salaries";
    }

    @GetMapping("/edit/{id}")
    public String showEditSalaryForm(@PathVariable Long id, Model model) {
        Salary salary = salaryService.getSalaryById(id);
        model.addAttribute("salary", salary);
        model.addAttribute("listEmployees", employeeService.getAllEmployees());
        return "salary/edit_salary";
    }

    @PostMapping("/update/{id}")
    public String updateSalary(@PathVariable Long id, @ModelAttribute("salary") @Valid Salary salary, BindingResult result, Model model) {
        if (result.hasErrors()) {
            salary.setSalaryId(id);
            model.addAttribute("listEmployees", employeeService.getAllEmployees());
            return "salary/edit_salary";
        }
        salaryService.saveSalary(salary);
        return "redirect:/salaries";
    }

    @GetMapping("/delete/{id}")
    public String deleteSalary(@PathVariable Long id) {
        salaryService.deleteSalary(id);
        return "redirect:/salaries";
    }
}
