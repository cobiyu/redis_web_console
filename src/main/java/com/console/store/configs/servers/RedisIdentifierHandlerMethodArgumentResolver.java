package com.console.store.configs.servers;

import com.console.store.exceptions.NotFoundRedisServerException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * RedisIdentifier 파라미터 세팅 관련 resolver
 */
@Component
public class RedisIdentifierHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * Redis 서버 정보 수집 객체
     */
    private final Map<String, RedisServerInfoListLoader> redisServerInfoListLoaderMap;

    /**
     * constructor
     * @param redisServerInfoListLoaderMap 기본 @link({@link RedisServerInfoListLoaderByYaml RedisServerInfoListLoaderByYaml} 객체 injection
     */
    public RedisIdentifierHandlerMethodArgumentResolver(Map<String, RedisServerInfoListLoader> redisServerInfoListLoaderMap) {
        this.redisServerInfoListLoaderMap = redisServerInfoListLoaderMap;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(RedisIdentifier.class);
    }

    /**
     * RedisIdentifier 파라미터 세팅 로직
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws NotFoundRedisServerException 존재하지 않는 server
     */
    @Override
    public RedisIdentifier resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws NotFoundRedisServerException {
        HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        final Map<String, String> pathVariables = (Map<String, String>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String alias = pathVariables.get("alias");
        String db = pathVariables.get("db");
        if(alias == null){
            throw new NotFoundRedisServerException("request server alias is null");
        }

        AtomicReference<Integer> countAtomic = new AtomicReference<>(0);
        redisServerInfoListLoaderMap.forEach((s, redisServerInfoListLoader) -> {
            List<RedisServerInfo> collect = redisServerInfoListLoader.getServerConfigList().stream().filter(redisServerInfo ->
                    redisServerInfo.getAlias().equals(alias)
            ).collect(Collectors.toList());

            countAtomic.updateAndGet(v -> v + collect.size());
        });

        if(countAtomic.get() != 1){
            throw new NotFoundRedisServerException(alias+" server is not found");
        }

        if(db!=null){
            return new RedisIdentifier(alias, Integer.parseInt(db));
        } else{
            return new RedisIdentifier(alias);
        }
    }
}
