package com.console.store.repositories;

import com.console.store.configs.servers.RedisIdentifier;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LettuceRedisRepository implements RedisRepository{
    private static final int DB_COUNT = 16;
    private static final String OK_MSG = "OK";
    private static final int ITERATOR_COUNT = 1000;

    private RedisCommands<String, String> redisCommands;
    private RedisIdentifier redisIdentifier;

    public LettuceRedisRepository(RedisCommands<String, String> redisCommands, RedisIdentifier redisIdentifier) {
        this.redisCommands = redisCommands;
        this.redisIdentifier = redisIdentifier;
    }

    /**
     * Redis 15개 DB count
     * @return DB별 key count 정보가 담겨있는 Map
     */
    @Override
    public Map<Integer, Long> getDbSize() {
        Map<Integer, Long> dbMap = new HashMap<>();
        for(int db = 0; db < DB_COUNT; db++){
            redisCommands.select(db);
            dbMap.put(db, redisCommands.dbsize());
        }
        redisCommands.select(redisIdentifier.getDb());
        return dbMap;
    }

    /**
     * 선택 DB 전체 Key 리스트
     * redis keys의 성능 이슈로 인하여 scan 을 iterate 해서 리스트 추출
     * @return Key List
     */
    @Override
    public List<String> getAllKeys() {
        ScanCursor scanCursor = new ScanCursor("0", false);

        ScanArgs scanArgs = ScanArgs.Builder
                .matches("*")
                .limit(ITERATOR_COUNT);

        KeyScanCursor<String> keyScanCursor = redisCommands.scan(scanCursor, scanArgs);
        List<String> allKeyList = keyScanCursor.getKeys();

        while (!keyScanCursor.isFinished()){
            keyScanCursor = redisCommands.scan(keyScanCursor, scanArgs);
            allKeyList.addAll(keyScanCursor.getKeys());
        }

        return allKeyList;
    }

    /**
     * get ttl by key
     * @param key 검색 key
     * @return 변경된 TTL
     */
    @Override
    public Long getTtlByKey(String key) {
        return redisCommands.ttl(key);
    }

    /**
     * get value by key
     * @param key 검색 key
     * @return value
     */
    @Override
    public String getValueByKey(String key) {
        return redisCommands.get(key);
    }

    /**
     * delete key
     * @param key 삭제할 key
     * @return 성공/실패 boolean
     */
    @Override
    public Boolean deleteKey(String key) {
        return redisCommands.del(
                key
        ) == 1;
    }

    /**
     * set key with value
     * @param key set할 key
     * @param value set할 value
     * @return 성공/실패 boolean
     */
    @Override
    public Boolean setValue(String key, String value) {
        return redisCommands.set(
                key,
                value
        ).equals(OK_MSG);
    }

    /**
     * modify ttl
     * @param key ttl 수정할 key
     * @param ttl 수정할 ttl 값
     * @return 성공/실패 boolean
     */
    @Override
    public Boolean modifyTtl(String key, Long ttl) {
        return redisCommands.expire(
                key,
                ttl
        );
    }

    /**
     * key rename
     * @param key target key
     * @param newKey change key
     * @return 성공/실패 boolean
     */
    @Override
    public Boolean reName(String key, String newKey) {
        return redisCommands.rename(
                key,
                newKey
        ).equals(OK_MSG);
    }

    /**
     * remove ttl
     * @param key ttl 제거할 key
     * @return 성공/실패 boolean
     */
    @Override
    public Boolean persist(String key) {
        return redisCommands.persist(
                key
        );
    }
}
