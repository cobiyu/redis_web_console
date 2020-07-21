package com.console.store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Redis 서버 key, value 수정 정보
 */
@AllArgsConstructor
public class ModifyValueAndTtlInfo {
    @Getter
    @Setter
    private String key;
    @Getter
    @Setter
    private String value;
    @Getter
    @Setter
    private Long ttl;

    public boolean isSetValue(){
        return value != null;
    }

    public boolean isSetTtl(){
        return ttl != null;
    }

    public boolean isPersist(){
        if(!isSetTtl()){
            return false;
        }
        return ttl == -1;
    }
}
