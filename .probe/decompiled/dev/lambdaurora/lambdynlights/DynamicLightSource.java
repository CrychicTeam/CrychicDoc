package dev.lambdaurora.lambdynlights;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface DynamicLightSource {

    double tdv$getDynamicLightX();

    double tdv$getDynamicLightY();

    double tdv$getDynamicLightZ();

    Level tdv$getDynamicLightWorld();

    default boolean tdv$isDynamicLightEnabled() {
        return LambDynLights.isEnabled() && LambDynLights.get().containsLightSource(this);
    }

    @Internal
    default void tdv$setDynamicLightEnabled(boolean enabled) {
        this.tdv$resetDynamicLight();
        if (enabled) {
            LambDynLights.get().addLightSource(this);
        } else {
            LambDynLights.get().removeLightSource(this);
        }
    }

    void tdv$resetDynamicLight();

    int tdv$getLuminance();

    void tdv$dynamicLightTick();

    boolean tdv$shouldUpdateDynamicLight();

    boolean tdv$lambdynlights$updateDynamicLight(@NotNull LevelRenderer var1);

    void tdv$lambdynlights$scheduleTrackedChunksRebuild(@NotNull LevelRenderer var1);
}