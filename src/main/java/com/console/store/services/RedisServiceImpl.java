package com.console.store.services;

import com.console.store.configs.servers.RedisIdentifier;
import com.console.store.dtos.RedisDataFormatDto;
import com.console.store.dtos.request.CreateValueInfo;
import com.console.store.dtos.request.ModifyValueAndTtlInfo;
import com.console.store.exceptions.*;
import com.console.store.repositories.RedisRepository;

import java.util.List;
import java.util.Map;

public class RedisServiceImpl implements RedisService{
    /**
     * Redis 명령 repository
     */
    private final RedisRepository redisRepository;

    /**
     * @param redisRepository Redis 명령 repository
     */
    public RedisServiceImpl(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    /**
     * Redis 15개 DB count
     * @param identifier Redis 서버 정보
     * @return DB별 key count 정보가 담겨있는 Map
     */
    @Override
    public Map<Integer, Long> getDbListAndCount(RedisIdentifier identifier) {
        return redisRepository.getDbSize();
    }

    /**
     * 선택 DB 전체 Key 리스트
     * @param identifier Redis 서버 정보
     * @return Key List
     */
    @Override
    public List<String> getAllKeys(RedisIdentifier identifier) {
        return redisRepository.getAllKeys();
    }

    /**
     * get value by key
     * @param identifier Redis 서버 정보
     * @param key 검색할 key
     * @return 검색한 key 정보
     * @throws NotFoundRedisValueException 존재 하지 않는 key
     */
    @Override
    public RedisDataFormatDto getValueByKey(
            RedisIdentifier identifier,
            String key) throws NotFoundRedisValueException {
        String value = redisRepository.getValueByKey(key);

        if(value == null){
            throw new NotFoundRedisValueException(identifier, key);
        }
        return RedisDataFormatDto.builder()
                .key(key)
                .value(value)
                .ttl(redisRepository.getTtlByKey(key))
                .build();
    }

    /**
     * create data
     * @param identifier Redis 서버 정보
     * @param createValueInfo 생성할 redis 정보
     * @return 생성된 redis 정보
     * @throws SetRedisDataException set redis data fail
     * @throws InvalidRequestDataException redis 생성 정보 validation fail
     */
    @Override
    public RedisDataFormatDto createData(RedisIdentifier identifier, CreateValueInfo createValueInfo)
            throws SetRedisDataException, InvalidRequestDataException {

        String value = redisRepository.getValueByKey(createValueInfo.getKey());
        if(value!=null){
            throw new InvalidRequestDataException(identifier, createValueInfo, true);
        }

        if(! redisRepository.setValue(createValueInfo.getKey(), createValueInfo.getValue()) ){
            throw new SetRedisDataException(SetRedisDataExceptionType.SET_VALUE);
        }

        if(! redisRepository.modifyTtl(createValueInfo.getKey(), createValueInfo.getTtl()) ){
            throw new SetRedisDataException(SetRedisDataExceptionType.SET_TTL);
        }

        return RedisDataFormatDto.builder()
                .key(createValueInfo.getKey())
                .ttl(createValueInfo.getTtl())
                .ttl(createValueInfo.getTtl())
                .build();
    }

    /**
     * delete key
     * @param identifier Redis 서버 정보
     * @param key 삭제할 key
     * @return 삭제 성공 여부
     */
    @Override
    public Boolean deleteKey(RedisIdentifier identifier, String key) {
        return redisRepository.deleteKey(key);
    }

    /**
     * modify value and ttl
     * @param identifier Redis 서버 정보
     * @param modifyValueAndTtlInfo Redis 서버 정보
     * @return 수정한 정보
     * @throws NotFoundRedisValueException 존재 하지 않는 key
     * @throws SetRedisDataException set redis data fail
     */
    @Override
    public RedisDataFormatDto modifyValueAndTtl(
            RedisIdentifier identifier,
            ModifyValueAndTtlInfo modifyValueAndTtlInfo) throws   NotFoundRedisValueException,
                                                            SetRedisDataException {

        if(redisRepository.getValueByKey(modifyValueAndTtlInfo.getKey())==null){
            throw new NotFoundRedisValueException(identifier, modifyValueAndTtlInfo.getKey());
        }

        if(modifyValueAndTtlInfo.isSetValue()){
            if( !redisRepository.setValue(modifyValueAndTtlInfo.getKey(), modifyValueAndTtlInfo.getValue()) ){
                throw new SetRedisDataException(SetRedisDataExceptionType.SET_VALUE);
            }
        }

        if(modifyValueAndTtlInfo.isSetTtl()){
            if(modifyValueAndTtlInfo.isPersist()){
                if(!redisRepository.persist(modifyValueAndTtlInfo.getKey())){
                    throw new SetRedisDataException(SetRedisDataExceptionType.SET_TTL);
                }
            } else{
                if(!redisRepository.modifyTtl(modifyValueAndTtlInfo.getKey(), modifyValueAndTtlInfo.getTtl())){
                    throw new SetRedisDataException(SetRedisDataExceptionType.SET_TTL);
                }
            }
        }

        return RedisDataFormatDto.builder()
                .key(modifyValueAndTtlInfo.getKey())
                .value(modifyValueAndTtlInfo.getValue())
                .ttl(modifyValueAndTtlInfo.getTtl())
                .build();
    }

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
    @Override
    public RedisDataFormatDto reName(
            RedisIdentifier identifier,
            String key,
            String newKey) throws SetRedisDataException, NotFoundRedisValueException, ReNameRedisException {

        String value = redisRepository.getValueByKey(key);
        String checkNewValue = redisRepository.getValueByKey(newKey);

        if(value == null){
            throw new NotFoundRedisValueException(identifier, key);
        }
        if(checkNewValue != null){
            throw new ReNameRedisException(newKey, checkNewValue);
        }


        boolean result = redisRepository.reName(key, newKey);
        if(!result){
            throw new SetRedisDataException(SetRedisDataExceptionType.RENAME);
        }

        return RedisDataFormatDto.builder()
                .key(newKey)
                .value(redisRepository.getValueByKey(newKey))
                .ttl(redisRepository.getTtlByKey(newKey))
                .build();
    }
}
