package org.violetmoon.zeta.event.play;

import net.minecraft.server.MinecraftServer;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZServerTick extends IZetaPlayEvent {

    MinecraftServer getServer();

    public interface End extends ZServerTick {
    }

    public interface Start extends ZServerTick {
    }
}