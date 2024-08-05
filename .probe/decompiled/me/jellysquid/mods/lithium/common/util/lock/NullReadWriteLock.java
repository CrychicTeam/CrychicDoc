package me.jellysquid.mods.lithium.common.util.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class NullReadWriteLock implements ReadWriteLock {

    private final NullLock lock = new NullLock();

    public Lock readLock() {
        return this.lock;
    }

    public Lock writeLock() {
        return this.lock;
    }
}