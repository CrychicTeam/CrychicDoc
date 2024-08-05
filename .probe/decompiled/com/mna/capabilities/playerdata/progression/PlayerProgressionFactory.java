package com.mna.capabilities.playerdata.progression;

import com.mna.api.capabilities.IPlayerProgression;
import java.util.concurrent.Callable;

public class PlayerProgressionFactory implements Callable<IPlayerProgression> {

    public IPlayerProgression call() throws Exception {
        return new PlayerProgression();
    }
}