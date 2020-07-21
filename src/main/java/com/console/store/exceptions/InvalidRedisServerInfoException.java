package com.console.store.exceptions;

import com.console.store.configs.servers.RedisServerInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidRedisServerInfoException extends RedisConsoleException{


    public InvalidRedisServerInfoException(RedisServerInfo redisServerInfo) {
        super(redisServerInfo.toString()+ " is InvalidRedisServerInfo");
    }

}
