package com.github.alexmodguy.alexscaves.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class NuclearExplosionSound extends AbstractTickableSoundInstance implements UnlimitedPitch {

    private int tickCount = 0;

    private final int duration;

    private final int fadesAt;

    private final float fadeInBy;

    public NuclearExplosionSound(SoundEvent soundEvent, double x, double y, double z, int duration, int fadesAt, float fadeInBy, boolean looping) {
        super(soundEvent, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119578_ = looping;
        this.f_119575_ = x;
        this.f_119576_ = y;
        this.f_119577_ = z;
        this.duration = duration;
        this.fadesAt = fadesAt;
        this.fadeInBy = fadeInBy;
        this.f_119579_ = 0;
        this.f_119573_ = 0.0F;
    }

    @Override
    public void tick() {
        if (this.tickCount < this.duration) {
            if (this.tickCount >= this.fadesAt) {
                float shrinkVolumeFor = 1.0F / Math.max((float) (this.duration - this.fadesAt), 1.0F);
                this.f_119573_ = Math.max(0.0F, this.f_119573_ - shrinkVolumeFor);
            } else if (this.f_119573_ < 1.0F) {
                this.f_119573_ = Math.min(1.0F, this.f_119573_ + this.fadeInBy);
            }
            this.tickCount++;
        } else {
            this.m_119609_();
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }
}