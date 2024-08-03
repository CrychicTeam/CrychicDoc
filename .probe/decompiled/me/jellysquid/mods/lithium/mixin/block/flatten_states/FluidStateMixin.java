package me.jellysquid.mods.lithium.mixin.block.flatten_states;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ FluidState.class })
public abstract class FluidStateMixin {

    private boolean isEmptyCache;

    @Shadow
    public abstract Fluid getType();

    @Inject(method = { "<init>(Lnet/minecraft/fluid/Fluid;Lcom/google/common/collect/ImmutableMap;Lcom/mojang/serialization/MapCodec;)V" }, at = { @At("RETURN") })
    private void initFluidCache(Fluid fluid, ImmutableMap<Property<?>, Comparable<?>> propertyMap, MapCodec<FluidState> codec, CallbackInfo ci) {
        this.isEmptyCache = this.getType().isEmpty();
    }

    @Overwrite
    public boolean isEmpty() {
        return this.isEmptyCache;
    }
}