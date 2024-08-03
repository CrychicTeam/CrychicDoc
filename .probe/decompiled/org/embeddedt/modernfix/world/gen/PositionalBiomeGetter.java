package org.embeddedt.modernfix.world.gen;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public class PositionalBiomeGetter implements Supplier<Holder<Biome>> {

    private final Function<BlockPos, Holder<Biome>> biomeGetter;

    private final BlockPos.MutableBlockPos pos;

    private int nextX;

    private int nextY;

    private int nextZ;

    private volatile Holder<Biome> curBiome;

    public PositionalBiomeGetter(Function<BlockPos, Holder<Biome>> biomeGetter, BlockPos.MutableBlockPos pos) {
        this.biomeGetter = biomeGetter;
        this.pos = pos;
    }

    public void update(int nextX, int nextY, int nextZ) {
        this.nextX = nextX;
        this.nextY = nextY;
        this.nextZ = nextZ;
        this.curBiome = null;
    }

    public Holder<Biome> get() {
        Holder<Biome> biome = this.curBiome;
        if (biome == null) {
            this.curBiome = biome = (Holder<Biome>) this.biomeGetter.apply(this.pos.set(this.nextX, this.nextY, this.nextZ));
        }
        return biome;
    }
}