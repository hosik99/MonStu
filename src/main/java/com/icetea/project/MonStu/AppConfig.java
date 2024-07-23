package com.icetea.project.MonStu;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
//    @Bean
//    public ModelMapper modelMapper(){
//        return new ModelMapper();
//    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true);  //필드 이름을 기반으로 객체 간의 매핑을 자동으로 수행하도록 활성화, getter와 setter가 없어도 필드 이름이 동일하면 매핑이 가능
//                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);    //private 필드에도 접근
        return modelMapper;
    }

}
