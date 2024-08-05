package dev.lambdaurora.lambdynlights.api;

import dev.lambdaurora.lambdynlights.LambDynLights;
import dev.lambdaurora.lambdynlights.config.DynamicLightsConfig;
import dev.lambdaurora.lambdynlights.config.QualityMode;
import java.util.function.Function;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DynamicLightHandler<T> {

    int getLuminance(T var1);

    default boolean isWaterSensitive(T lightSource) {
        return false;
    }

    @NotNull
    static <T extends LivingEntity> DynamicLightHandler<T> makeHandler(final Function<T, Integer> luminance, final Function<T, Boolean> waterSensitive) {
        return new DynamicLightHandler<T>() {

            public int getLuminance(T lightSource) {
                return (Integer) luminance.apply(lightSource);
            }

            public boolean isWaterSensitive(T lightSource) {
                return (Boolean) waterSensitive.apply(lightSource);
            }
        };
    }

    @NotNull
    static <T extends LivingEntity> DynamicLightHandler<T> makeLivingEntityHandler(@NotNull DynamicLightHandler<T> handler) {
        return entity -> {
            int luminance = 0;
            for (ItemStack equipped : entity.m_20158_()) {
                luminance = Math.max(luminance, LambDynLights.getLuminanceFromItemStack(equipped, entity.m_5842_()));
            }
            return Math.max(luminance, handler.getLuminance(entity));
        };
    }

    @NotNull
    static <T extends Creeper> DynamicLightHandler<T> makeCreeperEntityHandler(@Nullable final DynamicLightHandler<T> handler) {
        return new DynamicLightHandler<T>() {

            public int getLuminance(T entity) {
                int luminance = 0;
                if ((double) entity.getSwelling(0.0F) > 0.001) {
                    luminance = switch((QualityMode) DynamicLightsConfig.Quality.get()) {
                        case OFF ->
                            0;
                        case SLOW, FAST ->
                            10;
                        case REALTIME ->
                            (int) ((double) entity.getSwelling(0.0F) * 10.0);
                    };
                }
                if (handler != null) {
                    luminance = Math.max(luminance, handler.getLuminance(entity));
                }
                return luminance;
            }

            public boolean isWaterSensitive(T lightSource) {
                return true;
            }
        };
    }
}