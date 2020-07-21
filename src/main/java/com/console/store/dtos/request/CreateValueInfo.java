package com.console.store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Redis 서버 Key 생성 정보
 */
@AllArgsConstructor
public class CreateValueInfo {
    @NotNull(message = "key must be not null")
    @NotBlank(message = "key must be not blank")
    @NonNull
    @Getter
    @Setter
    private String key;
    @Getter
    @Setter
    private String value;
    @Getter
    @Setter
    private Long ttl;
}
