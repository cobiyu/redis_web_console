package com.console.store.exceptions;


import com.console.store.log.LogInfo;
import com.console.store.log.RedisConsoleLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.event.Level;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * exception handler
 */
@Component
public class ExceptionResolverForLogging implements HandlerExceptionResolver {
    private List<RedisConsoleLogger> redisConsoleLoggerList;

    public ExceptionResolverForLogging(List<RedisConsoleLogger> redisConsoleLoggerList) {
        this.redisConsoleLoggerList = redisConsoleLoggerList;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        Map<String, Object> errorMap = getErrorMap(request, ex);
        ObjectMapper mapper = new ObjectMapper();
        String logMsg = null;
        try {
            logMsg = mapper.writeValueAsString(errorMap);
        } catch (JsonProcessingException e) {
            /// do nothing
        }
        ResponseStatus rs = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

        if(rs == null) {
            errorLogging(logMsg);
        } else if(HttpStatus.INTERNAL_SERVER_ERROR.equals(rs.value())) {
            errorLogging(logMsg);
        } else if(HttpStatus.BAD_REQUEST.equals(rs.value())) {
            infoLogging(logMsg);
        } else if(HttpStatus.NOT_FOUND.equals(rs.value())) {
            infoLogging(logMsg);
        } else if(HttpStatus.NO_CONTENT.equals(rs.value())) {
            infoLogging(logMsg);
        } else {
            errorLogging(logMsg);
        }

        return null;
    }

    private Map<String, Object> getErrorMap(HttpServletRequest request, Exception ex) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("exception_name", ex.getClass().getName());
        errorMap.put("error_msg", ex.getMessage());
        errorMap.put("uri", request.getRequestURI());
        errorMap.put("method", request.getMethod());
        errorMap.put("all_parameter", getRequestParameterMap(request));
        errorMap.put("all_headers", getRequestHeaderMap(request));
        errorMap.put("stack_trace", ex.getStackTrace());

        return errorMap;
    }

    private Map<String, Object> getRequestParameterMap(HttpServletRequest request) {
        Map<String, Object> requestParameterMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            Object value = request.getParameter(name);
            requestParameterMap.put(name, value);
        }

        return requestParameterMap;
    }

    private Map<String, Object> getRequestHeaderMap(HttpServletRequest request) {
        Map<String, Object> requestHeaderMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            Object value = request.getHeader(name);
            requestHeaderMap.put(name, value);
        }

        return requestHeaderMap;
    }

    private void errorLogging(String msg){
        iterateLogging(msg, Level.ERROR);
    }

    private void warnLogging(String msg){
        iterateLogging(msg, Level.WARN);
    }

    private void infoLogging(String msg){
        iterateLogging(msg, Level.INFO);
    }

    private void debugLogging(String msg){
        iterateLogging(msg, Level.DEBUG);
    }

    private void traceLogging(String msg){
        iterateLogging(msg, Level.TRACE);
    }

    private void iterateLogging(String msg, Level level){
        if(redisConsoleLoggerList != null){
            for (RedisConsoleLogger redisConsoleLogger : redisConsoleLoggerList) {
                redisConsoleLogger.logging(new LogInfo(level, msg));
            }
        }
    }
}
