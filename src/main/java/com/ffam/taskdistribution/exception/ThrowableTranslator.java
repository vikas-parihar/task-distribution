package com.ffam.taskdistribution.exception;

import java.security.InvalidParameterException;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import javassist.NotFoundException;
import reactor.core.publisher.Mono;

public class ThrowableTranslator {

    private final HttpStatus httpStatus;
    private final String message;
    private final String exceptionClassName;

    public ThrowableTranslator(final Throwable throwable) {
        this.httpStatus = getStatus(throwable);
        this.message = throwable.getMessage();
        this.exceptionClassName = throwable.getClass().getName();
    }

    public HttpStatus getStatus(final Throwable error) {
        if (error instanceof InvalidParameterException) {
            return HttpStatus.BAD_REQUEST;
        } else if (error instanceof WebClientResponseException) {
            return ((WebClientResponseException) error).getStatusCode();
        } else if (error instanceof NotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    HttpStatus getHttpStatus() {
        return httpStatus;
    }

    String getMessage() {
        return message;
    }

    String getExceptionClassName() {
        return exceptionClassName;
    }

    static <T extends Throwable> Mono<ThrowableTranslator> translate(final Mono<T> throwable) {
        return throwable.flatMap(error -> Mono.just(new ThrowableTranslator(error)));
    }
}
