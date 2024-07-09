package com.security.gateway.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
@Getter
@Setter
public class ApiError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String path;
    private Integer statusCode;
    private String exception;


    public ApiError() {
        timestamp = LocalDateTime.now();
    }


    public ApiError(HttpStatus httpStatus, RuntimeException ex, HttpServletRequest request) {
        this();
        this.status = httpStatus;
        this.statusCode = httpStatus.value();
        this.message = ex.getMessage();
        this.path = request.getRequestURI();
        this.exception = ex.getClass().getSimpleName();
    }
    public ApiError(HttpStatus httpStatus, Exception ex, HttpServletRequest request, String message) {
        this();
        this.status = httpStatus;
        this.statusCode = httpStatus.value();
        this.message = message;
        this.path = request.getRequestURI();
        this.exception = ex.getClass().getSimpleName();
    }

    public ApiError(HttpStatus httpStatus, RuntimeException ex, String requestURI) {
        this();
        this.status = httpStatus;
        this.statusCode = httpStatus.value();
        this.message = ex.getMessage();
        this.path = requestURI;
        this.exception = ex.getClass().getSimpleName();
    }

    public ApiError(HttpStatus httpStatus, RuntimeException ex, String requestURI, String exception) {
        this();
        this.status = httpStatus;
        this.statusCode = httpStatus.value();
        this.message = ex.getMessage();
        this.path = requestURI;
        this.exception = exception;
    }
}
