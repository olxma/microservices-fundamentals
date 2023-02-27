package com.epam.microservices.resourseservice.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ResourceData {

    private static final String DEFAULT_CONTENT_TYPE = "audio/mpeg";
    private static final String DEFAULT_CONTENT_DISPOSITION_PREFIX = "attachment; filename=";

    private String location;
    private String fileName;
    private byte[] content;

    public HttpHeaders getMetadataAsHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, DEFAULT_CONTENT_DISPOSITION_PREFIX + fileName);
        headers.add(HttpHeaders.CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
        return headers;
    }
}
