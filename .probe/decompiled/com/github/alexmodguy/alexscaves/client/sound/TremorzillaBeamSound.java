package com.github.alexmodguy.alexscaves.client.sound;

import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class TremorzillaBeamSound extends AbstractTickableSoundInstance {

    private final TremorzillaEntity tremorzilla;

    public TremorzillaBeamSound(TremorzillaEntity tremorzilla) {
        super(ACSoundRegistry.TREMORZILLA_BEAM_LOOP.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.tremorzilla = tremorzilla;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119575_ = (double) ((float) this.tremorzilla.m_20185_());
        this.f_119576_ = (double) ((float) this.tremorzilla.m_20188_());
        this.f_119577_ = (double) ((float) this.tremorzilla.m_20189_());
        this.f_119578_ = true;
        this.f_119579_ = 0;
        this.f_119573_ = 0.0F;
    }

    @Override
    public boolean canPlaySound() {
        return this.tremorzilla.m_6084_() && !this.tremorzilla.m_20067_() && this.tremorzilla.getBeamProgress(1.0F) > 0.0F;
    }

    @Override
    public void tick() {
        if (this.tremorzilla.m_6084_() && this.tremorzilla.getBeamProgress(1.0F) > 0.0F) {
            this.f_119575_ = (double) ((float) this.tremorzilla.m_20185_());
            this.f_119576_ = (double) ((float) this.tremorzilla.m_20188_());
            this.f_119577_ = (double) ((float) this.tremorzilla.m_20189_());
            this.f_119573_ = this.tremorzilla.getBeamProgress(1.0F);
        } else {
            this.f_119573_ = 0.0F;
            this.m_119609_();
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(TremorzillaEntity tremorzilla) {
        return this.tremorzilla.m_6084_() && this.tremorzilla.m_19879_() == tremorzilla.m_19879_();
    }
}