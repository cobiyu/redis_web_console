package com.console.store.configs.servers;

import lombok.*;

/**
 * Redis 서버 정보 DTO
 */
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class RedisServerInfo {
    @NonNull
    @Getter
    @Setter
    private String alias;
    @NonNull
    @Getter
    @Setter
    private String hosts;
    @NonNull
    @Getter
    @Setter
    private Integer port;
    @Getter
    @Setter
    private String password;

    public Boolean isValidInfo(){
        if(alias==null || alias.isEmpty()){
            return false;
        }
        if(hosts==null || hosts.isEmpty()){
            return false;
        }
        if(port==null || port <= 0){
            return false;
        }
        return true;
    }
}
