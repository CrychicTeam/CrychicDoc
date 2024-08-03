package fuzs.puzzleslib.api.event.v1.entity.player;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;

public final class PlayerXpEvents {

    public static final EventInvoker<PlayerXpEvents.PickupXp> PICKUP_XP = EventInvoker.lookup(PlayerXpEvents.PickupXp.class);

    private PlayerXpEvents() {
    }

    @FunctionalInterface
    public interface PickupXp {

        EventResult onPickupXp(Player var1, ExperienceOrb var2);
    }
}