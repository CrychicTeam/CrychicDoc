package com.github.alexmodguy.alexscaves.client.sound;

import com.github.alexmodguy.alexscaves.server.entity.item.QuarrySmasherEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class QuarrySmasherSound extends AbstractTickableSoundInstance {

    private final QuarrySmasherEntity quarrySmasherEntity;

    private float moveFade = 0.0F;

    private float prevChainLength = 0.0F;

    public QuarrySmasherSound(QuarrySmasherEntity quarrySmasherEntity) {
        super(ACSoundRegistry.BOUNDROID_CHAIN_LOOP.get(), SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
        this.quarrySmasherEntity = quarrySmasherEntity;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119578_ = true;
        this.f_119575_ = (double) ((float) this.quarrySmasherEntity.m_20185_());
        this.f_119576_ = (double) ((float) this.quarrySmasherEntity.m_20186_());
        this.f_119577_ = (double) ((float) this.quarrySmasherEntity.m_20189_());
        this.f_119579_ = 0;
    }

    @Override
    public boolean canPlaySound() {
        return this.quarrySmasherEntity.m_6084_() && !this.quarrySmasherEntity.m_20067_();
    }

    @Override
    public void tick() {
        if (this.quarrySmasherEntity.m_6084_()) {
            this.f_119575_ = (double) ((float) this.quarrySmasherEntity.m_20185_());
            this.f_119576_ = (double) ((float) this.quarrySmasherEntity.m_20186_());
            this.f_119577_ = (double) ((float) this.quarrySmasherEntity.m_20189_());
            float f = 0.0F;
            if (this.quarrySmasherEntity.headPart != null) {
                f = this.quarrySmasherEntity.m_20270_(this.quarrySmasherEntity.headPart);
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

    public boolean isSameEntity(QuarrySmasherEntity entity) {
        return this.quarrySmasherEntity.m_6084_() && this.quarrySmasherEntity.m_19879_() == entity.m_19879_();
    }
}