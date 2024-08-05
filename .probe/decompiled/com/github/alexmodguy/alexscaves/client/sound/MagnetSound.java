package com.github.alexmodguy.alexscaves.client.sound;

import com.github.alexmodguy.alexscaves.server.block.blockentity.MagnetBlockEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;

public class MagnetSound extends BlockEntityTickableSound<MagnetBlockEntity> {

    private float activeAmount = 0.0F;

    public MagnetSound(MagnetBlockEntity magnetBlockEntity) {
        super(magnetBlockEntity.isAzure() ? ACSoundRegistry.AZURE_NEODYMIUM_PUSH_LOOP.get() : ACSoundRegistry.SCARLET_NEODYMIUM_PULL_LOOP.get(), magnetBlockEntity);
        this.f_119573_ = 0.0F;
    }

    @Override
    public boolean canPlaySound() {
        return !this.blockEntity.m_58901_() && this.blockEntity.isLocallyActive();
    }

    @Override
    public void tick() {
        if (this.activeAmount < 1.0F && this.blockEntity.isLocallyActive()) {
            this.activeAmount += 0.1F;
        }
        if (this.activeAmount > 0.0F && !this.blockEntity.isLocallyActive()) {
            this.activeAmount -= 0.1F;
        }
        if (this.blockEntity == null || this.blockEntity.m_58901_() || this.activeAmount == 0.0F && !this.blockEntity.isLocallyActive()) {
            this.m_119609_();
        } else {
            this.f_119575_ = (double) this.blockEntity.m_58899_().m_123341_() + 0.5;
            this.f_119576_ = (double) this.blockEntity.m_58899_().m_123342_() + 0.5;
            this.f_119577_ = (double) this.blockEntity.m_58899_().m_123343_() + 0.5;
            this.f_119573_ = this.activeAmount * 0.5F;
            this.f_119574_ = 1.0F;
        }
    }

    public boolean isSameBlockEntity(MagnetBlockEntity blockEntity) {
        return super.isSameBlockEntity(blockEntity) && this.blockEntity.isAzure() == blockEntity.isAzure();
    }
}