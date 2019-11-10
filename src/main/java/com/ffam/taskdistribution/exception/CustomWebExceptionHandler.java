package com.ffam.taskdistribution.exception;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.EncoderHttpMessageWriter;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.ModelMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import reactor.core.publisher.Mono;

public class CustomWebExceptionHandler implements WebExceptionHandler {
    
    public static final String ERROR_ATTRIBUTES = "error_attributes";
    private static HttpMessageWriter<Object> writer = new EncoderHttpMessageWriter(new Jackson2JsonEncoder());
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomWebExceptionHandler.class);

    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        ThrowableTranslator translator = new ThrowableTranslator(throwable);
        if (translator.getStatus(throwable).equals(HttpStatus.NOT_FOUND)) {
            LOGGER.error("HttpStatus.NOT_FOUND in CustomWebExceptionHandler {}", throwable.getMessage());
        } else {
            LOGGER.error("Unhandled Exception in CustomWebExceptionHandler", throwable);
        }
        Exception ex = throwable instanceof Exception ? (Exception)throwable : new RuntimeException("Unhandled error.", throwable);
        return errorResponseWriter(exchange, ex, writer, translator.getStatus(throwable), "task-distribution");
    }
    
    public static Mono<Void> errorResponseWriter(ServerWebExchange exchange, Exception exception, HttpMessageWriter<Object> writer, HttpStatus httpStatus, String app) {
        LOGGER.debug("errorResponseWriter rendering error response");
        
        if (exception instanceof AccessDeniedException) {
            httpStatus = HttpStatus.FORBIDDEN;
        }
        if (exception instanceof AuthenticationException) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        
        if(Objects.isNull(httpStatus)) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        
        ServerHttpResponse response = processResponse(exchange, app, httpStatus);
        response.getStatusCode();

        Map<String, ?> model = getErrorAttributes(exchange.getRequest(), exception, app, httpStatus);
        Object value = model.get(ERROR_ATTRIBUTES);

        Publisher<?> input = Mono.justOrEmpty(value);
        ResolvableType elementType = ResolvableType.forClass(value.getClass());
        return writer.write(input, elementType, MediaType.APPLICATION_JSON, response, Collections.emptyMap());
    }
    
    /**
     * Process response.
     * The response header is populated with respective content type
     * and Realm for the authentication exception.
     *
     * @param exchange
     *            the exchange
     * @param app
     *            the app
     * @param httpStatus
     *            the http status
     * @return the server http response
     */
    private static ServerHttpResponse processResponse(ServerWebExchange exchange, String app, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().set(HttpHeaders.WWW_AUTHENTICATE, String.join("=", "", app));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response;
    }
    
    /**
     * Gets the error attributes.
     * Creates a Map of error details like
     * time stamp, path status error etc.
     * and sends it back as error attributes 
     * 
     * @param request
     *            the request
     * @param exception
     *            the exception
     * @param app
     *            the app
     * @param httpStatus
     *            the http status
     * @return the error attributes
     */
    private static Map<String, Object> getErrorAttributes(ServerHttpRequest request, Exception exception, String app, HttpStatus httpStatus) {
        
        ModelMap model = new ExtendedModelMap();
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", new Date());
        errorAttributes.put("path", request.getPath().value());
        errorAttributes.put("status", httpStatus.value());
        errorAttributes.put("error", httpStatus.name());
        errorAttributes.put("message", exception.getMessage());
        errorAttributes.put("application", app);

        model.addAttribute(ERROR_ATTRIBUTES, errorAttributes);
        return model;
    }
    
}
