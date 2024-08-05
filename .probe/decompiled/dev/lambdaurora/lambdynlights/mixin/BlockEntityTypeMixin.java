package dev.lambdaurora.lambdynlights.mixin;

import dev.lambdaurora.lambdynlights.accessor.DynamicLightHandlerHolder;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandler;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ BlockEntityType.class })
public class BlockEntityTypeMixin<T extends BlockEntity> implements DynamicLightHandlerHolder<T> {

    @Unique
    private DynamicLightHandler<T> lambdynlights$lightHandler;

    @Nullable
    @Override
    public DynamicLightHandler<T> lambdynlights$getDynamicLightHandler() {
        return this.lambdynlights$lightHandler;
    }

    @Override
    public void lambdynlights$setDynamicLightHandler(DynamicLightHandler<T> handler) {
        this.lambdynlights$lightHandler = handler;
    }
}