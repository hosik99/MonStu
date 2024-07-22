package com.icetea.project.MonStu.util;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/*
    @Component는 스프링이 자동으로 객체를 생성하고 관리하는 방식이며,
    @Bean은 개발자가 직접 메서드를 정의하여 객체를 생성하고 빈으로 등록하는 방식
    @Bean으로 등록된 객체는 메서드 호출 시 생성되며, 싱글톤으로 관리
*/
@Component
public class QuerydslManager {
    @PersistenceContext //엔티티 매니저(EntityManager)를 주입
    private EntityManager entityManager;

    @Bean   //해당 메서드가 생성한 객체를 스프링 컨테이너에 빈으로 등록
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }
}
