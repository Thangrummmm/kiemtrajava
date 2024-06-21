package com.hutech.quanlynhansu.repository;

import com.hutech.quanlynhansu.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByProjectName(String projectName);
}
