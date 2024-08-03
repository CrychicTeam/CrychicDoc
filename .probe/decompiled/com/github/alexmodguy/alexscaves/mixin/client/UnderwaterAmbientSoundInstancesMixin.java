package com.github.alexmodguy.alexscaves.mixin.client;

import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.UnderwaterAmbientSoundInstances;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ UnderwaterAmbientSoundInstances.UnderwaterAmbientSoundInstance.class })
public abstract class UnderwaterAmbientSoundInstancesMixin extends AbstractTickableSoundInstance {

    @Shadow
    @Final
    private LocalPlayer player;

    @Shadow
    private int fade;

    protected UnderwaterAmbientSoundInstancesMixin(SoundEvent soundEvent, SoundSource soundSource, RandomSource randomSource) {
        super(soundEvent, soundSource, randomSource);
    }

    @Inject(method = { "Lnet/minecraft/client/resources/sounds/UnderwaterAmbientSoundInstances$UnderwaterAmbientSoundInstance;tick()V" }, at = { @At("HEAD") }, cancellable = true)
    private void ac_tick(CallbackInfo ci) {
        if (this.player.isUnderWater() && this.fade > 0 && this.player.m_9236_().m_204166_(this.player.m_20183_()).is(ACTagRegistry.OVERRIDE_UNDERWATER_AMBIENCE_IN)) {
            this.fade = Math.max(this.fade - 5, 0);
            if (this.fade <= 0) {
                this.f_119573_ = 0.0F;
                ci.cancel();
            }
        }
    }
}