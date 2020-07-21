package com.console.store.configs.servers;

import java.util.List;

/**
 * Redis 서버 정보 수집 interface
 */
public interface RedisServerInfoListLoader {
    /**
     * server config list
     * @return RedisServerInfo List
     */
    public List<RedisServerInfo> getServerConfigList();

    /**
     * server alias slist
     * @return server alias list
     */
    public List<String> getServerAliasList();
}
