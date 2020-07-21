package com.console.store.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.slf4j.event.Level;

/**
 * 공통으로 사용할 로그 포맷
 */
@AllArgsConstructor
public class LogInfo {
    @NonNull
    @Getter
    private Level level;
    @NonNull
    @Getter
    private Object obj;
}
