package com.console.store.repositories;

import com.console.store.configs.servers.RedisIdentifier;
import com.console.store.configs.servers.RedisServerInfo;
import com.console.store.configs.servers.RedisServerInfoListLoader;
import com.console.store.dtos.RedisDataFormatDto;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LettuceRedisRepositoryTest {
    @Autowired
    private Map<String, RedisServerInfoListLoader> redisServerInfoListLoaderMap;
    private RedisRepository redisRepository;
    private RedisDataFormatDto testRedisDataFormatDto;
    private RedisCommands<String, String> redisCommands;

    @Before
    public void set(){
        Map.Entry<String, RedisServerInfoListLoader> redisServerInfoListLoaderEntry
                = redisServerInfoListLoaderMap.entrySet().iterator().next();
        RedisServerInfoListLoader redisServerInfoListLoader = redisServerInfoListLoaderEntry.getValue();    /// extract first
        RedisServerInfo redisServerInfo = redisServerInfoListLoader.getServerConfigList().iterator().next();

        RedisURI redisURI = new RedisURI(redisServerInfo.getHosts(), redisServerInfo.getPort(), Duration.ofSeconds(10L));
        redisURI.setPassword(redisServerInfo.getPassword());
        redisURI.setDatabase(0);

        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        redisCommands = connection.sync();

        RedisIdentifier redisIdentifier = new RedisIdentifier(redisServerInfo.getAlias(), 0);

        this.redisRepository = new LettuceRedisRepository(redisCommands, redisIdentifier);
        this.testRedisDataFormatDto = new RedisDataFormatDto("redisconsoletest", "redisconsolevalue", 5000L);

        /// set test data
        redisCommands.del(testRedisDataFormatDto.getKey());
        redisCommands.set(
                this.testRedisDataFormatDto.getKey(),
                this.testRedisDataFormatDto.getValue(),
                SetArgs.Builder.ex(this.testRedisDataFormatDto.getTtl())
        );
    }

    @After
    public void reset(){
        redisCommands.del(testRedisDataFormatDto.getKey());
    }

    @Test
    public void getDbSize() {
        assertThat(this.redisRepository.getDbSize().size(), is(16));
    }

    @Test
    public void getAllKeys() {
        assertThat(this.redisRepository.getAllKeys().size(), greaterThan(0));
    }

    @Test
    public void getTtlByKey() {
        assertThat(this.redisRepository.getTtlByKey(this.testRedisDataFormatDto.getKey()),
                greaterThan(this.testRedisDataFormatDto.getTtl() - 1000L)
        );
    }

    @Test
    public void getValueByKey() {
        assertThat(this.redisRepository.getValueByKey(this.testRedisDataFormatDto.getKey()),
                is(this.testRedisDataFormatDto.getValue())
        );
    }

    @Test
    public void modifyValue() {
        this.testRedisDataFormatDto.setValue("redisconsolevalue_modify");
        assertThat(
                this.redisRepository.setValue(
                        this.testRedisDataFormatDto.getKey(),
                        this.testRedisDataFormatDto.getValue()
                ),
                is(true)
        );
    }

    @Test
    public void modifyTtl() {
        assertThat(
                this.redisRepository.modifyTtl(
                    this.testRedisDataFormatDto.getKey(),
                    this.testRedisDataFormatDto.getTtl()
                ),
                is(true)
        );
    }

    @Test
    public void deleteData() {
        assertThat(this.redisRepository.deleteKey(this.testRedisDataFormatDto.getKey()),
                is(true)
        );
    }
}
