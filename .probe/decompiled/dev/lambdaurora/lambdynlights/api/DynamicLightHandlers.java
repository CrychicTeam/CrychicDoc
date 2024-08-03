package dev.lambdaurora.lambdynlights.api;

import dev.lambdaurora.lambdynlights.LambDynLights;
import dev.lambdaurora.lambdynlights.accessor.DynamicLightHandlerHolder;
import dev.lambdaurora.lambdynlights.config.DynamicLightsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

public final class DynamicLightHandlers {

    private DynamicLightHandlers() {
        throw new UnsupportedOperationException("DynamicLightHandlers only contains static definitions.");
    }

    public static void registerDefaultHandlers() {
        registerDynamicLightHandler(EntityType.BLAZE, DynamicLightHandler.makeHandler(blaze -> 10, blaze -> true));
        registerDynamicLightHandler(EntityType.CREEPER, DynamicLightHandler.makeCreeperEntityHandler(null));
        registerDynamicLightHandler(EntityType.ENDERMAN, entity -> {
            int luminance = 0;
            if (entity.getCarriedBlock() != null) {
                luminance = entity.getCarriedBlock().m_60791_();
            }
            return luminance;
        });
        registerDynamicLightHandler(EntityType.ITEM, entity -> LambDynLights.getLuminanceFromItemStack(entity.getItem(), entity.m_5842_()));
        registerDynamicLightHandler(EntityType.ITEM_FRAME, entity -> {
            Level world = entity.m_20193_();
            return LambDynLights.getLuminanceFromItemStack(entity.getItem(), !world.getFluidState(entity.m_20183_()).isEmpty());
        });
        registerDynamicLightHandler(EntityType.GLOW_ITEM_FRAME, entity -> {
            Level world = entity.m_20193_();
            return Math.max(14, LambDynLights.getLuminanceFromItemStack(entity.m_31822_(), !world.getFluidState(entity.m_20183_()).isEmpty()));
        });
        registerDynamicLightHandler(EntityType.MAGMA_CUBE, entity -> (double) entity.f_33584_ > 0.6 ? 11 : 8);
        registerDynamicLightHandler(EntityType.SPECTRAL_ARROW, entity -> 8);
    }

    public static <T extends Entity> void registerDynamicLightHandler(EntityType<T> type, DynamicLightHandler<T> handler) {
        register((DynamicLightHandlerHolder<T>) type, handler);
    }

    public static <T extends BlockEntity> void registerDynamicLightHandler(BlockEntityType<T> type, DynamicLightHandler<T> handler) {
        register((DynamicLightHandlerHolder<T>) type, handler);
    }

    private static <T> void register(DynamicLightHandlerHolder<T> holder, DynamicLightHandler<T> handler) {
        DynamicLightHandler<T> registeredHandler = holder.lambdynlights$getDynamicLightHandler();
        if (registeredHandler != null) {
            DynamicLightHandler<T> newHandler = entity -> Math.max(registeredHandler.getLuminance(entity), handler.getLuminance(entity));
            holder.lambdynlights$setDynamicLightHandler(newHandler);
        } else {
            holder.lambdynlights$setDynamicLightHandler(handler);
        }
    }

    @Nullable
    public static <T extends Entity> DynamicLightHandler<T> getDynamicLightHandler(EntityType<T> type) {
        return DynamicLightHandlerHolder.cast(type).lambdynlights$getDynamicLightHandler();
    }

    @Nullable
    public static <T extends BlockEntity> DynamicLightHandler<T> getDynamicLightHandler(BlockEntityType<T> type) {
        return DynamicLightHandlerHolder.cast(type).lambdynlights$getDynamicLightHandler();
    }

    public static <T extends Entity> boolean canLightUp(T entity) {
        return DynamicLightsConfig.EntityLighting.get();
    }

    public static <T extends BlockEntity> boolean canLightUp(T entity) {
        return DynamicLightsConfig.TileEntityLighting.get();
    }

    public static <T extends Entity> int getLuminanceFrom(T entity) {
        if (!DynamicLightsConfig.EntityLighting.get()) {
            return 0;
        } else {
            DynamicLightHandler<T> handler = getDynamicLightHandler((EntityType<T>) entity.getType());
            if (handler == null) {
                return 0;
            } else if (!canLightUp(entity)) {
                return 0;
            } else {
                return handler.isWaterSensitive(entity) && !entity.getCommandSenderWorld().getFluidState(new BlockPos(entity.getBlockX(), entity.getBlockY() + (int) entity.getEyeHeight(), entity.getBlockZ())).isEmpty() ? 0 : handler.getLuminance(entity);
            }
        }
    }

    public static <T extends BlockEntity> int getLuminanceFrom(T entity) {
        if (!DynamicLightsConfig.TileEntityLighting.get()) {
            return 0;
        } else {
            DynamicLightHandler<T> handler = getDynamicLightHandler((BlockEntityType<T>) entity.getType());
            if (handler == null) {
                return 0;
            } else if (!canLightUp(entity)) {
                return 0;
            } else {
                return handler.isWaterSensitive(entity) && entity.getLevel() != null && !entity.getLevel().getFluidState(entity.getBlockPos()).isEmpty() ? 0 : handler.getLuminance(entity);
            }
        }
    }
}