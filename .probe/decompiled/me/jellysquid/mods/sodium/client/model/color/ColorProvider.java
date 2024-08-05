package me.jellysquid.mods.sodium.client.model.color;

import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.core.BlockPos;

public interface ColorProvider<T> {

    void getColors(WorldSlice var1, BlockPos var2, T var3, ModelQuadView var4, int[] var5);
}