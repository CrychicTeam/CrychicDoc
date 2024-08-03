package com.mna.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class EntityAliveLoopingSound extends AbstractTickableSoundInstance {

    private final Entity target;

    public EntityAliveLoopingSound(SoundEvent soundIn, Entity targetEntity) {
        super(soundIn, SoundSource.PLAYERS, targetEntity.level().getRandom());
        this.target = targetEntity;
        this.f_119578_ = true;
        this.f_119579_ = 0;
    }

    @Override
    public void tick() {
        if (!this.target.isAlive()) {
            this.f_119573_ -= 0.1F;
            if (this.f_119573_ <= 0.0F) {
                this.m_119609_();
            }
        }
    }
}