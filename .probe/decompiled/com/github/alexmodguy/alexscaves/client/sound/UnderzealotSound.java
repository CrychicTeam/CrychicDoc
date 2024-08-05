package com.github.alexmodguy.alexscaves.client.sound;

import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class UnderzealotSound extends AbstractTickableSoundInstance implements UnlimitedPitch {

    private final UnderzealotEntity underzealot;

    public UnderzealotSound(UnderzealotEntity underzealot) {
        super(ACSoundRegistry.UNDERZEALOT_CHANT.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.underzealot = underzealot;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119578_ = true;
        this.f_119575_ = (double) ((float) this.underzealot.m_20185_());
        this.f_119576_ = (double) ((float) this.underzealot.m_20186_());
        this.f_119577_ = (double) ((float) this.underzealot.m_20189_());
        this.f_119579_ = 0;
    }

    @Override
    public boolean canPlaySound() {
        return !this.underzealot.m_20067_() && this.underzealot.isPraying() && this.underzealot.getWorshipTime() < 500;
    }

    @Override
    public void tick() {
        if (this.underzealot.m_6084_() && this.underzealot.isPraying()) {
            this.f_119575_ = (double) ((float) this.underzealot.m_20185_());
            this.f_119576_ = (double) ((float) this.underzealot.m_20186_());
            this.f_119577_ = (double) ((float) this.underzealot.m_20189_());
            int time = this.underzealot.getWorshipTime();
            float f = time < 60 ? (float) time / 60.0F : 1.0F;
            float f1 = 1.0F - (float) time / 500.0F;
            this.f_119573_ = f * 5.0F;
            this.f_119574_ = 0.3F + f1 * 0.7F;
        } else {
            this.m_119609_();
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(UnderzealotEntity nucleeper) {
        return this.underzealot.m_6084_() && this.underzealot.m_19879_() == nucleeper.m_19879_();
    }
}