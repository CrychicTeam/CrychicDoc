package me.jellysquid.mods.sodium.client.model.light.data;

import java.util.Arrays;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockAndTintGetter;

public class ArrayLightDataCache extends LightDataAccess {

    private static final int NEIGHBOR_BLOCK_RADIUS = 2;

    private static final int BLOCK_LENGTH = 20;

    private final int[] light;

    private int xOffset;

    private int yOffset;

    private int zOffset;

    public ArrayLightDataCache(BlockAndTintGetter world) {
        this.world = world;
        this.light = new int[8000];
    }

    public void reset(SectionPos origin) {
        this.xOffset = origin.minBlockX() - 2;
        this.yOffset = origin.minBlockY() - 2;
        this.zOffset = origin.minBlockZ() - 2;
        Arrays.fill(this.light, 0);
    }

    private int index(int x, int y, int z) {
        int x2 = x - this.xOffset;
        int y2 = y - this.yOffset;
        int z2 = z - this.zOffset;
        return z2 * 20 * 20 + y2 * 20 + x2;
    }

    @Override
    public int get(int x, int y, int z) {
        int l = this.index(x, y, z);
        int word = this.light[l];
        return word != 0 ? word : (this.light[l] = this.compute(x, y, z));
    }
}