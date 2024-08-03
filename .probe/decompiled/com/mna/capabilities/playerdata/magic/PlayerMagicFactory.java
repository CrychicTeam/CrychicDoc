package com.mna.capabilities.playerdata.magic;

import com.mna.api.capabilities.IPlayerMagic;
import java.util.concurrent.Callable;

public final class PlayerMagicFactory implements Callable<IPlayerMagic> {

    public IPlayerMagic call() throws Exception {
        return new PlayerMagic();
    }
}