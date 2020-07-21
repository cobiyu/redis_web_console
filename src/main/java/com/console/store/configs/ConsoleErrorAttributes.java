package com.console.store.configs;

import com.console.store.dtos.response.RedisServerRestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * 공통된 에러 규격으로 handling
 */
@Configuration
public class ConsoleErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Throwable error = getError(webRequest);
        ObjectMapper objectMapper = new ObjectMapper();

        RedisServerRestResponse redisServerRestResponse = new RedisServerRestResponse();
        int status = (int) webRequest.getAttribute("javax.servlet.error.status_code", RequestAttributes.SCOPE_REQUEST);
        redisServerRestResponse.setStatus(HttpStatus.valueOf(status));
        redisServerRestResponse.setMessage(error.getMessage());

        return objectMapper.convertValue(redisServerRestResponse, Map.class);
    }
}
