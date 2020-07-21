package com.console.store.exceptions;

import com.console.store.configs.servers.RedisIdentifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NotFoundRedisValueException extends RedisConsoleException {
    private RedisIdentifier identifier;
    private String key;

    public NotFoundRedisValueException(RedisIdentifier identifier, String key) {
        super();
        this.identifier = identifier;
        this.key = key;
    }

    @Override
    public String getMessage() {
        String message = "Not Found Redis value [ RedisIdentifer : " + identifier.toString()
                + "\n key : " + key.toString() + "]";

        return message;
    }
}
