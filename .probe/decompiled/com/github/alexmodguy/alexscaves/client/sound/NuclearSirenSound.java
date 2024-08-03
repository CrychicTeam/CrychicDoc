package com.github.alexmodguy.alexscaves.client.sound;

import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearSirenBlockEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;

public class NuclearSirenSound extends BlockEntityTickableSound<NuclearSirenBlockEntity> {

    public NuclearSirenSound(NuclearSirenBlockEntity siren) {
        super(ACSoundRegistry.NUCLEAR_SIREN.get(), siren);
        this.f_119573_ = 0.1F;
    }

    @Override
    public boolean canPlaySound() {
        return !this.blockEntity.m_58901_() && this.blockEntity.isActivated(this.blockEntity.m_58900_()) && this.blockEntity.getVolume(1.0F) > 0.0F;
    }

    @Override
    public void tick() {
        if (this.blockEntity != null && !this.blockEntity.m_58901_()) {
            this.f_119575_ = (double) this.blockEntity.m_58899_().m_123341_() + 0.5;
            this.f_119576_ = (double) this.blockEntity.m_58899_().m_123342_() + 0.5;
            this.f_119577_ = (double) this.blockEntity.m_58899_().m_123343_() + 0.5;
            this.f_119573_ = this.blockEntity.getVolume(1.0F);
        } else {
            this.m_119609_();
        }
    }
}