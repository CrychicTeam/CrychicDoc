package com.github.alexmodguy.alexscaves.client.sound;

import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidWinchEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class BoundroidSound extends AbstractTickableSoundInstance {

    private final BoundroidEntity boundroid;

    private float moveFade = 0.0F;

    private float prevChainLength = 0.0F;

    public BoundroidSound(BoundroidEntity boundroid) {
        super(ACSoundRegistry.BOUNDROID_CHAIN_LOOP.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.boundroid = boundroid;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119578_ = true;
        this.f_119575_ = (double) ((float) this.boundroid.m_20185_());
        this.f_119576_ = (double) ((float) this.boundroid.m_20186_());
        this.f_119577_ = (double) ((float) this.boundroid.m_20189_());
        this.f_119579_ = 0;
    }

    @Override
    public boolean canPlaySound() {
        return this.boundroid.m_6084_() && !this.boundroid.m_20067_();
    }

    @Override
    public void tick() {
        if (this.boundroid.m_6084_()) {
            this.f_119575_ = (double) ((float) this.boundroid.m_20185_());
            this.f_119576_ = (double) ((float) this.boundroid.m_20186_());
            this.f_119577_ = (double) ((float) this.boundroid.m_20189_());
            float f = 0.0F;
            if (this.boundroid.getWinch() instanceof BoundroidWinchEntity winchEntity) {
                f = winchEntity.m_20270_(this.boundroid);
            }
            float f1 = Math.min(Math.abs(this.prevChainLength - f) * 20.0F, 1.0F);
            if (f1 <= 0.3F) {
                this.moveFade = Math.min(1.0F, this.moveFade + 0.1F);
            } else {
                this.moveFade = Math.max(0.0F, this.moveFade - 0.25F);
            }
            this.f_119573_ = 1.0F - this.moveFade;
            this.f_119574_ = 1.0F + f1;
            this.prevChainLength = f;
        } else {
            this.f_119573_ = 0.0F;
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(BoundroidEntity entity) {
        return this.boundroid.m_6084_() && this.boundroid.m_19879_() == entity.m_19879_();
    }
}