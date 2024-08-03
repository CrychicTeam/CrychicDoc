package com.github.alexmodguy.alexscaves.mixin.client;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import java.util.function.Supplier;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ClientLevel.class })
public abstract class ClientLevelMixin extends Level {

    protected ClientLevelMixin(WritableLevelData writableLevelData0, ResourceKey<Level> resourceKeyLevel1, RegistryAccess registryAccess2, Holder<DimensionType> holderDimensionType3, Supplier<ProfilerFiller> supplierProfilerFiller4, boolean boolean5, boolean boolean6, long long7, int int8) {
        super(writableLevelData0, resourceKeyLevel1, registryAccess2, holderDimensionType3, supplierProfilerFiller4, boolean5, boolean6, long7, int8);
    }

    @Inject(method = { "Lnet/minecraft/client/multiplayer/ClientLevel;getSkyColor(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;" }, at = { @At("RETURN") }, cancellable = true)
    private void ac_getSkyColor_timeOfDay(Vec3 position, float partialTick, CallbackInfoReturnable<Vec3> cir) {
        if (AlexsCaves.CLIENT_CONFIG.biomeSkyOverrides.get() && ClientProxy.acSkyOverrideAmount > 0.0F) {
            Vec3 prevVec3 = (Vec3) cir.getReturnValue();
            Vec3 sampledVec3 = ClientProxy.acSkyOverrideColor;
            sampledVec3 = ClientProxy.processSkyColor(sampledVec3, partialTick);
            cir.setReturnValue(prevVec3.add(sampledVec3.subtract(prevVec3).scale((double) ClientProxy.acSkyOverrideAmount)));
        }
    }

    @Inject(method = { "Lnet/minecraft/client/multiplayer/ClientLevel;getSkyDarken(F)F" }, at = { @At("RETURN") }, cancellable = true)
    private void ac_getSkyDarken_timeOfDay(float partialTick, CallbackInfoReturnable<Float> cir) {
        if (AlexsCaves.CLIENT_CONFIG.biomeSkyOverrides.get()) {
            float skyDarken = (Float) cir.getReturnValue();
            if (ClientProxy.acSkyOverrideAmount > 0.0F) {
                cir.setReturnValue(Math.max(skyDarken, ClientProxy.acSkyOverrideAmount));
            }
        }
    }
}