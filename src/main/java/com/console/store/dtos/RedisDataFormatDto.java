package com.console.store.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Redis Key 관련 정보 규격
 */
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class RedisDataFormatDto {
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

    public boolean isValidData(){
        if(key==null){
            return false;
        }
        if(value==null){
            return false;
        }
        if(ttl==null){
            return false;
        }
        return true;
    }
}
