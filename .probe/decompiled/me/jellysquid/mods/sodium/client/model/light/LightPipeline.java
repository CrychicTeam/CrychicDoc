package me.jellysquid.mods.sodium.client.model.light;

import me.jellysquid.mods.sodium.client.model.light.data.QuadLightData;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public interface LightPipeline {

    void calculate(ModelQuadView var1, BlockPos var2, QuadLightData var3, Direction var4, Direction var5, boolean var6);

    default void reset() {
    }
}