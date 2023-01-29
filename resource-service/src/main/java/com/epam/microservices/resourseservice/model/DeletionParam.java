package com.epam.microservices.resourseservice.model;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;

import static java.util.Optional.ofNullable;

@Validated
public class DeletionParam {

    private static final String SEPARATOR = ",";
    private static final int MAX_LENGTH = 199;
    private static final String ERR_MESSAGE = "Valid CSV length for 'id' param < 200 characters";

    @Length(max = MAX_LENGTH, message = ERR_MESSAGE)
    private final String csv;

    @Getter
    private final List<Integer> ids;

    public DeletionParam(String csv) {
        this.csv = csv;
        this.ids = ofNullable(csv).map(s ->
                        Arrays.stream(s.split(SEPARATOR)).map(Integer::parseInt).toList())
                .orElse(List.of());
    }
}
