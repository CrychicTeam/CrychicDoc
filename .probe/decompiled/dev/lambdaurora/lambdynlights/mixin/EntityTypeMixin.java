package dev.lambdaurora.lambdynlights.mixin;

import dev.lambdaurora.lambdynlights.accessor.DynamicLightHandlerHolder;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandler;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ EntityType.class })
public abstract class EntityTypeMixin<T extends Entity> implements DynamicLightHandlerHolder<T> {

    @Shadow
    @Nullable
    private Component description;

    @Unique
    private DynamicLightHandler<T> lambdynlights$lightHandler;

    @org.jetbrains.annotations.Nullable
    @Override
    public DynamicLightHandler<T> lambdynlights$getDynamicLightHandler() {
        return this.lambdynlights$lightHandler;
    }

    @Override
    public void lambdynlights$setDynamicLightHandler(DynamicLightHandler<T> handler) {
        this.lambdynlights$lightHandler = handler;
    }
}