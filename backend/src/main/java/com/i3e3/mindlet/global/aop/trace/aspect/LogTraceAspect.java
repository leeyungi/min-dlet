package com.i3e3.mindlet.global.aop.trace.aspect;

import com.i3e3.mindlet.global.aop.trace.LogTrace;
import com.i3e3.mindlet.global.aop.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Slf4j
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("execution(* com.i3e3.mindlet.domain..*(..))")
    public Object doTrace(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;

        try {
            String message = joinPoint.getSignature().toShortString();

            StringBuilder parameterMessage = new StringBuilder();
            String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
            Object[] args = joinPoint.getArgs();
            if (parameterNames != null && args != null) {
                parameterMessage.append(" parameters=[");
                int size = parameterNames.length;
                for (int i = 0; i < size; i++) {
                    parameterMessage.append("(").append(parameterNames[i]).append("=").append(args[i]).append(")");
                    if (i < size - 1) {
                        parameterMessage.append(", ");
                    }
                }
                parameterMessage.append("]");
            }

            status = logTrace.begin(message, parameterMessage.toString());

            Object result = joinPoint.proceed();

            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
