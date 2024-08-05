package com.simibubi.create.content.kinetics.fan;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class AirCurrentSound extends AbstractTickableSoundInstance {

    private float pitch;

    protected AirCurrentSound(SoundEvent p_i46532_1_, float pitch) {
        super(p_i46532_1_, SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
        this.pitch = pitch;
        this.f_119573_ = 0.01F;
        this.f_119578_ = true;
        this.f_119579_ = 0;
        this.f_119582_ = true;
    }

    @Override
    public void tick() {
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void fadeIn(float maxVolume) {
        this.f_119573_ = Math.min(maxVolume, this.f_119573_ + 0.05F);
    }

    public void fadeOut() {
        this.f_119573_ = Math.max(0.0F, this.f_119573_ - 0.05F);
    }

    public boolean isFaded() {
        return this.f_119573_ == 0.0F;
    }

    @Override
    public float getPitch() {
        return this.pitch;
    }

    public void stopSound() {
        this.m_119609_();
    }
}