package org.embeddedt.modernfix.common.mixin.perf.remove_biome_temperature_cache;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ Biome.class })
public abstract class BiomeMixin {

    @Shadow
    protected abstract float getHeightAdjustedTemperature(BlockPos var1);

    @Overwrite
    private float getTemperature(BlockPos pos) {
        return this.getHeightAdjustedTemperature(pos);
    }
}