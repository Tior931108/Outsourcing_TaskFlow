package com.example.outsourcing_taskflow.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceLoggingAspect {

    @Around("@within(com.example.outsourcing_taskflow.common.annotaion.MeasureAllMethods)")
    public Object logExecutionTimeForAllMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            long elapsed = end - start;
            String methodName = joinPoint.getSignature().toShortString();
            log.info("[RUNTIME] {} 실행시간: {} ms", methodName, elapsed);
        }
    }
}