package com.console.store.controllers;

import com.console.store.configs.servers.RedisIdentifier;
import com.console.store.services.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ViewControllerTest {
    private static final String TEST_ALIAS = "test_alias";
    private MockMvc mvc;

    @InjectMocks
    private ViewController viewController;

    @Spy
    private Map<RedisIdentifier, RedisService> mockRedisRepositoryMap;

    @Test
    public void indexTest() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(viewController).build();

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
        ;
    }

    @Test
    public void serverDetailNotFoundRedisServerExceptionTest() throws Exception {
        RedisIdentifier redisIdentifier = new RedisIdentifier("invalid_alias");
        given(mockRedisRepositoryMap.get(redisIdentifier)).willReturn(null);

        mvc = MockMvcBuilders.standaloneSetup(viewController).build();
        mvc.perform(get("/"+TEST_ALIAS))
                .andExpect(status().isNotFound());
    }

    @Test
    public void serverDetailSuccessTest() throws Exception{
        RedisIdentifier redisIdentifier = new RedisIdentifier(TEST_ALIAS,0);
        given(mockRedisRepositoryMap.get(redisIdentifier)).willReturn(
                mock(RedisService.class)
        );
        mvc = MockMvcBuilders.standaloneSetup(viewController).build();


        mvc.perform(get("/"+TEST_ALIAS))
                .andExpect(status().isOk())
                .andExpect(view().name("server_detail"))
                .andExpect(model().attributeExists("alias"))
        ;
    }



}
