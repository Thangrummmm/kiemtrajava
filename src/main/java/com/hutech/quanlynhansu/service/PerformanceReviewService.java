package com.hutech.quanlynhansu.service;

import com.hutech.quanlynhansu.entity.PerformanceReview;
import com.hutech.quanlynhansu.repository.PerformanceReviewRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Validated
@RequiredArgsConstructor
public class PerformanceReviewService {

    private final PerformanceReviewRepository reviewRepository;

    public List<PerformanceReview> getAllReviews() {
        return reviewRepository.findAll();
    }
    public List<PerformanceReview> getReviewsByEmployeeId(Long employeeId) {
        return reviewRepository.findByEmployeeId(employeeId);
    }

    public PerformanceReview getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Review not found with id " + id));
    }

    public PerformanceReview saveReview(@Valid PerformanceReview review) {
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }


}
