package com.github.alexmodguy.alexscaves.mixin.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ BiomeAmbientSoundsHandler.class })
public abstract class BiomeAmbientSoundsHandlerMixin {

    @Shadow
    @Final
    private BiomeManager biomeManager;

    @Shadow
    @Final
    private LocalPlayer player;

    @Shadow
    @Final
    private Object2ObjectArrayMap<Biome, BiomeAmbientSoundsHandler.LoopSoundInstance> loopSounds;

    @Inject(method = { "Lnet/minecraft/client/resources/sounds/BiomeAmbientSoundsHandler;tick()V" }, at = { @At("TAIL") })
    private void ac_tick(CallbackInfo ci) {
        Biome biome = this.biomeManager.getNoiseBiomeAtPosition(this.player.m_20185_(), this.player.m_20186_(), this.player.m_20189_()).value();
        if (biome.getAmbientLoop().isPresent() && !this.player.m_6084_()) {
            this.loopSounds.values().forEach(BiomeAmbientSoundsHandler.LoopSoundInstance::m_119659_);
        }
    }
}