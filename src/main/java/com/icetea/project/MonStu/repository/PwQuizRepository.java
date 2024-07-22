package com.icetea.project.MonStu.repository;

import com.icetea.project.MonStu.domain.PwQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PwQuizRepository extends JpaRepository<PwQuiz,Long> {



}
