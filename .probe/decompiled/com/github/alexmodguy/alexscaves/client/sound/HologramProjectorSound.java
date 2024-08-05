package com.github.alexmodguy.alexscaves.client.sound;

import com.github.alexmodguy.alexscaves.server.block.blockentity.HologramProjectorBlockEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.client.Minecraft;

public class HologramProjectorSound extends BlockEntityTickableSound<HologramProjectorBlockEntity> {

    public HologramProjectorSound(HologramProjectorBlockEntity hologramProjectorBlockEntity) {
        super(ACSoundRegistry.HOLOGRAM_LOOP.get(), hologramProjectorBlockEntity);
        this.f_119573_ = 0.1F;
    }

    @Override
    public boolean canPlaySound() {
        return !this.blockEntity.m_58901_() && (this.blockEntity.isPlayerRender() || this.blockEntity.getDisplayEntity(Minecraft.getInstance().level) != null) && this.blockEntity.getSwitchAmount(1.0F) > 0.0F;
    }

    @Override
    public void tick() {
        if (this.blockEntity != null && !this.blockEntity.m_58901_()) {
            this.f_119575_ = (double) this.blockEntity.m_58899_().m_123341_() + 0.5;
            this.f_119576_ = (double) this.blockEntity.m_58899_().m_123342_() + 0.5;
            this.f_119577_ = (double) this.blockEntity.m_58899_().m_123343_() + 0.5;
            float f = this.blockEntity.getSwitchAmount(1.0F);
            this.f_119573_ = f * 0.5F;
            this.f_119574_ = 1.0F;
        } else {
            this.m_119609_();
        }
    }
}