package org.violetmoon.zeta.event.bus.helpers;

import net.minecraft.world.entity.player.Player;

public interface PlayerGetter extends LivingGetter {

    default Player getPlayer() {
        return this.getEntity() instanceof Player p ? p : null;
    }
}