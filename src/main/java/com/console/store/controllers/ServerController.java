package com.console.store.controllers;


import com.console.store.configs.servers.RedisIdentifier;
import com.console.store.configs.servers.RedisServerInfoListLoaderByYaml;
import com.console.store.dtos.RedisDataFormatDto;
import com.console.store.dtos.request.CreateValueInfo;
import com.console.store.dtos.request.ModifyValueAndTtlInfo;
import com.console.store.dtos.response.RedisServerRestResponse;
import com.console.store.exceptions.InvalidRequestDataException;
import com.console.store.exceptions.NotFoundRedisValueException;
import com.console.store.exceptions.ReNameRedisException;
import com.console.store.exceptions.SetRedisDataException;
import com.console.store.services.RedisService;
import com.console.store.utilities.ResponseUtility;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * api 컨트롤러
 */
@RestController
public class ServerController {
    /**
     * 서버 정보에 의해 미리 정의된 Service 들을 컨트롤하는 Proxy 객체
     */
    private final RedisService redisService;
    /**
     * 미리 정의된 Redis 서버 리스트 정보를 가지고 있는 객체
     */
    private final RedisServerInfoListLoaderByYaml redisServerInfoListLoaderByYaml;

    /**
     * constructor
     * @param redisService {@link com.console.store.factories.RedisServiceProxy RedisServiceProxy}
     * @param redisServerInfoListLoaderByYaml  {@link RedisServerInfoListLoaderByYaml redisServerInfoListLoaderByYaml}
     */
    public ServerController(@Qualifier("redisServiceProxy") RedisService redisService,
                            RedisServerInfoListLoaderByYaml redisServerInfoListLoaderByYaml) {
        this.redisService = redisService;
        this.redisServerInfoListLoaderByYaml = redisServerInfoListLoaderByYaml;
    }

    /**
     * server list
     * @return 서버 정보 list
     */
    @RequestMapping(value = "/api/server_list", method = RequestMethod.GET)
    public RedisServerRestResponse< List<String> > getServerList() {
        return ResponseUtility.response(redisServerInfoListLoaderByYaml.getServerAliasList());
    }

    /**
     * get db list
     * @param redisIdentifier Redis 서버 정보
     * @return DB별 key count 정보가 담겨있는 Map
     */
    @RequestMapping(value = "/api/{alias}/db_list", method = RequestMethod.GET)
    public RedisServerRestResponse< Map<Integer, Long> > getDbList(RedisIdentifier redisIdentifier) {
        return ResponseUtility.response(redisService.getDbListAndCount(redisIdentifier));
    }

    /**
     * get key list
     * @param redisIdentifier Redis 서버 정보
     * @return Key List
     */
    @RequestMapping(value = "/api/{alias}/{db}/key_list", method = RequestMethod.GET)
    public RedisServerRestResponse<List<String>> getKeyList(RedisIdentifier redisIdentifier) {
        return ResponseUtility.response(redisService.getAllKeys(redisIdentifier));
    }

    /**
     * get data by key
     * @param redisIdentifier Redis 서버 정보
     * @param key 검색할 key
     * @return 검색한 key 정보
     * @throws NotFoundRedisValueException 존재 하지 않는 key
     */
    @RequestMapping(value = "/api/{alias}/{db}/data/{key}", method = RequestMethod.GET)
    public RedisServerRestResponse<RedisDataFormatDto> getDataByKey(
            RedisIdentifier redisIdentifier,
            @PathVariable("key") String key) throws NotFoundRedisValueException {
        return ResponseUtility.response(redisService.getValueByKey(redisIdentifier, key));
    }

    /**
     * delete by key
     * @param redisIdentifier Redis 서버 정보
     * @param key 삭제할 key
     * @return 삭제 결과 boolean
     */
    @RequestMapping(value = "/api/{alias}/{db}/data/{key}", method = RequestMethod.DELETE)
    public RedisServerRestResponse<Boolean> deleteDataByKey(
            RedisIdentifier redisIdentifier,
            @PathVariable("key") String key) {
        return ResponseUtility.response(redisService.deleteKey(redisIdentifier, key));
    }

    /**
     * create data
     * @param redisIdentifier Redis 서버 정보
     * @param createValueInfo 생성할 redis 정보
     * @return 생성된 redis 정보
     * @throws SetRedisDataException set redis data fail
     * @throws InvalidRequestDataException redis 생성 정보 validation fail
     */
    @RequestMapping(value = "/api/{alias}/{db}/data", method = RequestMethod.POST)
    public RedisServerRestResponse<RedisDataFormatDto> createValue(
            RedisIdentifier redisIdentifier,
            @RequestBody @Valid CreateValueInfo createValueInfo) throws SetRedisDataException, InvalidRequestDataException {
        return ResponseUtility.response(redisService.createData(redisIdentifier, createValueInfo));
    }

    /**
     * modify value and ttl
     * @param redisIdentifier Redis 서버 정보
     * @param modifyValueAndTtlInfo Redis 서버 정보
     * @param key 수정할 key
     * @return 수정한 정보
     * @throws SetRedisDataException set redis data fail
     * @throws NotFoundRedisValueException 존재 하지 않는 key
     */
    @RequestMapping(value = "/api/{alias}/{db}/data/{key}", method = RequestMethod.PUT)
    public RedisServerRestResponse<RedisDataFormatDto> modifyValueAndTtl(
            RedisIdentifier redisIdentifier,
            @RequestBody ModifyValueAndTtlInfo modifyValueAndTtlInfo,
            @PathVariable(value = "key") String key
    ) throws SetRedisDataException, NotFoundRedisValueException {
        modifyValueAndTtlInfo.setKey(key);
        return ResponseUtility.response(redisService.modifyValueAndTtl(redisIdentifier, modifyValueAndTtlInfo));
    }

    /**
     * key 변경
     * @param redisIdentifier Redis 서버 정보
     * @param key target key
     * @param newKey
     * @return 변경할 key
     * @throws SetRedisDataException set redis data fail
     * @throws ReNameRedisException rename 실패
     * @throws NotFoundRedisValueException 존재 하지 않는 key
     */
    @RequestMapping(value = "/api/{alias}/{db}/data/{key}/rename", method = RequestMethod.PUT)
    public RedisServerRestResponse<RedisDataFormatDto> reName(
            RedisIdentifier redisIdentifier,
            @PathVariable("key") String key,
            @RequestParam(value = "new_key") String newKey
    ) throws SetRedisDataException, ReNameRedisException, NotFoundRedisValueException {
        return ResponseUtility.response(redisService.reName( redisIdentifier, key, newKey));
    }

}
