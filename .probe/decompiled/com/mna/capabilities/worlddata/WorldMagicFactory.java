package com.mna.capabilities.worlddata;

import com.mna.api.capabilities.IWorldMagic;
import java.util.concurrent.Callable;

public final class WorldMagicFactory implements Callable<IWorldMagic> {

    public IWorldMagic call() throws Exception {
        return new WorldMagic();
    }
}