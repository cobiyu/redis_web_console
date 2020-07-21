package com.console.store.repositories;


import java.util.List;
import java.util.Map;

public interface RedisRepository {
    /**
     * Redis 15개 DB count
     * @return DB별 key count 정보가 담겨있는 Map
     */
    public Map<Integer, Long> getDbSize();

    /**
     * 선택 DB 전체 Key 리스트
     * @return Key List
     */
    public List<String> getAllKeys();

    /**
     * get ttl by key
     * @param key 검색 key
     * @return 변경된 TTL
     */
    public String getValueByKey(String key);

    /**
     * get value by key
     * @param key 검색 key
     * @return value
     */
    public Long getTtlByKey(String key);

    /**
     * delete key
     * @param key 삭제할 key
     * @return 성공/실패 boolean
     */
    public Boolean deleteKey(String key);

    /**
     * set key with value
     * @param key set할 key
     * @param value set할 value
     * @return 성공/실패 boolean
     */
    public Boolean setValue(String key, String value);

    /**
     * modify ttl
     * @param key ttl 수정할 key
     * @param ttl 수정할 ttl 값
     * @return 성공/실패 boolean
     */
    public Boolean modifyTtl(String key, Long ttl);

    /**
     * key rename
     * @param key target key
     * @param newKey change key
     * @return 성공/실패 boolean
     */
    public Boolean reName(String key, String newKey);

    /**
     * remove ttl
     * @param key ttl 제거할 key
     * @return 성공/실패 boolean
     */
    public Boolean persist(String key);
}
