package com.console.store.log;

/**
 * 서버에서 사용할 logging interface
 * interface를 구현한 모든 방법으로 logging
 */
public interface RedisConsoleLogger {
    public void logging(LogInfo logInfo);
}
