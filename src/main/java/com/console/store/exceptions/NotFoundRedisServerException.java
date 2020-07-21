package com.console.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundRedisServerException extends RedisConsoleException {

    public NotFoundRedisServerException(String alias) {
        super(alias+" is not found server alias");
    }

    public NotFoundRedisServerException(String alias, Throwable t) {
        super(alias+" is not found server alias", t);
    }
}
