package com.console.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RedisConsoleException extends Exception {
    public RedisConsoleException() {
        super();
    }

    public RedisConsoleException(String message) {
        super(message);
    }

    public RedisConsoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisConsoleException(Throwable cause) {
        super(cause);
    }

    public RedisConsoleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
