package com.console.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReNameRedisException extends RedisConsoleException{
    String newKey;
    String newValue;

    public ReNameRedisException(String newKey, String newValue) {
        this.newKey = newKey;
        this.newValue = newValue;
    }

    @Override
    public String getMessage() {
        return "NewKey :: "+newKey+" is already exist => "+newValue;
    }
}
