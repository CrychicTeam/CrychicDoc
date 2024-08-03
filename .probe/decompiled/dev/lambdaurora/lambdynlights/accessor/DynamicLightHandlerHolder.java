package dev.lambdaurora.lambdynlights.accessor;

import dev.lambdaurora.lambdynlights.api.DynamicLightHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@Internal
@NonExtendable
public interface DynamicLightHandlerHolder<T> {

    @Nullable
    DynamicLightHandler<T> lambdynlights$getDynamicLightHandler();

    void lambdynlights$setDynamicLightHandler(DynamicLightHandler<T> var1);

    static <T extends Entity> DynamicLightHandlerHolder<T> cast(EntityType<T> entityType) {
        return (DynamicLightHandlerHolder<T>) entityType;
    }

    static <T extends BlockEntity> DynamicLightHandlerHolder<T> cast(BlockEntityType<T> entityType) {
        return (DynamicLightHandlerHolder<T>) entityType;
    }
}