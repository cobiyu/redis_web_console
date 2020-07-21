package com.console.store.log;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Sl4jRedisConsoleLogger implements RedisConsoleLogger {
    @Override
    public void logging(LogInfo logInfo) {
        Level level = logInfo.getLevel();
        String msg = logInfo.getObj().toString();
        switch (level) {
            case TRACE:
                log.trace(msg);
                break;
            case DEBUG:
                log.debug(msg);
                break;
            case INFO:
                log.info(msg);
                break;
            case WARN:
                log.warn(msg);
                break;
            default:
                log.error(msg);
                break;
        }
    }
}
