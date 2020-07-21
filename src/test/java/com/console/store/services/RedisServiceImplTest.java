package com.console.store.services;

import com.console.store.configs.servers.RedisIdentifier;
import com.console.store.dtos.RedisDataFormatDto;
import com.console.store.dtos.request.CreateValueInfo;
import com.console.store.dtos.request.ModifyValueAndTtlInfo;
import com.console.store.exceptions.InvalidRequestDataException;
import com.console.store.exceptions.NotFoundRedisValueException;
import com.console.store.exceptions.SetRedisDataException;
import com.console.store.exceptions.SetRedisDataExceptionType;
import com.console.store.repositories.RedisRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class RedisServiceImplTest{
    private static final Integer DB_COUNT = 16;
    @InjectMocks
    private RedisServiceImpl redisServiceImpl;

    @Spy
    private RedisRepository redisRepository;

    private RedisIdentifier redisIdentifier;
    private RedisDataFormatDto testRedisDataFormatDto;

    @Before
    public void setup(){
        redisIdentifier = new RedisIdentifier();
        testRedisDataFormatDto = new RedisDataFormatDto("testKey", null, null);
    }

    @Test
    public void getDbListAndCountTest(){
        // given
        Map<Integer, Long> testDbListAndCountMap = new HashMap<Integer, Long>();
        for (int i = 0; i < DB_COUNT; i++) {
            testDbListAndCountMap.put(i, 0L);
        }
        given(redisRepository.getDbSize()).willReturn(testDbListAndCountMap);

        // when
        Map<Integer, Long> dbListAndCount = redisServiceImpl.getDbListAndCount(redisIdentifier);

        // then
        assertEquals(testDbListAndCountMap.keySet(), dbListAndCount.keySet());
    }

    @Test
    public void getDataByKey() throws NotFoundRedisValueException {
        // given
        RedisDataFormatDto validRedisDataFormatDto = new RedisDataFormatDto("testKey", null, null);

        given(redisRepository.getValueByKey(validRedisDataFormatDto.getKey()))
                .willReturn("testValue");

        given(redisRepository.getTtlByKey(validRedisDataFormatDto.getKey()))
                .willReturn(1000L);

        // when
        RedisDataFormatDto redisDataFormatDto
                = redisServiceImpl.getValueByKey(redisIdentifier, validRedisDataFormatDto.getKey());

        // then
        assertTrue(redisDataFormatDto.isValidData());
    }

    @Test(expected = NotFoundRedisValueException.class)
    public void getDataByKeyFailTest() throws NotFoundRedisValueException {
        // given
        RedisDataFormatDto invalidRedisDataFormatDto = new RedisDataFormatDto("invalidTestKey", null, null);
        given(redisRepository.getValueByKey(invalidRedisDataFormatDto.getKey()))
                .willReturn(null);

        given(redisRepository.getTtlByKey(invalidRedisDataFormatDto.getKey()))
                .willReturn(null);

        // when
        RedisDataFormatDto notFoundRedisDataFormatDto
                = redisServiceImpl.getValueByKey(redisIdentifier, invalidRedisDataFormatDto.getKey());

        // then
        assertFalse(notFoundRedisDataFormatDto.isValidData());
    }

    @Test
    public void createDataTest() throws SetRedisDataException, InvalidRequestDataException {
        // given
        CreateValueInfo testCreateValueInfo = new CreateValueInfo("testKey", "testvalue", 12345L);

        given(redisRepository.getValueByKey(testCreateValueInfo.getKey()))
                .willReturn(null)       /// redis 중복 key없음 검증
                .willReturn(testCreateValueInfo.getValue()); ///redis set 완료 검증
        given(redisRepository.getTtlByKey(testCreateValueInfo.getKey()))
                .willReturn(testCreateValueInfo.getTtl());

        given(redisRepository.setValue(testCreateValueInfo.getKey(), testCreateValueInfo.getValue()))
                .willReturn(true);
        given(redisRepository.modifyTtl(testCreateValueInfo.getKey(), testCreateValueInfo.getTtl()))
                .willReturn(true);

        // when
        RedisDataFormatDto succcessData = redisServiceImpl.createData(redisIdentifier, testCreateValueInfo);

        // then
        assertEquals(succcessData.getKey(), testCreateValueInfo.getKey());
        assertEquals(succcessData.getTtl(), testCreateValueInfo.getTtl());

        verify(redisRepository, atLeastOnce()).setValue(any(String.class), any(String.class));
        verify(redisRepository, atLeastOnce()).modifyTtl(any(String.class), any(Long.class));
    }

    @Test
    public void deleteDataTest(){
        // given
        given(redisRepository.deleteKey(testRedisDataFormatDto.getKey()))
                .willReturn(true);

        assertTrue(redisServiceImpl.deleteKey(redisIdentifier, testRedisDataFormatDto.getKey()));

        // when
        given(redisRepository.deleteKey(testRedisDataFormatDto.getKey()))
                .willReturn(false);

        // then
        assertFalse(redisServiceImpl.deleteKey(redisIdentifier, testRedisDataFormatDto.getKey()));
    }

    @Test
    public void modifyValueAndTtlTest() throws SetRedisDataException, NotFoundRedisValueException {
        // given
        ModifyValueAndTtlInfo testModifyValueAndTtlInfo = new ModifyValueAndTtlInfo("testKey", "testvalue", 12345L);

        given(redisRepository.getValueByKey(testModifyValueAndTtlInfo.getKey()))
                .willReturn(testModifyValueAndTtlInfo.getKey());
        given(redisRepository.getTtlByKey(testModifyValueAndTtlInfo.getKey()))
                .willReturn(testModifyValueAndTtlInfo.getTtl());

        given(redisRepository.setValue(testModifyValueAndTtlInfo.getKey(), testModifyValueAndTtlInfo.getValue()))
                .willReturn(true);
        given(redisRepository.modifyTtl(testModifyValueAndTtlInfo.getKey(), testModifyValueAndTtlInfo.getTtl()))
                .willReturn(true);

        // when
        RedisDataFormatDto succcessData = redisServiceImpl.modifyValueAndTtl(redisIdentifier, testModifyValueAndTtlInfo);

        // then
        assertEquals(succcessData.getKey(), testModifyValueAndTtlInfo.getKey());
        assertEquals(succcessData.getTtl(), testModifyValueAndTtlInfo.getTtl());
        verify(redisRepository, atLeastOnce()).setValue(any(String.class), any(String.class));
        verify(redisRepository, atLeastOnce()).modifyTtl(any(String.class), any(Long.class));
    }

    @Test(expected = NotFoundRedisValueException.class)
    public void modifyValueAndTtlNotFoundTest() throws SetRedisDataException, NotFoundRedisValueException {
        // given
        ModifyValueAndTtlInfo testModifyValueAndTtlInfo = new ModifyValueAndTtlInfo("testKey", "notfoundkey", null);

        // when
        given(redisRepository.getValueByKey(testModifyValueAndTtlInfo.getKey()))
                .willReturn(null);

        // then
        redisServiceImpl.modifyValueAndTtl(redisIdentifier, testModifyValueAndTtlInfo);
    }

    @Test
    public void modifyValueAndTtlSetRedisDataExceptionTest() {
        // given
        ModifyValueAndTtlInfo testModifyValueAndTtlInfo = new ModifyValueAndTtlInfo("testKey", null, null);

        given(redisRepository.getValueByKey(testModifyValueAndTtlInfo.getKey()))
                .willReturn(testModifyValueAndTtlInfo.getKey());
        given(redisRepository.getTtlByKey(testModifyValueAndTtlInfo.getKey()))
                .willReturn(testModifyValueAndTtlInfo.getTtl());

        testModifyValueAndTtlInfo.setValue("SetRedisDataExceptionKey");
        given(redisRepository.setValue(testModifyValueAndTtlInfo.getKey(), testModifyValueAndTtlInfo.getValue()))
                .willReturn(false);


        // when
        try{
            // set value fail test execute and verify
            redisServiceImpl.modifyValueAndTtl(redisIdentifier, testModifyValueAndTtlInfo);
        } catch (NotFoundRedisValueException e) {
            fail(e.getMessage());
        } catch (SetRedisDataException e) {
            // then
            // verify exception type
            assertEquals(e.getSetRedisDataExceptionType(), SetRedisDataExceptionType.SET_VALUE);
        }

        // given
        testModifyValueAndTtlInfo.setValue(null);
        testModifyValueAndTtlInfo.setTtl(12345L);
        given(redisRepository.modifyTtl(testModifyValueAndTtlInfo.getKey(), testModifyValueAndTtlInfo.getTtl()))
                .willReturn(false);
        try{
            // when
            redisServiceImpl.modifyValueAndTtl(redisIdentifier, testModifyValueAndTtlInfo);
        } catch (NotFoundRedisValueException e) {
            fail(e.getMessage());
        } catch (SetRedisDataException e) {
            // then
            // verify exception type
            assertEquals(e.getSetRedisDataExceptionType(), SetRedisDataExceptionType.SET_TTL);
        }
    }

}
