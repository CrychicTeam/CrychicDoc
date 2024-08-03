package com.mna.effects.interfaces;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public interface IDoubleTapEndEarly {

    default boolean canEndEarly(Player player, MobEffectInstance effect) {
        return true;
    }

    default void onRemoved(Player player, MobEffectInstance effect) {
    }
}