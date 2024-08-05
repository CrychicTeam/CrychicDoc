package com.github.alexmodguy.alexscaves.client.sound;

import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class NotorHologramSound extends AbstractTickableSoundInstance {

    private final NotorEntity notor;

    public NotorHologramSound(NotorEntity notor) {
        super(ACSoundRegistry.HOLOGRAM_LOOP.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.notor = notor;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119578_ = true;
        this.f_119575_ = (double) ((float) this.notor.m_20185_());
        this.f_119576_ = (double) ((float) this.notor.m_20186_());
        this.f_119577_ = (double) ((float) this.notor.m_20189_());
        this.f_119579_ = 0;
    }

    @Override
    public boolean canPlaySound() {
        return this.notor.m_6084_() && !this.notor.m_20067_() && (this.notor.showingHologram() || this.notor.getScanningMob() != null);
    }

    @Override
    public void tick() {
        if (!this.notor.m_6084_() || !this.notor.showingHologram() && this.notor.getScanningMob() == null) {
            this.m_119609_();
        } else {
            this.f_119575_ = (double) ((float) this.notor.m_20185_());
            this.f_119576_ = (double) ((float) this.notor.m_20186_());
            this.f_119577_ = (double) ((float) this.notor.m_20189_());
            float f = this.notor.getBeamProgress(1.0F);
            this.f_119573_ = 4.0F * f;
            this.f_119574_ = 1.0F;
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(NotorEntity entity) {
        return this.notor.m_6084_() && this.notor.m_19879_() == entity.m_19879_();
    }
}