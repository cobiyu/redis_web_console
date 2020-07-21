package com.console.store.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 서버에서 취급하는 Http 상태 코드
 */
@NoArgsConstructor
@AllArgsConstructor
public enum RedisServerRestStatus {
    SUCCESS(200),
    NODATA(204),
    UNAUTH(401),
    INVALID_PARAM(400),
    INTERNAL_ERROR(500);

    @Getter
    private int code;

}
