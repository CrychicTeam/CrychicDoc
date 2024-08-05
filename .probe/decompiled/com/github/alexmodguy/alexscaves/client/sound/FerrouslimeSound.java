package com.github.alexmodguy.alexscaves.client.sound;

import com.github.alexmodguy.alexscaves.server.entity.living.FerrouslimeEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

public class FerrouslimeSound extends AbstractTickableSoundInstance {

    private final FerrouslimeEntity ferrouslime;

    private float moveFade = 0.0F;

    public FerrouslimeSound(FerrouslimeEntity ferrouslime) {
        super(ACSoundRegistry.FERROUSLIME_MOVE_LOOP.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.ferrouslime = ferrouslime;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119578_ = true;
        this.f_119575_ = (double) ((float) this.ferrouslime.m_20185_());
        this.f_119576_ = (double) ((float) this.ferrouslime.m_20186_());
        this.f_119577_ = (double) ((float) this.ferrouslime.m_20189_());
        this.f_119579_ = 0;
    }

    @Override
    public boolean canPlaySound() {
        return this.ferrouslime.m_6084_() && !this.ferrouslime.m_20067_();
    }

    @Override
    public void tick() {
        if (this.ferrouslime.m_6084_()) {
            this.f_119575_ = (double) ((float) this.ferrouslime.m_20185_());
            this.f_119576_ = (double) ((float) this.ferrouslime.m_20186_());
            this.f_119577_ = (double) ((float) this.ferrouslime.m_20189_());
            float f = (float) this.ferrouslime.m_20184_().length();
            if (f <= 0.01F) {
                this.moveFade = Math.min(1.0F, this.moveFade + 0.1F);
            } else {
                this.moveFade = Math.max(0.0F, this.moveFade - 0.25F);
            }
            float f1 = (3.0F - Mth.clamp(this.ferrouslime.getSlimeSize(1.0F), 1.0F, 3.0F)) / 3.0F;
            this.f_119573_ = 1.0F - this.moveFade;
            this.f_119574_ = 1.0F - f1;
        } else {
            this.f_119573_ = 0.0F;
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(FerrouslimeEntity entity) {
        return this.ferrouslime.m_6084_() && this.ferrouslime.m_19879_() == entity.m_19879_();
    }
}