package dev.xkmc.l2library.base.effects.api;

import dev.xkmc.l2library.util.Proxy;
import java.util.function.Consumer;
import net.minecraft.world.entity.LivingEntity;

public interface IconOverlayEffect extends ClientRenderEffect {

    @Override
    default void render(LivingEntity entity, int lv, Consumer<DelayedEntityRender> adder) {
        if (entity != Proxy.getClientPlayer()) {
            adder.accept(this.getIcon(entity, lv));
        }
    }

    DelayedEntityRender getIcon(LivingEntity var1, int var2);
}