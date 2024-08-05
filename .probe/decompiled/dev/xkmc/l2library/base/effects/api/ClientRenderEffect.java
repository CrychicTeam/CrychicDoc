package dev.xkmc.l2library.base.effects.api;

import java.util.function.Consumer;
import net.minecraft.world.entity.LivingEntity;

public interface ClientRenderEffect {

    void render(LivingEntity var1, int var2, Consumer<DelayedEntityRender> var3);
}