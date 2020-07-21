package com.console.store.configs.servers;

import lombok.*;

/**
 * redis 서버 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class RedisIdentifier {
    @NonNull
    private String alias;

    private int db;
}
