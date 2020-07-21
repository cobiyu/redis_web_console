package com.console.store.configs;


import com.console.store.configs.servers.RedisIdentifier;
import com.console.store.configs.servers.RedisIdentifierHandlerMethodArgumentResolver;
import com.console.store.configs.servers.RedisServerInfo;
import com.console.store.configs.servers.RedisServerInfoListLoader;
import com.console.store.exceptions.ExceptionResolverForLogging;
import com.console.store.exceptions.InvalidRedisServerInfoException;
import com.console.store.exceptions.NotExistRedisServerListException;
import com.console.store.repositories.LettuceRedisRepository;
import com.console.store.services.RedisService;
import com.console.store.services.RedisServiceImpl;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class RedisServerConfig implements WebMvcConfigurer {
    private static final Integer DB_COUNT = 16;

    /**
     * RedisServerInfoListLoader
     */
    private final Map<String, RedisServerInfoListLoader> redisServerInfoListLoaderMap;

    /**
     * exception handler
     */
    private final ExceptionResolverForLogging exceptionResolverForLogging;

    /**
     * RedisIdentifier 파라미터 설정 관련 resolver
     */
    private final RedisIdentifierHandlerMethodArgumentResolver redisIdentifierHandlerMethodArgumentResolver;

    /**
     * constructor
     * @param redisServerInfoListLoaderMap {@link com.console.store.configs.servers.RedisServerInfoListLoaderByYaml RedisServerInfoListLoaderByYaml} 참조
     * @param exceptionResolverForLogging exception handler
     * @param redisIdentifierHandlerMethodArgumentResolver RedisIdentifier 파라미터 설정 관련 resolver
     */
    public RedisServerConfig(Map<String, RedisServerInfoListLoader> redisServerInfoListLoaderMap,
                             ExceptionResolverForLogging exceptionResolverForLogging,
                             RedisIdentifierHandlerMethodArgumentResolver redisIdentifierHandlerMethodArgumentResolver) {
        this.redisServerInfoListLoaderMap = redisServerInfoListLoaderMap;
        this.exceptionResolverForLogging = exceptionResolverForLogging;
        this.redisIdentifierHandlerMethodArgumentResolver = redisIdentifierHandlerMethodArgumentResolver;
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(0, exceptionResolverForLogging);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(redisIdentifierHandlerMethodArgumentResolver);
    }

    /**
     * Redis 서버 정보별로 Service 객체 생성 및 Bean 등록
     * @return RedisIdentifier를 key로 가지는 service 객체로 이루어진 Map
     * @throws NotExistRedisServerListException 미리 정의돈 서버 정보 없음
     * @throws InvalidRedisServerInfoException 올바르지 않은 규격의 서버 정보
     */
    @Bean
    public Map<RedisIdentifier, RedisService> redisServiceMap()
            throws NotExistRedisServerListException, InvalidRedisServerInfoException {
        if(redisServerInfoListLoaderMap.keySet().size()<=0){
            throw new NotExistRedisServerListException();
        }

        Map<RedisIdentifier, RedisService> redisServiceMap = new HashMap<>();

        for (String redisServerType : redisServerInfoListLoaderMap.keySet()) {
            RedisServerInfoListLoader redisServerInfoListLoader = redisServerInfoListLoaderMap.get(redisServerType);
            List<RedisServerInfo> serverConfigList = redisServerInfoListLoader.getServerConfigList();


            for (RedisServerInfo redisServerInfo : serverConfigList) {
                if(!redisServerInfo.isValidInfo()){
                    throw new InvalidRedisServerInfoException(redisServerInfo);
                }
                RedisURI redisURI = new RedisURI(redisServerInfo.getHosts(), redisServerInfo.getPort(), Duration.ofSeconds(10L));
                if(redisServerInfo.getPassword() != null){
                    redisURI.setPassword(redisServerInfo.getPassword());
                }

                for (int db = 0; db < DB_COUNT; db++) {
                    RedisIdentifier identifier = new RedisIdentifier(redisServerInfo.getAlias(), db);
                    redisURI.setDatabase(db);

                    redisServiceMap.put(identifier, generateLettuceRedisServiceImpl(redisURI, identifier));
                }
            }
        }

        return redisServiceMap;
    }

    /**
     * Service 객체 생성
     * @param redisURI Redis 서버 주소
     * @param identifier Redis 서버 정보
     * @return Service 객체
     */
    private RedisService generateLettuceRedisServiceImpl(RedisURI redisURI, RedisIdentifier identifier){
        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> redisCommands = connection.sync();

        return new RedisServiceImpl( new LettuceRedisRepository(redisCommands, identifier));
    }

}
