package com.ffam.taskdistribution.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class ErrorHandler {

    private static final String ERROR_RAISED = "error raised";
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

   public Mono<ServerResponse> throwableError(final Throwable error) {
        LOGGER.error(ERROR_RAISED, error);
        return Mono.just(error).transform(this::getResponse);
    }

    <T extends Throwable> Mono<ServerResponse> getResponse(final Mono<T> monoError) {
       return monoError.transform(ThrowableTranslator::translate)
                .flatMap(translation -> ServerResponse
                        .status(translation.getHttpStatus()).contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(Mono.just(getErrorResponse(translation.getMessage(), translation.getExceptionClassName(), translation.getHttpStatus().value())), ErrorResponse.class));
  }

    private ErrorResponse getErrorResponse(String message, String className, int status) {
        return ErrorResponse.error().timestamp().status(status).error(HttpStatus.valueOf(status).getReasonPhrase()).exception(className).message(message).build();
    }


}
