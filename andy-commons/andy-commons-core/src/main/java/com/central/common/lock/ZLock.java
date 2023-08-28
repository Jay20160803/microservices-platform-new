package com.central.common.lock;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 高杰
 * @create 2023/8/28 15:51
 */
@AllArgsConstructor
public class ZLock implements AutoCloseable{

    @Getter
    private final Object lock;

    private final DistributedLock locker;

    @Override
    public void close() throws Exception {
        locker.unlock(lock);
    }
}
