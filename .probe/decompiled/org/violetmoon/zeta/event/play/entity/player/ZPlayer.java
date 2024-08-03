package org.violetmoon.zeta.event.play.entity.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;
import org.violetmoon.zeta.event.bus.helpers.PlayerGetter;

public interface ZPlayer extends IZetaPlayEvent, PlayerGetter {

    public interface BreakSpeed extends ZPlayer {

        BlockState getState();

        float getOriginalSpeed();

        void setNewSpeed(float var1);
    }

    public interface Clone extends ZPlayer {

        Player getOriginal();
    }

    public interface LoggedIn extends ZPlayer {
    }

    public interface LoggedOut extends ZPlayer {
    }
}