package com.console.store.exceptions;

import com.console.store.configs.servers.RedisServerInfoListLoader;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotExistRedisServerListException extends RedisConsoleException {
    public NotExistRedisServerListException() {
        super( "Not exist redis server, Please check "
                + RedisServerInfoListLoader.class.toString()
                +" 's implementation"
        );
    }
}
