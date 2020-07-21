package com.console.store.services;

import com.console.store.configs.servers.RedisIdentifier;
import com.console.store.dtos.RedisDataFormatDto;
import com.console.store.dtos.request.CreateValueInfo;
import com.console.store.dtos.request.ModifyValueAndTtlInfo;
import com.console.store.exceptions.InvalidRequestDataException;
import com.console.store.exceptions.NotFoundRedisValueException;
import com.console.store.exceptions.ReNameRedisException;
import com.console.store.exceptions.SetRedisDataException;

import java.util.List;
import java.util.Map;

public interface RedisService {
    /**
     * Redis 15개 DB count
     * @param identifier Redis 서버 정보
     * @return DB별 key count 정보가 담겨있는 Map
     */
    public Map<Integer, Long> getDbListAndCount(RedisIdentifier identifier);

    /**
     * 선택 DB 전체 Key 리스트
     * @param identifier Redis 서버 정보
     * @return Key List
     */
    public List<String> getAllKeys(RedisIdentifier identifier);

    /**
     * get value by key
     * @param identifier Redis 서버 정보
     * @param key 검색할 key
     * @return 검색한 key 정보
     * @throws NotFoundRedisValueException 존재 하지 않는 key
     */
    public RedisDataFormatDto getValueByKey(RedisIdentifier identifier, String key) throws NotFoundRedisValueException;

    /**
     * create data
     * @param identifier Redis 서버 정보
     * @param createValueInfo 생성할 redis 정보
     * @return 생성된 redis 정보
     * @throws SetRedisDataException set redis data fail
     * @throws InvalidRequestDataException redis 생성 정보 validation fail
     */
    public RedisDataFormatDto createData(RedisIdentifier identifier, CreateValueInfo createValueInfo)
            throws SetRedisDataException, InvalidRequestDataException;

    /**
     * delete key
     * @param identifier Redis 서버 정보
     * @param key 삭제할 key
     * @return 삭제 성공 여부
     */
    public Boolean deleteKey(RedisIdentifier identifier, String key);

    /**
     * modify value and ttl
     * @param identifier Redis 서버 정보
     * @param modifyValueAndTtlInfo Redis 서버 정보
     * @return 수정한 정보
     * @throws NotFoundRedisValueException 존재 하지 않는 key
     * @throws SetRedisDataException set redis data fail
     */
    public RedisDataFormatDto modifyValueAndTtl(RedisIdentifier identifier, ModifyValueAndTtlInfo modifyValueAndTtlInfo)
            throws NotFoundRedisValueException, SetRedisDataException;

    /**
     * Key 변경
     * @param identifier Redis 서버 정보
     * @param key target key
     * @param newKey 변경할 key
     * @return 변경한 key 정보
     * @throws SetRedisDataException set redis data fail
     * @throws NotFoundRedisValueException 존재 하지 않는 key
     * @throws ReNameRedisException rename 실패
     */
    public RedisDataFormatDto reName(RedisIdentifier identifier, String key, String newKey)
            throws SetRedisDataException, NotFoundRedisValueException, ReNameRedisException;
}
