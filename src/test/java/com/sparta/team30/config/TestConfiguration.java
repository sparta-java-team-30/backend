package com.sparta.team30.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration(proxyBeanMethods = false)
public class TestConfiguration {

    @Autowired
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }

    @Bean(name = "testAuditorProvider")
    @Qualifier("testAuditorProvider")
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("testUser");
    }
}
