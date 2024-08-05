package com.github.alexmodguy.alexscaves.client.sound;

import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class CorrodentSound extends AbstractTickableSoundInstance {

    private final CorrodentEntity corrodent;

    private float moveFade = 0.0F;

    public CorrodentSound(CorrodentEntity corrodent) {
        super(ACSoundRegistry.CORRODENT_DIG_LOOP.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.corrodent = corrodent;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119578_ = true;
        this.f_119575_ = (double) ((float) this.corrodent.m_20185_());
        this.f_119576_ = (double) ((float) this.corrodent.m_20186_());
        this.f_119577_ = (double) ((float) this.corrodent.m_20189_());
        this.f_119579_ = 0;
    }

    @Override
    public boolean canPlaySound() {
        return this.corrodent.m_6084_() && !this.corrodent.m_20067_() && this.corrodent.isDigging();
    }

    @Override
    public void tick() {
        if (this.corrodent.m_6084_() && this.corrodent.isDigging()) {
            this.f_119575_ = (double) ((float) this.corrodent.m_20185_());
            this.f_119576_ = (double) ((float) this.corrodent.m_20186_());
            this.f_119577_ = (double) ((float) this.corrodent.m_20189_());
            float f = (float) this.corrodent.m_20184_().length();
            this.f_119574_ = 1.0F;
            if (f <= 0.01F) {
                this.moveFade = Math.min(1.0F, this.moveFade + 0.1F);
            } else {
                this.moveFade = Math.max(0.0F, this.moveFade - 0.25F);
            }
            this.f_119573_ = 1.0F - this.moveFade;
        } else {
            this.f_119573_ = 0.0F;
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(CorrodentEntity entity) {
        return this.corrodent.m_6084_() && this.corrodent.m_19879_() == entity.m_19879_();
    }
}