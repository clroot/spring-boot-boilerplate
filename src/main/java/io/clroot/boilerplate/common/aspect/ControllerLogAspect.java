package io.clroot.boilerplate.common.aspect;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class ControllerLogAspect {
    @Around("execution(* io.clroot.boilerplate..*Controller.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
            .getRequest();
        List<Object> args = Stream.of(joinPoint.getArgs())
            .filter(Objects::nonNull)
            .filter(arg -> !(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse))
            .toList();

        String controller = joinPoint.getTarget().getClass().getSimpleName();

        String method = request.getMethod();
        String path = request.getRequestURI();
        String addr = request.getRemoteAddr();

        log.info("########################################");
        log.info("# REQUEST | CONTROLLER = {} | METHOD = {} | PATH = {} | REMOTE_ADDR = {} | IN_PARAMS = {}",
            controller, method, path, addr, args);
        log.info("########################################");

        return joinPoint.proceed();
    }
}
