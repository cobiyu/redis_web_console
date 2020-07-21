package com.console.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SetRedisDataException extends RedisConsoleException {
    private SetRedisDataExceptionType setRedisDataExceptionType;

    public SetRedisDataException(SetRedisDataExceptionType setRedisDataExceptionType){
        super("SetRedisDataException occured in " + setRedisDataExceptionType);
        this.setRedisDataExceptionType = setRedisDataExceptionType;
    }

    public SetRedisDataExceptionType getSetRedisDataExceptionType() {
        return setRedisDataExceptionType;
    }
}
