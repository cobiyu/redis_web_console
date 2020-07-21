package com.console.store.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * 서버 전체 response 규격
 * @param <T>
 */
public class RedisServerRestResponse <T>{
    @Setter
    @Getter
    private HttpStatus status;
    @Setter
    @Getter
    private String message;
    @Setter
    @Getter
    private T data;

    public RedisServerRestResponse() {
    }

    public RedisServerRestResponse(HttpStatus status) {
        this.status = status;
    }

    public RedisServerRestResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public RedisServerRestResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
