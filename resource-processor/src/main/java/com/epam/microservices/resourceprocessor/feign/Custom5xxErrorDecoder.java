package com.epam.microservices.resourceprocessor.feign;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

public class Custom5xxErrorDecoder implements ErrorDecoder {

    private static final int INTERNAL_SERVER_ERROR = 500;

    @Override
    public FeignException decode(String methodKey, Response response) {
        FeignException exception = feign.FeignException.errorStatus(methodKey, response);
        int status = response.status();
        if (status >= INTERNAL_SERVER_ERROR) {
            exception = buildRetryableException(response, exception);
        }
        return exception;
    }

    private FeignException buildRetryableException(Response response, FeignException exception) {
        return new RetryableException(
                response.status(),
                exception.getMessage(),
                response.request().httpMethod(),
                exception,
                null,
                response.request());
    }
}
