package com.simibubi.create.foundation.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class ContinuousSound extends AbstractTickableSoundInstance {

    private float sharedPitch;

    private SoundScape scape;

    private float relativeVolume;

    protected ContinuousSound(SoundEvent event, SoundScape scape, float sharedPitch, float relativeVolume) {
        super(event, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
        this.scape = scape;
        this.sharedPitch = sharedPitch;
        this.relativeVolume = relativeVolume;
        this.f_119578_ = true;
        this.f_119579_ = 0;
        this.f_119582_ = false;
    }

    public void remove() {
        this.m_119609_();
    }

    @Override
    public float getVolume() {
        return this.scape.getVolume() * this.relativeVolume;
    }

    @Override
    public float getPitch() {
        return this.sharedPitch;
    }

    @Override
    public double getX() {
        return this.scape.getMeanPos().x;
    }

    @Override
    public double getY() {
        return this.scape.getMeanPos().y;
    }

    @Override
    public double getZ() {
        return this.scape.getMeanPos().z;
    }

    @Override
    public void tick() {
    }
}