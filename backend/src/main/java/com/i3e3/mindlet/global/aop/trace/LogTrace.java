package com.i3e3.mindlet.global.aop.trace;

public interface LogTrace {

    TraceStatus begin(String message, String parameterInfo);

    void end(TraceStatus status);

    void exception(TraceStatus status, Exception e);
}
