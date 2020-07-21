package com.console.store.factories;

import com.console.store.configs.RedisServerConfig;
import com.console.store.configs.servers.RedisIdentifier;
import com.console.store.dtos.RedisDataFormatDto;
import com.console.store.dtos.request.CreateValueInfo;
import com.console.store.dtos.request.ModifyValueAndTtlInfo;
import com.console.store.exceptions.InvalidRequestDataException;
import com.console.store.exceptions.NotFoundRedisValueException;
import com.console.store.exceptions.ReNameRedisException;
import com.console.store.exceptions.SetRedisDataException;
import com.console.store.services.RedisService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * application.yml의 Redis 서버 및 DB정보를 기반으로 등록된 Bean을 관리하는 Service Proxy 객체
 * proxy가 반환해준 service를 그대로 실행
 */
@Component("redisServiceProxy")
public class RedisServiceProxy implements RedisService {
    /**
     * application.yml의 redis-server의 정보를 바탕으로
     * 으로 등록된 각 Service 들을 가지고 있는 Map
     * 해당 Map은 RedisIdentifier를 key로 가짐
     */
    private final Map<RedisIdentifier, RedisService> redisServiceMap;

    /**
     * constructor
     * @param redisServiceMap {@link RedisServerConfig#redisServiceMap() redisServiceMap} 참조
     */
    public RedisServiceProxy(Map<RedisIdentifier, RedisService> redisServiceMap) {
        this.redisServiceMap = redisServiceMap;
    }

    /**
     * RedisIdentifier를 이용해 맞는 service를 return
     * @param identifier Redis 서버 정보
     * @return RedisIdentifier에 맞는 Service 객체
     */
    private RedisService getServiceByIndentifier(RedisIdentifier identifier)  {
        return redisServiceMap.get(identifier);
    }

    /**
     * @param identifier Redis 서버 정보
     * @return {@link com.console.store.services.RedisServiceImpl#getDbListAndCount(RedisIdentifier) getDbListAndCount} 참조
     */
    @Override
    public Map<Integer, Long> getDbListAndCount(RedisIdentifier identifier) {
        return getServiceByIndentifier(identifier).getDbListAndCount(identifier);
    }

    /**
     * @param identifier Redis 서버 정보
     * @return {@link com.console.store.services.RedisServiceImpl#getAllKeys(RedisIdentifier) getAllKeys} 참조
     */
    @Override
    public List<String> getAllKeys(RedisIdentifier identifier)  {
        return getServiceByIndentifier(identifier).getAllKeys(identifier);
    }

    /**
     * @param identifier Redis 서버 정보
     * @param key 검색할 key
     * @return {@link com.console.store.services.RedisServiceImpl#getValueByKey(RedisIdentifier, String) getValueByKey} 참조
     */
    @Override
    public RedisDataFormatDto getValueByKey(RedisIdentifier identifier, String key)
            throws NotFoundRedisValueException {
        return this.getServiceByIndentifier(identifier).getValueByKey(identifier, key);
    }

    /**
     * @param identifier Redis 서버 정보
     * @param createValueInfo 생성할 redis 정보
     * @return {@link com.console.store.services.RedisServiceImpl#createData(RedisIdentifier, CreateValueInfo) createData 참조}
     */
    @Override
    public RedisDataFormatDto createData(RedisIdentifier identifier, CreateValueInfo createValueInfo)
            throws SetRedisDataException, InvalidRequestDataException {
        return this.getServiceByIndentifier(identifier).createData(identifier, createValueInfo);
    }

    /**
     * @param identifier Redis 서버 정보
     * @param key 삭제할 key
     * @return {@link com.console.store.services.RedisServiceImpl#deleteKey(RedisIdentifier, String) deleteKey 참조}
     */
    @Override
    public Boolean deleteKey(RedisIdentifier identifier, String key) {
        return this.getServiceByIndentifier(identifier).deleteKey(identifier, key);
    }

    /**
     * @param identifier Redis 서버 정보
     * @param modifyValueAndTtlInfo Redis 서버 정보
     * @return {@link com.console.store.services.RedisServiceImpl#modifyValueAndTtl(RedisIdentifier, ModifyValueAndTtlInfo)  modifyValueAndTtl 참조}
     */
    @Override
    public RedisDataFormatDto modifyValueAndTtl(RedisIdentifier identifier,
                                                ModifyValueAndTtlInfo modifyValueAndTtlInfo)
            throws NotFoundRedisValueException, SetRedisDataException {
        return this.getServiceByIndentifier(identifier).modifyValueAndTtl(identifier, modifyValueAndTtlInfo);
    }

    /**
     * @param identifier Redis 서버 정보
     * @param key target key
     * @param newKey 변경할 key
     * @return {@link com.console.store.services.RedisServiceImpl#reName(RedisIdentifier, String, String)  reName 참조}
     */
    @Override
    public RedisDataFormatDto reName(RedisIdentifier identifier, String key, String newKey)
            throws SetRedisDataException, ReNameRedisException, NotFoundRedisValueException {
        return this.getServiceByIndentifier(identifier).reName(identifier, key, newKey);
    }
}
