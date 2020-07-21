package com.console.store.configs.servers;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 서버 loader by Yaml
 */
@Data
@NoArgsConstructor
@ToString
@Component(value = "yaml")
@ConfigurationProperties("redis-server")
public class RedisServerInfoListLoaderByYaml implements RedisServerInfoListLoader {
    @Valid
    private List<RedisServerInfo> list = new ArrayList<RedisServerInfo>();

    @Override
    public List<RedisServerInfo> getServerConfigList() {
        return list;
    }

    @Override
    public List<String> getServerAliasList() {
        return list.stream().map(RedisServerInfo::getAlias).collect(Collectors.toList());
    }
}
