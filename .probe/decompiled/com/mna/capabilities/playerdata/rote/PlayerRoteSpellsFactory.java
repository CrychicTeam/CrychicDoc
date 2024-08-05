package com.mna.capabilities.playerdata.rote;

import com.mna.api.capabilities.IPlayerRoteSpells;
import java.util.concurrent.Callable;

public class PlayerRoteSpellsFactory implements Callable<IPlayerRoteSpells> {

    public IPlayerRoteSpells call() throws Exception {
        return new PlayerRoteSpells();
    }
}