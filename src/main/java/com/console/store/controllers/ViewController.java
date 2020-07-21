package com.console.store.controllers;

import com.console.store.configs.RedisServerConfig;
import com.console.store.configs.servers.RedisIdentifier;
import com.console.store.exceptions.NotFoundRedisServerException;
import com.console.store.services.RedisService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * front 페이지 view 컨트롤러
 */
@Controller
public class ViewController {
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
    public ViewController(Map<RedisIdentifier, RedisService> redisServiceMap) {
        this.redisServiceMap = redisServiceMap;
    }

    /**
     * index page
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    /**
     * @param redisIdentifier Redis 서버 정보
     * @param model model
     * @return server_detail.html
     * @throws NotFoundRedisServerException 미리 정의된 서버 리스트에 없는 요청
     */
    @RequestMapping(value = "/{alias}", method = RequestMethod.GET)
    public String serverDetail(RedisIdentifier redisIdentifier, Model model) throws NotFoundRedisServerException {
        redisIdentifier.setDb(0);
        if (redisServiceMap.get(redisIdentifier) == null){
            throw new NotFoundRedisServerException(redisIdentifier.getAlias());
        }
        model.addAttribute("alias", redisIdentifier.getAlias());
        return "server_detail";
    }
}
