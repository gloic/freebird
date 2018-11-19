package com.gloic.freebird.webservices.response.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GenericMapper {

    private static Map<Class, Class> dtoLightMappers;
    private static Map<Class, Class> dtoMappers;

    @Autowired
    public GenericMapper(Map<Class, Class> dtoLightMappers, Map<Class, Class> dtoMappers) {
        GenericMapper.dtoLightMappers = dtoLightMappers;
        GenericMapper.dtoMappers = dtoMappers;
    }

    public static <V, T> List<V> toListLightDTO(Collection<T> sourceList) {
        if (sourceList == null) {
            return Collections.emptyList();
        }
        return sourceList.stream().map(s -> ((V) toLightDTO(s))).collect(Collectors.toList());
    }

    public static <V, T> V toLightDTO(T source) {
        if (source != null) {
            Class voClass = dtoLightMappers.get(source.getClass());
            try {
                return (V) voClass.getConstructor(source.getClass()).newInstance(source);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <V, T> List<V> toListDTO(Collection<T> sourceList) {
        if (sourceList == null) {
            return Collections.emptyList();
        }
        return sourceList.stream().map(s -> ((V) toDTO(s))).collect(Collectors.toList());
    }

    public static <V, T> V toDTO(T source) {
        if (source != null) {
            Class voClass = dtoMappers.get(source.getClass());
            try {
                return (V) voClass.getConstructor(source.getClass()).newInstance(source);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
