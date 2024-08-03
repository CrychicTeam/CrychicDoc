package me.jellysquid.mods.lithium.common.util.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class NullLock implements Lock {

    public void lock() {
    }

    public void lockInterruptibly() {
    }

    public boolean tryLock() {
        return true;
    }

    public boolean tryLock(long time, TimeUnit unit) {
        return true;
    }

    public void unlock() {
    }

    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }
}