package me.jellysquid.mods.sodium.client.world.biome;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import me.jellysquid.mods.sodium.client.util.color.BoxBlur;
import me.jellysquid.mods.sodium.client.world.cloned.ChunkRenderContext;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;

public class BiomeColorCache {

    private static final int NEIGHBOR_BLOCK_RADIUS = 2;

    private final BiomeSlice biomeData;

    private final Reference2ReferenceOpenHashMap<ColorResolver, BiomeColorCache.Slice[]> slices;

    private long populateStamp;

    private final int blendRadius;

    private final BoxBlur.ColorBuffer tempColorBuffer;

    private int minX;

    private int minY;

    private int minZ;

    private int maxX;

    private int maxY;

    private int maxZ;

    private final int sizeXZ;

    private final int sizeY;

    public BiomeColorCache(BiomeSlice biomeData, int blendRadius) {
        this.biomeData = biomeData;
        this.blendRadius = Math.min(7, blendRadius);
        this.sizeXZ = 16 + (2 + this.blendRadius) * 2;
        this.sizeY = 20;
        this.slices = new Reference2ReferenceOpenHashMap();
        this.populateStamp = 1L;
        this.tempColorBuffer = new BoxBlur.ColorBuffer(this.sizeXZ, this.sizeXZ);
    }

    public void update(ChunkRenderContext context) {
        this.minX = context.getOrigin().minBlockX() - 2 - this.blendRadius;
        this.minY = context.getOrigin().minBlockY() - 2;
        this.minZ = context.getOrigin().minBlockZ() - 2 - this.blendRadius;
        this.maxX = context.getOrigin().maxBlockX() + 2 + this.blendRadius;
        this.maxY = context.getOrigin().maxBlockY() + 2;
        this.maxZ = context.getOrigin().maxBlockZ() + 2 + this.blendRadius;
        this.populateStamp++;
    }

    public int getColor(BiomeColorSource source, int blockX, int blockY, int blockZ) {
        return switch(source) {
            case GRASS ->
                this.getColor(BiomeColors.GRASS_COLOR_RESOLVER, blockX, blockY, blockZ);
            case FOLIAGE ->
                this.getColor(BiomeColors.FOLIAGE_COLOR_RESOLVER, blockX, blockY, blockZ);
            case WATER ->
                this.getColor(BiomeColors.WATER_COLOR_RESOLVER, blockX, blockY, blockZ);
        };
    }

    public int getColor(ColorResolver resolver, int blockX, int blockY, int blockZ) {
        int relX = Mth.clamp(blockX, this.minX, this.maxX) - this.minX;
        int relY = Mth.clamp(blockY, this.minY, this.maxY) - this.minY;
        int relZ = Mth.clamp(blockZ, this.minZ, this.maxZ) - this.minZ;
        if (!this.slices.containsKey(resolver)) {
            this.initializeSlices(resolver);
        }
        BiomeColorCache.Slice slice = ((BiomeColorCache.Slice[]) this.slices.get(resolver))[relY];
        if (slice.lastPopulateStamp < this.populateStamp) {
            this.updateColorBuffers(relY, resolver, slice);
        }
        BoxBlur.ColorBuffer buffer = slice.getBuffer();
        return buffer.get(relX, relZ);
    }

    private void initializeSlices(ColorResolver resolver) {
        BiomeColorCache.Slice[] slice = new BiomeColorCache.Slice[this.sizeY];
        this.slices.put(resolver, slice);
        for (int y = 0; y < this.sizeY; y++) {
            slice[y] = new BiomeColorCache.Slice(this.sizeXZ);
        }
    }

    private void updateColorBuffers(int relY, ColorResolver resolver, BiomeColorCache.Slice slice) {
        int worldY = this.minY + relY;
        for (int worldZ = this.minZ; worldZ <= this.maxZ; worldZ++) {
            for (int worldX = this.minX; worldX <= this.maxX; worldX++) {
                Biome biome = this.biomeData.getBiome(worldX, worldY, worldZ).value();
                int relativeX = worldX - this.minX;
                int relativeZ = worldZ - this.minZ;
                slice.buffer.set(relativeX, relativeZ, resolver.getColor(biome, (double) worldX, (double) worldZ));
            }
        }
        if (this.blendRadius > 0) {
            BoxBlur.blur(slice.buffer, this.tempColorBuffer, this.blendRadius);
        }
        slice.lastPopulateStamp = this.populateStamp;
    }

    private static class Slice {

        private final BoxBlur.ColorBuffer buffer;

        private long lastPopulateStamp;

        private Slice(int size) {
            this.buffer = new BoxBlur.ColorBuffer(size, size);
        }

        public BoxBlur.ColorBuffer getBuffer() {
            return this.buffer;
        }
    }
}