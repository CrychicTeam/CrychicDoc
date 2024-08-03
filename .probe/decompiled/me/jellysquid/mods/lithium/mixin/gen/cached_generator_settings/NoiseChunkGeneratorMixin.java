package me.jellysquid.mods.lithium.mixin.gen.cached_generator_settings;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ NoiseBasedChunkGenerator.class })
public class NoiseChunkGeneratorMixin {

    @Shadow
    @Final
    private Holder<NoiseGeneratorSettings> settings;

    private int cachedSeaLevel;

    @Overwrite
    public int getSeaLevel() {
        return this.cachedSeaLevel;
    }

    @Inject(method = { "<init>" }, at = { @At(value = "INVOKE", target = "Lcom/google/common/base/Suppliers;memoize(Lcom/google/common/base/Supplier;)Lcom/google/common/base/Supplier;", remap = false, shift = Shift.BEFORE) })
    private void hookConstructor(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings, CallbackInfo ci) {
        this.cachedSeaLevel = this.settings.value().seaLevel();
    }
}