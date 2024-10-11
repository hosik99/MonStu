package com.icetea.project.MonStu.repository;

import com.icetea.project.MonStu.domain.Content;
import com.icetea.project.MonStu.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content,Long> {

}
