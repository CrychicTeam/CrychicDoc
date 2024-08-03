package me.jellysquid.mods.sodium.client.model.light.data;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

public abstract class LightDataAccess {

    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

    protected BlockAndTintGetter world;

    public int get(int x, int y, int z, Direction d1, Direction d2) {
        return this.get(x + d1.getStepX() + d2.getStepX(), y + d1.getStepY() + d2.getStepY(), z + d1.getStepZ() + d2.getStepZ());
    }

    public int get(int x, int y, int z, Direction dir) {
        return this.get(x + dir.getStepX(), y + dir.getStepY(), z + dir.getStepZ());
    }

    public int get(BlockPos pos, Direction dir) {
        return this.get(pos.m_123341_(), pos.m_123342_(), pos.m_123343_(), dir);
    }

    public int get(BlockPos pos) {
        return this.get(pos.m_123341_(), pos.m_123342_(), pos.m_123343_());
    }

    public abstract int get(int var1, int var2, int var3);

    protected int compute(int x, int y, int z) {
        BlockPos pos = this.pos.set(x, y, z);
        BlockAndTintGetter world = this.world;
        BlockState state = world.m_8055_(pos);
        boolean em = state.m_60788_(world, pos);
        boolean op = state.m_60831_(world, pos) && state.m_60739_(world, pos) != 0;
        boolean fo = state.m_60804_(world, pos);
        boolean fc = state.m_60838_(world, pos);
        int lu = state.getLightEmission(world, pos);
        int bl;
        int sl;
        if (fo && lu == 0) {
            bl = 0;
            sl = 0;
        } else if (em) {
            bl = world.getBrightness(LightLayer.BLOCK, pos);
            sl = world.getBrightness(LightLayer.SKY, pos);
        } else {
            int packedCoords = LevelRenderer.getLightColor(world, state, pos);
            bl = LightTexture.block(packedCoords);
            sl = LightTexture.sky(packedCoords);
        }
        float ao;
        if (lu == 0) {
            ao = state.m_60792_(world, pos);
        } else {
            ao = 1.0F;
        }
        return packFC(fc) | packFO(fo) | packOP(op) | packEM(em) | packAO(ao) | packLU(lu) | packSL(sl) | packBL(bl);
    }

    public static int packBL(int blockLight) {
        return blockLight & 15;
    }

    public static int unpackBL(int word) {
        return word & 15;
    }

    public static int packSL(int skyLight) {
        return (skyLight & 15) << 4;
    }

    public static int unpackSL(int word) {
        return word >>> 4 & 15;
    }

    public static int packLU(int luminance) {
        return (luminance & 15) << 8;
    }

    public static int unpackLU(int word) {
        return word >>> 8 & 15;
    }

    public static int packAO(float ao) {
        int aoi = (int) (ao * 4096.0F);
        return (aoi & 65535) << 12;
    }

    public static float unpackAO(int word) {
        int aoi = word >>> 12 & 65535;
        return (float) aoi * 2.4414062E-4F;
    }

    public static int packEM(boolean emissive) {
        return (emissive ? 1 : 0) << 28;
    }

    public static boolean unpackEM(int word) {
        return (word >>> 28 & 1) != 0;
    }

    public static int packOP(boolean opaque) {
        return (opaque ? 1 : 0) << 29;
    }

    public static boolean unpackOP(int word) {
        return (word >>> 29 & 1) != 0;
    }

    public static int packFO(boolean opaque) {
        return (opaque ? 1 : 0) << 30;
    }

    public static boolean unpackFO(int word) {
        return (word >>> 30 & 1) != 0;
    }

    public static int packFC(boolean fullCube) {
        return (fullCube ? 1 : 0) << 31;
    }

    public static boolean unpackFC(int word) {
        return (word >>> 31 & 1) != 0;
    }

    public static int getLightmap(int word) {
        return LightTexture.pack(Math.max(unpackBL(word), unpackLU(word)), unpackSL(word));
    }

    public static int getEmissiveLightmap(int word) {
        return unpackEM(word) ? 15728880 : getLightmap(word);
    }

    public BlockAndTintGetter getWorld() {
        return this.world;
    }
}