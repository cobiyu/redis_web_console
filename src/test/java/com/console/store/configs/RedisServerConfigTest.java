package com.console.store.configs;

import com.console.store.configs.servers.RedisIdentifierHandlerMethodArgumentResolver;
import com.console.store.configs.servers.RedisServerInfo;
import com.console.store.configs.servers.RedisServerInfoListLoader;
import com.console.store.exceptions.ExceptionResolverForLogging;
import com.console.store.exceptions.InvalidRedisServerInfoException;
import com.console.store.exceptions.NotExistRedisServerListException;
import com.console.store.log.RedisConsoleLogger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RedisServerConfigTest {

    @InjectMocks
    private RedisServerConfig mockRedisServerConfig;
    @Spy
    private Map<String, RedisServerInfoListLoader> mockRedisServerInfoListLoaderMap;

    @Spy
    List<RedisConsoleLogger> redisConsoleLoggerList;
    private ExceptionResolverForLogging exceptionResolverForLogging;

    @Spy
    private Map<String, RedisServerInfoListLoader> redisServerInfoListLoaderMap;
    private RedisIdentifierHandlerMethodArgumentResolver redisIdentifierHandlerMethodArgumentResolver;

    @Autowired
    private Map<String, RedisServerInfoListLoader> realRedisServerInfoListLoaderMap;


    @Test
    public void redisRepositoryMapTest(){
        /// NotExistRedisServerException Test
        this.redisRepositoryMapNotExistRedisServerExceptionTest();

        /// InvalidRedisServerInfoException Test
        this.redisRepositoryMapInvalidRedisServerInfoExceptionTest();

        /// success
        this.redisRepositoryMapSuccessTest();
    }

    private void redisRepositoryMapNotExistRedisServerExceptionTest(){
        try {
            given(mockRedisServerInfoListLoaderMap.keySet()).willReturn(
                    new HashSet<String>()
            );

            mockRedisServerConfig.redisServiceMap();

        } catch (NotExistRedisServerListException e) {
            /// it's pass
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void redisRepositoryMapInvalidRedisServerInfoExceptionTest(){
        try {
            /// Set invalid redisServerInfo
            RedisServerInfoListLoader testRedisServerInfoListLoader = mock(RedisServerInfoListLoader.class);
            RedisServerInfo invalidRedisServerInfo = new RedisServerInfo();
            invalidRedisServerInfo.setAlias("");
            invalidRedisServerInfo.setHosts("");
            invalidRedisServerInfo.setPort(0);

            /// Add invalid redisServerInfo to list
            List<RedisServerInfo> invalidServerConfigList = new ArrayList<>();
            invalidServerConfigList.add(invalidRedisServerInfo);

            /// Set return invalid redisServerInfo
            given(testRedisServerInfoListLoader.getServerConfigList())
                    .willReturn(invalidServerConfigList);

            Map<String, RedisServerInfoListLoader> mockRedisServerInfoListLoaderMap
                    = new HashMap<>();
            mockRedisServerInfoListLoaderMap.put("test", testRedisServerInfoListLoader);

            /// Inject mockRedisServerInfoListLoaderMap to RedisServerConfig Test instance
            exceptionResolverForLogging = new ExceptionResolverForLogging(redisConsoleLoggerList);
            redisIdentifierHandlerMethodArgumentResolver = new RedisIdentifierHandlerMethodArgumentResolver(redisServerInfoListLoaderMap);
            RedisServerConfig generatedMockRedisServerConfig = new RedisServerConfig(
                    mockRedisServerInfoListLoaderMap,
                    exceptionResolverForLogging,
                    redisIdentifierHandlerMethodArgumentResolver);

            /// Test
            generatedMockRedisServerConfig.redisServiceMap();

        } catch (NotExistRedisServerListException e) {
            fail(e.getMessage());
        } catch (InvalidRedisServerInfoException e) {
            /// it's pass
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    private void redisRepositoryMapSuccessTest(){
        try {
            /// Inject RedisServerInfoListLoaderMap from application-test.yml to RedisServerConfig Test instance
            exceptionResolverForLogging = new ExceptionResolverForLogging(redisConsoleLoggerList);
            redisIdentifierHandlerMethodArgumentResolver = new RedisIdentifierHandlerMethodArgumentResolver(redisServerInfoListLoaderMap);
            RedisServerConfig generatedMockRedisServerConfig =
                    new RedisServerConfig(realRedisServerInfoListLoaderMap,
                            exceptionResolverForLogging,
                            redisIdentifierHandlerMethodArgumentResolver);

            /// Test
            generatedMockRedisServerConfig.redisServiceMap();

        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}
