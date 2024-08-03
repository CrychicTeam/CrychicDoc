package com.github.alexmodguy.alexscaves.client.sound;

import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class NucleeperSound extends AbstractTickableSoundInstance implements UnlimitedPitch {

    private final NucleeperEntity nucleeper;

    public NucleeperSound(NucleeperEntity nucleeper) {
        super(ACSoundRegistry.NUCLEEPER_CHARGE.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.nucleeper = nucleeper;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119578_ = true;
        this.f_119575_ = (double) ((float) this.nucleeper.m_20185_());
        this.f_119576_ = (double) ((float) this.nucleeper.m_20186_());
        this.f_119577_ = (double) ((float) this.nucleeper.m_20189_());
        this.f_119579_ = 0;
    }

    @Override
    public boolean canPlaySound() {
        return !this.nucleeper.m_20067_() && this.nucleeper.isTriggered();
    }

    @Override
    public void tick() {
        if (this.nucleeper.m_6084_() && this.nucleeper.isTriggered()) {
            this.f_119575_ = (double) ((float) this.nucleeper.m_20185_());
            this.f_119576_ = (double) ((float) this.nucleeper.m_20186_());
            this.f_119577_ = (double) ((float) this.nucleeper.m_20189_());
            float f = this.nucleeper.getCloseProgress(0.0F);
            float f1 = (float) Math.pow((double) f, 0.5);
            this.f_119573_ = 1.0F + f * 2.0F;
            this.f_119574_ = 1.0F + 6.0F * f1;
        } else {
            this.m_119609_();
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(NucleeperEntity nucleeper) {
        return this.nucleeper.m_6084_() && this.nucleeper.m_19879_() == nucleeper.m_19879_();
    }
}