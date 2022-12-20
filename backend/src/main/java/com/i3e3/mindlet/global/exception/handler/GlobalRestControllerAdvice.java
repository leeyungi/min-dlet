package com.i3e3.mindlet.global.exception.handler;

import com.i3e3.mindlet.global.dto.ErrorResponseDto;
import com.i3e3.mindlet.global.exception.DuplicateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MailException.class)
    public ErrorResponseDto<Void> mailExceptionHandler(MailException e){
        logPrint(e);
        return ErrorResponseDto.<Void>builder()
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResponseDto<Void> illegalStateExceptionHandler(IllegalStateException e) {
        logPrint(e);
        return ErrorResponseDto.<Void>builder()
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponseDto<Void> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        logPrint(e);
        return ErrorResponseDto.<Void>builder()
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponseDto<Void> httpMessageNotReadableExceptionExceptionHandler(HttpMessageNotReadableException e) {
        logPrint(e);
        return ErrorResponseDto.<Void>builder()
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponseDto<Void> methodArgumentTypeMismatchExceptionExceptionHandler(MethodArgumentTypeMismatchException e) {
        logPrint(e);
        return ErrorResponseDto.<Void>builder()
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDto<Map<String, Object>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        logPrint(e);

        Map<String, Object> data = new HashMap<>();

        BindingResult bindingResult = e.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            Map<String, String> errorInfo = new HashMap<>();
            errorInfo.put("field", fieldError.getField());
            errorInfo.put("message", fieldError.getDefaultMessage());
            data.put("fieldError", errorInfo);
        }

        for (ObjectError globalError : bindingResult.getGlobalErrors()) {
            Map<String, String> errorInfo = new HashMap<>();
            errorInfo.put("field", globalError.getObjectName());
            errorInfo.put("message", globalError.getDefaultMessage());
            data.put("globalError", errorInfo);
        }

        return ErrorResponseDto.<Map<String, Object>>builder()
                .data(data)
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponseDto<Void> accessDeniedExceptionHandler(AccessDeniedException e) {
        logPrint(e);
        return ErrorResponseDto.<Void>builder()
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateException.class)
    public ErrorResponseDto<Void> duplicationExceptionHandler(DuplicateException e) {
        logPrint(e);
        return ErrorResponseDto.<Void>builder()
                .build();
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(IOException.class)
    public ErrorResponseDto<Void> iOExceptionHandler(IOException e) {
        logPrint(e);
        return ErrorResponseDto.<Void>builder()
                .build();
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponseDto<Void> noSuchElementExceptionHandler(NoSuchElementException e) {
        logPrint(e);
        return ErrorResponseDto.<Void>builder()
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponseDto<Void> exceptionHandler(Exception e) {
        logPrint(e);
        return ErrorResponseDto.<Void>builder()
                .build();
    }

    private void logPrint(Exception e) {
        log.error("[exceptionHandler] ex", e);
    }
}
