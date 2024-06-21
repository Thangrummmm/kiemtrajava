package com.hutech.quanlynhansu.controller;

import com.hutech.quanlynhansu.entity.FaceData;
import com.hutech.quanlynhansu.service.EmployeeService;
import com.hutech.quanlynhansu.service.FaceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/face-data")
@RequiredArgsConstructor
public class FaceDataController {

    private final FaceDataService faceDataService;
    private final EmployeeService employeeService;

    @GetMapping("/new/{employeeId}")
    public String showNewFaceDataForm(@PathVariable Long employeeId, Model model) {
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("faceData", new FaceData());
        return "face_data/new_face_data";
    }

    @PostMapping("/save/{employeeId}")
    public String saveFaceData(@PathVariable Long employeeId, @RequestParam("file") MultipartFile file) throws IOException {
        faceDataService.saveFaceData(employeeId, file);
        return "redirect:/employees";
    }

    @GetMapping("/delete/{id}")
    public String deleteFaceData(@PathVariable Long id) {
        faceDataService.deleteFaceData(id);
        return "redirect:/employees";
    }
}
