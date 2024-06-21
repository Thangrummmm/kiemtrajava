package com.hutech.quanlynhansu.service;

import com.hutech.quanlynhansu.entity.Salary;
import com.hutech.quanlynhansu.repository.SalaryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Validated
@RequiredArgsConstructor
public class SalaryService {
    private final SalaryRepository salaryRepository;

    public List<Salary> getAllSalaries() {
        return salaryRepository.findAll();
    }
    public List<Salary> getSalariesByEmployeeId(Long employeeId) {
        return salaryRepository.findByEmployeeId(employeeId);
    }


    public Salary getSalaryById(Long id) {
        return salaryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Salary not found with id " + id));
    }

    public Salary saveSalary(@Valid Salary salary) {
        return salaryRepository.save(salary);
    }

    public void deleteSalary(Long id) {
        salaryRepository.deleteById(id);
    }

    public List<Salary> getSalariesByMonthAndYear(int month, int year) {
        return salaryRepository.findByMonthAndYear(month, year);
    }
}
