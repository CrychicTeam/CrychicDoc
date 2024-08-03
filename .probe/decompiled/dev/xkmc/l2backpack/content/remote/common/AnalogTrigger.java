package dev.xkmc.l2backpack.content.remote.common;

import dev.xkmc.l2backpack.init.advancement.BackpackTriggers;
import dev.xkmc.l2library.util.Proxy;
import java.util.UUID;
import net.minecraft.world.level.Level;

public class AnalogTrigger {

    public static void trigger(Level level, UUID id) {
        if (!level.isClientSide()) {
            Proxy.getServer().map(e -> e.getPlayerList().getPlayer(id)).ifPresent(BackpackTriggers.ANALOG::trigger);
        }
    }
}