package com.github.alexmodguy.alexscaves.mixin.client;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.sound.NuclearExplosionSound;
import com.github.alexmodguy.alexscaves.client.sound.UnlimitedPitch;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ SoundEngine.class })
public abstract class SoundEngineMixin {

    @Shadow
    @Final
    private Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel;

    private float lastNukeSoundDampenBy = 0.0F;

    @Shadow
    protected abstract float calculateVolume(SoundInstance var1);

    @Inject(method = { "Lnet/minecraft/client/sounds/SoundEngine;calculatePitch(Lnet/minecraft/client/resources/sounds/SoundInstance;)F" }, at = { @At("HEAD") }, cancellable = true)
    private void ac_calculatePitch(SoundInstance soundInstance, CallbackInfoReturnable<Float> cir) {
        if (soundInstance instanceof UnlimitedPitch) {
            cir.setReturnValue(soundInstance.getPitch());
        }
    }

    @Inject(method = { "Lnet/minecraft/client/sounds/SoundEngine;calculateVolume(Lnet/minecraft/client/resources/sounds/SoundInstance;)F" }, at = { @At("RETURN") }, cancellable = true)
    private void ac_calculateVolume(SoundInstance soundInstance, CallbackInfoReturnable<Float> cir) {
        if (!(soundInstance instanceof NuclearExplosionSound) && ClientProxy.masterVolumeNukeModifier > 0.0F && AlexsCaves.CLIENT_CONFIG.nuclearBombMufflesSounds.get()) {
            float f = Math.max(1.0F - ClientProxy.masterVolumeNukeModifier, 0.01F);
            cir.setReturnValue((Float) cir.getReturnValue() * f);
        }
    }

    @Inject(method = { "Lnet/minecraft/client/sounds/SoundEngine;tickNonPaused()V" }, at = { @At("TAIL") })
    private void ac_tickNonPaused(CallbackInfo ci) {
        if ((this.lastNukeSoundDampenBy != ClientProxy.masterVolumeNukeModifier || ClientProxy.masterVolumeNukeModifier > 0.0F) && AlexsCaves.CLIENT_CONFIG.nuclearBombMufflesSounds.get()) {
            this.dampenSoundsFromNuke();
        }
    }

    public void dampenSoundsFromNuke() {
        for (Entry<SoundInstance, ChannelAccess.ChannelHandle> entry : this.instanceToChannel.entrySet()) {
            ChannelAccess.ChannelHandle channelHandle = (ChannelAccess.ChannelHandle) entry.getValue();
            SoundInstance soundinstance = (SoundInstance) entry.getKey();
            float f = this.calculateVolume(soundinstance);
            channelHandle.execute(sound -> sound.setVolume(f));
        }
        this.lastNukeSoundDampenBy = ClientProxy.masterVolumeNukeModifier;
    }
}