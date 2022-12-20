package com.i3e3.mindlet.global.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(basePackages = "com.i3e3.mindlet.domain.admin")
public class GlobalControllerAdvice {

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e) {
        log.error("Exception 예외 발생!!", e);
        return "error/500";
    }
}
