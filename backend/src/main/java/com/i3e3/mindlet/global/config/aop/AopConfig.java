package com.i3e3.mindlet.global.config.aop;

import com.i3e3.mindlet.global.aop.trace.LogTrace;
import com.i3e3.mindlet.global.aop.trace.ThreadLocalLogTrace;
import com.i3e3.mindlet.global.aop.trace.aspect.LogTraceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AopConfig {

    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }
}
