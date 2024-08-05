package org.violetmoon.zeta.event.play.entity.player;

import net.minecraft.world.entity.player.Player;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZPlayerTick extends IZetaPlayEvent {

    Player getPlayer();

    public interface End extends ZPlayerTick {
    }

    public interface Start extends ZPlayerTick {
    }
}