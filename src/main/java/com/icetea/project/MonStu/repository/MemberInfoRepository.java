package com.icetea.project.MonStu.repository;

import com.icetea.project.MonStu.domain.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInfoRepository extends JpaRepository<MemberInfo,Long> {
}
