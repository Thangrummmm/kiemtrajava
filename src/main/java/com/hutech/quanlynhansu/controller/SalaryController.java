package com.hutech.quanlynhansu.controller;

import com.hutech.quanlynhansu.entity.Employee;
import com.hutech.quanlynhansu.entity.Salary;
import com.hutech.quanlynhansu.service.EmployeeService;
import com.hutech.quanlynhansu.service.SalaryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/salaries")
@RequiredArgsConstructor
public class SalaryController {

    private final Logger logger = LoggerFactory.getLogger(SalaryController.class);

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
    public String saveSalary(@ModelAttribute("salary") @Valid Salary salary, BindingResult result,
                             RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.salary", result);
                redirectAttributes.addFlashAttribute("salary", salary);
                return "redirect:/salaries/new";
            }

            salaryService.saveSalary(salary);
            redirectAttributes.addFlashAttribute("message", "Thêm lương thành công!");
            return "redirect:/salaries";
        } catch (Exception e) {
            logger.error("Error saving salary", e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thêm lương.");
            return "redirect:/salaries/new";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditSalaryForm(@PathVariable Long id, Model model) {
        try {
            Salary salary = salaryService.getSalaryById(id);
            model.addAttribute("salary", salary);
            model.addAttribute("listEmployees", employeeService.getAllEmployees());
            return "salary/edit_salary";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/update/{id}")
    public String updateSalary(@PathVariable Long id, @ModelAttribute("salary") @Valid Salary salary,
                               BindingResult result, RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.salary", result);
                redirectAttributes.addFlashAttribute("salary", salary);
                return "redirect:/salaries/edit/" + id;
            }

            Salary existingSalary = salaryService.getSalaryById(id);
            if (existingSalary == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin lương.");
                return "redirect:/salaries";
            }

            // Cập nhật thông tin lương
            existingSalary.setEmployee(salary.getEmployee());
            existingSalary.setMonth(salary.getMonth());
            existingSalary.setYear(salary.getYear());
            existingSalary.setBasicSalary(salary.getBasicSalary());
            existingSalary.setAllowance(salary.getAllowance());
            existingSalary.setBonus(salary.getBonus());
            existingSalary.setDeduction(salary.getDeduction());

            salaryService.saveSalary(existingSalary); // Lưu lại đối tượng đã cập nhật
            redirectAttributes.addFlashAttribute("message", "Cập nhật lương thành công!");
            return "redirect:/salaries";
        } catch (Exception e) {
            logger.error("Error updating salary with id " + id, e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật lương.");
            return "redirect:/salaries/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteSalary(@PathVariable Long id) {
        salaryService.deleteSalary(id);
        return "redirect:/salaries";
    }
}
