package de.keksuccino.melody.mixin.mixins.common.client;

import de.keksuccino.melody.resources.audio.MinecraftSoundSettingsObserver;
import net.minecraft.client.Options;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ SoundEngine.class })
public class MixinSoundEngine {

    @Unique
    private static final Logger LOGGER_MELODY = LogManager.getLogger();

    @Shadow
    private boolean loaded;

    @Shadow
    @Final
    private Options options;

    @Inject(method = { "updateCategoryVolume" }, at = { @At("RETURN") })
    private void afterUpdateVolumeCategoryMelody(SoundSource source, float vol, CallbackInfo info) {
        if (this.loaded) {
            MinecraftSoundSettingsObserver.getVolumeListeners().forEach(soundSourceFloatBiConsumer -> soundSourceFloatBiConsumer.accept(source, this.options.getSoundSourceVolume(source)));
        }
    }

    @Inject(method = { "reload" }, at = { @At("RETURN") })
    private void afterReloadSoundEngineMelody(CallbackInfo info) {
        MinecraftSoundSettingsObserver.getSoundEngineReloadListeners().forEach(Runnable::run);
    }
}