package com.security.gateway.exception.handler;

import com.security.gateway.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.hibernate.HibernateException;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestExceptionHandler extends ResponseEntityExceptionHandler 
{



    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


    @ExceptionHandler(EntityException.class)
    protected ResponseEntity<Object> handleEntityExists(HttpServletRequest request,
                                                        EntityException ex) {

        ApiError apiError = new ApiError(BAD_REQUEST, ex, request);
        LogSupport.logError(ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(VerifyTokenException.class)
    protected ResponseEntity<Object> handleVerifyTokenException(HttpServletRequest request,
                                                        VerifyTokenException ex) {
        ApiError apiError = new ApiError(CONFLICT, ex, request);
        LogSupport.logError(ex);
        return buildResponseEntity(apiError);
    }
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(HttpServletRequest request,
                                                                   AuthenticationException ex) {
        if (ex instanceof DisabledException) {
            ApiError apiError = new ApiError(UNAUTHORIZED, ex, request);
            apiError.setMessage("We sent you an email. Please confirm your email address.");
            LogSupport.logError(ex);
            return buildResponseEntity(apiError);
        }

        ApiError apiError = new ApiError(UNAUTHORIZED, ex, request);
        LogSupport.logError(ex);
        return buildResponseEntity(apiError);
    }



    @ExceptionHandler(EmailException.class)
    protected ResponseEntity<Object> handleEmailExists(HttpServletRequest request,
                                                       EmailException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex, request);
        LogSupport.logError(ex);
        return buildResponseEntity(apiError);
    }
    @ExceptionHandler(ResetPasswordCodeException.class)
    protected ResponseEntity<Object> handleResetPasswordCodeException(HttpServletRequest request,
                                                       ResetPasswordCodeException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex, request);

        LogSupport.logError(ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<Object> handleIllegalStateException(HttpServletRequest request,
                                                                 IllegalStateException ex) {
        ApiError apiError = new ApiError(UNAUTHORIZED, ex, request);
        LogSupport.logError(ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(HibernateException.class)
    protected ResponseEntity<Object> handlePropertyValue(HttpServletRequest request,
                                                         HibernateException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex, request);
        String message;
        if (ex instanceof PropertyValueException ) {
            PropertyValueException e = (PropertyValueException) ex;
            message = MessageFormat.format("PropertyValueException:  {0} , {1}", e.getEntityName(), e.getPropertyName());
            apiError = new ApiError(NOT_ACCEPTABLE, e, request, message);
        } else if (ex instanceof ConstraintViolationException ) {
            ConstraintViolationException e = (ConstraintViolationException) ex;
            message = MessageFormat.format("ConstraintViolationException:  {0} ", e.getConstraintName());
            apiError = new ApiError(CONFLICT, e, request, message);
        }
        LogSupport.logError(ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> processRuntimeException(HttpServletRequest request, RuntimeException ex) {
        if (ex instanceof ExpiredJwtException) {

            ApiError apiError = new ApiError(FORBIDDEN, ex, request.getRequestURI());

            LogSupport.logError(ex);
            return buildResponseEntity(apiError);

        } else if (ex instanceof JwtException | ex instanceof BadCredentialsException) {
            ApiError apiError = new ApiError(FORBIDDEN, ex, request.getRequestURI(), "BadCredentials");
            LogSupport.logError(ex);
            return buildResponseEntity(apiError);
        } else {

            ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex, request.getRequestURI());
            LogSupport.logError(ex);
            return buildResponseEntity(apiError);
        }


    }





}

