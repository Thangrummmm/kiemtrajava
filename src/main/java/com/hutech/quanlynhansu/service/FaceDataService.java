package com.hutech.quanlynhansu.service;

import com.hutech.quanlynhansu.entity.Employee;
import com.hutech.quanlynhansu.entity.FaceData;
import com.hutech.quanlynhansu.repository.EmployeeRepository;
import com.hutech.quanlynhansu.repository.FaceDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FaceDataService {

    private final FaceDataRepository faceDataRepository;
    private final EmployeeRepository employeeRepository;

    public FaceData getFaceDataByEmployeeId(Long employeeId) {
        return faceDataRepository.findByEmployeeEmployeeId(employeeId); // Sử dụng phương thức đã sửa
    }


    public FaceData saveFaceData(Long employeeId, MultipartFile file) throws IOException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Employee not found with id " + employeeId));

        // Kiểm tra xem đã có dữ liệu khuôn mặt cho nhân viên này chưa
        FaceData existingFaceData = faceDataRepository.findByEmployeeEmployeeId(employeeId);
        if (existingFaceData != null) {
            // Nếu đã có, cập nhật dữ liệu mới
            existingFaceData.setFaceFeatures(file.getBytes());
            return faceDataRepository.save(existingFaceData);
        } else {
            // Nếu chưa có, tạo mới dữ liệu khuôn mặt
            FaceData faceData = new FaceData();
            faceData.setEmployee(employee);
            faceData.setFaceFeatures(file.getBytes());
            return faceDataRepository.save(faceData);
        }
    }

    public void deleteFaceData(Long id) {
        faceDataRepository.deleteById(id);
    }

}
