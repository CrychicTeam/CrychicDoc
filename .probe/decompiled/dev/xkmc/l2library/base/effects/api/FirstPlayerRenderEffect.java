package dev.xkmc.l2library.base.effects.api;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.effect.MobEffectInstance;

public interface FirstPlayerRenderEffect {

    void onClientLevelRender(AbstractClientPlayer var1, MobEffectInstance var2);
}