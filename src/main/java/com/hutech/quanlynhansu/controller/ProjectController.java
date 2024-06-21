package com.hutech.quanlynhansu.controller;

import com.hutech.quanlynhansu.entity.Project;
import com.hutech.quanlynhansu.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public String listProjects(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("listProjects", projects);
        return "project/list";
    }

    @GetMapping("/new")
    public String showNewProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "project/new_project";
    }

    @PostMapping("/save")
    public String saveProject(@ModelAttribute("project") @Valid Project project, BindingResult result) {
        if (result.hasErrors()) {
            return "project/new_project";
        }
        projectService.saveProject(project);
        return "redirect:/projects";
    }

    @GetMapping("/edit/{id}")
    public String showEditProjectForm(@PathVariable Long id, Model model) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        return "project/edit_project";
    }

    @PostMapping("/update/{id}")
    public String updateProject(@PathVariable Long id, @ModelAttribute("project") @Valid Project project, BindingResult result) {
        if (result.hasErrors()) {
            project.setProjectId(id);
            return "project/edit_project";
        }
        projectService.saveProject(project);
        return "redirect:/projects";
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }
}
