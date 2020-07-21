package com.console.store.utilities;


import com.console.store.dtos.response.RedisServerRestResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

/**
 * 공통으로 사용할 response에 대한 helper 클래스
 */
@UtilityClass
public class ResponseUtility {
    static public RedisServerRestResponse response() {
        return new RedisServerRestResponse(HttpStatus.OK);
    }

    static public RedisServerRestResponse<String> response(String msg) {
        return new RedisServerRestResponse<String>(HttpStatus.OK, msg);
    }

    static public <T> RedisServerRestResponse<T> response(T data) {
        return new RedisServerRestResponse<T>(HttpStatus.OK, "SUCCESS", data);
    }
}
