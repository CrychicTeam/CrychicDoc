package dev.xkmc.l2artifacts.content.mobeffects;

import dev.xkmc.l2library.base.effects.api.DelayedEntityRender;
import dev.xkmc.l2library.base.effects.api.IconOverlayEffect;
import dev.xkmc.l2library.util.Proxy;
import java.util.function.Consumer;
import net.minecraft.world.entity.LivingEntity;

public interface IOverlayRenderEffect extends IconOverlayEffect {

    @Override
    default void render(LivingEntity entity, int lv, Consumer<DelayedEntityRender> adder) {
        if (entity != Proxy.getClientPlayer()) {
            IconOverlayEffect.super.render(entity, lv, adder);
        }
    }
}