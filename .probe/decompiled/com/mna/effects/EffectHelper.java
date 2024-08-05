package com.mna.effects;

import com.mna.ManaAndArtifice;
import com.mna.effects.interfaces.IDoubleTapEndEarly;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectHelper {

    public static void removeDoubleTapEvents(Player player) {
        for (MobEffectInstance inst : (List) player.m_21220_().stream().filter(e -> e.getEffect() instanceof IDoubleTapEndEarly && ((IDoubleTapEndEarly) e.getEffect()).canEndEarly(player, e)).collect(Collectors.toList())) {
            if (inst != null) {
                ((IDoubleTapEndEarly) inst.getEffect()).onRemoved(player, inst);
                if (!player.m_9236_().isClientSide()) {
                    try {
                        player.m_21195_(inst.getEffect());
                    } catch (Exception var5) {
                        ManaAndArtifice.LOGGER.warn("Attempted to remove potion effect " + ForgeRegistries.MOB_EFFECTS.getKey(inst.getEffect()).toString() + " early, but it failed.  Recovering and moving on.  The error was: " + var5.getMessage());
                    }
                }
            }
        }
    }
}