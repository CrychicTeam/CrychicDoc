package me.jellysquid.mods.lithium.mixin.world.temperature_cache;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ Biome.class })
public abstract class BiomeMixin {

    @Shadow
    protected abstract float getHeightAdjustedTemperature(BlockPos var1);

    @Deprecated
    @Overwrite
    public float getTemperature(BlockPos blockPos) {
        return this.getHeightAdjustedTemperature(blockPos);
    }
}