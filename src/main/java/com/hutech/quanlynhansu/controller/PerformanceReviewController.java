package com.hutech.quanlynhansu.controller;

import com.hutech.quanlynhansu.entity.PerformanceReview;
import com.hutech.quanlynhansu.service.EmployeeService;
import com.hutech.quanlynhansu.service.PerformanceReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/performance-reviews")
@RequiredArgsConstructor
public class PerformanceReviewController {

    private final PerformanceReviewService reviewService;
    private final EmployeeService employeeService;

    @GetMapping
    public String listReviews(Model model) {
        List<PerformanceReview> reviews = reviewService.getAllReviews();
        model.addAttribute("listReviews", reviews);
        return "performance_review/list";
    }

    @GetMapping("/new")
    public String showNewReviewForm(Model model) {
        model.addAttribute("review", new PerformanceReview());
        model.addAttribute("listEmployees", employeeService.getAllEmployees());
        return "performance_review/new_review";
    }

    @PostMapping("/save")
    public String saveReview(@ModelAttribute("review") @Valid PerformanceReview review, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("listEmployees", employeeService.getAllEmployees());
            return "performance_review/new_review";
        }
        reviewService.saveReview(review);
        return "redirect:/performance-reviews";
    }

    @GetMapping("/edit/{id}")
    public String showEditReviewForm(@PathVariable Long id, Model model) {
        PerformanceReview review = reviewService.getReviewById(id);
        model.addAttribute("review", review);
        model.addAttribute("listEmployees", employeeService.getAllEmployees());
        return "performance_review/edit_review";
    }

    @PostMapping("/update/{id}")
    public String updateReview(@PathVariable Long id, @ModelAttribute("review") @Valid PerformanceReview review, BindingResult result, Model model) {
        if (result.hasErrors()) {
            review.setReviewId(id);
            model.addAttribute("listEmployees", employeeService.getAllEmployees());
            return "performance_review/edit_review";
        }
        reviewService.saveReview(review);
        return "redirect:/performance-reviews";
    }

    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return "redirect:/performance-reviews";
    }
}
