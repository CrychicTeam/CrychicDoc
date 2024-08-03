package org.violetmoon.zeta.event.play;

import net.minecraft.world.level.Level;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZLevelTick extends IZetaPlayEvent {

    Level getLevel();

    public interface End extends ZLevelTick {
    }

    public interface Start extends ZLevelTick {
    }
}