package com.github.alexmodguy.alexscaves.client.sound;

import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class SubmarineSound extends AbstractTickableSoundInstance {

    private final SubmarineEntity submarine;

    private float moveFade = 0.0F;

    public SubmarineSound(SubmarineEntity submarine) {
        super(ACSoundRegistry.SUBMARINE_MOVE_LOOP.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.submarine = submarine;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119578_ = true;
        this.f_119575_ = (double) ((float) this.submarine.m_20185_());
        this.f_119576_ = (double) ((float) this.submarine.m_20186_());
        this.f_119577_ = (double) ((float) this.submarine.m_20189_());
        this.f_119579_ = 0;
    }

    @Override
    public boolean canPlaySound() {
        return !this.submarine.m_213877_() && !this.submarine.m_20067_();
    }

    @Override
    public void tick() {
        if (this.submarine.m_6084_()) {
            this.f_119575_ = (double) ((float) this.submarine.m_20185_());
            this.f_119576_ = (double) ((float) this.submarine.m_20186_());
            this.f_119577_ = (double) ((float) this.submarine.m_20189_());
            float f = this.submarine.getAcceleration();
            if (!(f <= 0.1F) && this.submarine.m_20160_() && this.submarine.m_20072_()) {
                this.moveFade = Math.max(0.0F, this.moveFade - 0.25F);
            } else {
                this.moveFade = Math.min(1.0F, this.moveFade + 0.1F);
            }
            float f1 = 1.0F - this.moveFade;
            this.f_119573_ = f1;
            this.f_119574_ = 0.8F + f1 * 0.4F;
        } else {
            this.f_119573_ = 0.0F;
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(SubmarineEntity entity) {
        return this.submarine.m_6084_() && this.submarine.m_19879_() == entity.m_19879_();
    }
}