package com.epam.microservices.resourseservice.converter;

import com.epam.microservices.resourseservice.model.DeletionParam;
import org.springframework.core.convert.converter.Converter;

public class StringToDeletionParamConverter implements Converter<String, DeletionParam> {

    @Override
    public DeletionParam convert(String source) {
        return new DeletionParam(source);
    }
}
