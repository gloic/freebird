package com.gloic.freebird.webservices.response.mapper;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MapperConfiguration {

    @Bean
    public Map<Class, Class> dtoLightMappers() throws ClassNotFoundException {
        return getClassMapForAnnotation(true);
    }

    @Bean
    public Map<Class, Class> dtoMappers() throws ClassNotFoundException {
        return getClassMapForAnnotation(false);
    }

    private Map<Class, Class> getClassMapForAnnotation(boolean lights) throws ClassNotFoundException {
        String packageStr = "com.gloic.freebird.webservices.response.dto";
        Map<Class, Class> map = new HashMap<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(DTO.class));
        for (BeanDefinition bd : scanner.findCandidateComponents(packageStr)) {
            Class<?> dto = Class.forName(bd.getBeanClassName());
            DTO annotation = dto.getAnnotation(DTO.class);
            Class<?> aClass = annotation.DtoOf();
            if (annotation.light() == lights) {
                map.put(aClass, dto);
            }
        }

        return map;
    }
}
