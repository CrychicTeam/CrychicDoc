package me.jellysquid.mods.sodium.client.model.quad.blender;

import java.util.Arrays;
import me.jellysquid.mods.sodium.client.model.color.ColorProvider;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.caffeinemc.mods.sodium.api.util.ColorMixer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

public abstract class BlendedColorProvider<T> implements ColorProvider<T> {

    private static boolean shouldUseVertexBlending;

    private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

    public static void checkBlendingEnabled() {
        shouldUseVertexBlending = Minecraft.getInstance().options.biomeBlendRadius().get() > 0;
    }

    @Override
    public void getColors(WorldSlice view, BlockPos pos, T state, ModelQuadView quad, int[] output) {
        if (shouldUseVertexBlending) {
            for (int vertexIndex = 0; vertexIndex < 4; vertexIndex++) {
                output[vertexIndex] = this.getVertexColor(view, pos, quad, vertexIndex);
            }
        } else {
            Arrays.fill(output, ColorARGB.toABGR(this.getColor(view, pos)));
        }
    }

    private int getVertexColor(WorldSlice world, BlockPos blockPos, ModelQuadView quad, int vertexIndex) {
        float posX = quad.getX(vertexIndex) - 0.5F;
        float posY = quad.getY(vertexIndex) - 0.5F;
        float posZ = quad.getZ(vertexIndex) - 0.5F;
        int intX = Mth.floor(posX);
        int intY = Mth.floor(posY);
        int intZ = Mth.floor(posZ);
        int worldIntX = blockPos.m_123341_() + intX;
        int worldIntY = blockPos.m_123342_() + intY;
        int worldIntZ = blockPos.m_123343_() + intZ;
        BlockPos.MutableBlockPos neighborPos = this.cursor;
        int c00 = this.getColor(world, neighborPos.set(worldIntX + 0, worldIntY, worldIntZ + 0));
        int c01 = this.getColor(world, neighborPos.set(worldIntX + 0, worldIntY, worldIntZ + 1));
        int c10 = this.getColor(world, neighborPos.set(worldIntX + 1, worldIntY, worldIntZ + 0));
        int c11 = this.getColor(world, neighborPos.set(worldIntX + 1, worldIntY, worldIntZ + 1));
        int z0;
        if (c00 != c01) {
            z0 = ColorMixer.mix(c00, c01, posZ - (float) intZ);
        } else {
            z0 = c00;
        }
        int z1;
        if (c10 != c11) {
            z1 = ColorMixer.mix(c10, c11, posZ - (float) intZ);
        } else {
            z1 = c10;
        }
        int x0;
        if (z0 != z1) {
            x0 = ColorMixer.mix(z0, z1, posX - (float) intX);
        } else {
            x0 = z0;
        }
        return ColorARGB.toABGR(x0);
    }

    protected int getColor(WorldSlice world, BlockPos pos) {
        return this.getColor(world, pos.m_123341_(), pos.m_123342_(), pos.m_123343_());
    }

    @Deprecated(forRemoval = true)
    protected int getColor(WorldSlice world, int x, int y, int z) {
        throw new AssertionError("Must override one of the getColor methods");
    }
}