package com.icetea.project.MonStu.repository;

import com.icetea.project.MonStu.domain.AiContent;
import com.icetea.project.MonStu.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiContentRepository extends JpaRepository<AiContent,Long> {
}
