package com.icetea.project.MonStu.repository;

import com.icetea.project.MonStu.domain.Member;
import com.icetea.project.MonStu.domain.MyWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyWordRepository extends JpaRepository<MyWord,Long> {

}
