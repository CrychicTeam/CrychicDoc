package com.github.alexmodguy.alexscaves.client.sound;

import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearFurnaceBlockEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;

public class NuclearFurnaceSound extends BlockEntityTickableSound<NuclearFurnaceBlockEntity> {

    private final int criticality;

    private int fade = 0;

    public NuclearFurnaceSound(NuclearFurnaceBlockEntity furnace) {
        super(getSoundFromFurnaceCriticality(furnace.getCriticality()), furnace);
        this.f_119573_ = 0.1F;
        this.criticality = furnace.getCriticality();
    }

    private static SoundEvent getSoundFromFurnaceCriticality(int criticality) {
        return criticality >= 3 ? ACSoundRegistry.NUCLEAR_FURNACE_ACTIVE_SUPERCRITICAL.get() : (criticality == 2 ? ACSoundRegistry.NUCLEAR_FURNACE_ACTIVE_CRITICAL.get() : (criticality > 0 ? ACSoundRegistry.NUCLEAR_FURNACE_ACTIVE_SUBCRITICAL.get() : ACSoundRegistry.NUCLEAR_FURNACE_ACTIVE.get()));
    }

    @Override
    public boolean canPlaySound() {
        return !this.blockEntity.m_58901_();
    }

    public boolean isSameBlockEntity(NuclearFurnaceBlockEntity blockEntity) {
        return super.isSameBlockEntity(blockEntity) && this.criticality == blockEntity.getCriticality();
    }

    @Override
    public void tick() {
        if ((this.blockEntity.isUndergoingFission() || this.criticality > 0) && this.criticality == this.blockEntity.getCriticality()) {
            this.f_119575_ = (double) this.blockEntity.m_58899_().m_123341_() + 1.0;
            this.f_119576_ = (double) this.blockEntity.m_58899_().m_123342_() + 1.0;
            this.f_119577_ = (double) this.blockEntity.m_58899_().m_123343_() + 1.0;
            this.f_119574_ = 1.0F;
            if (this.fade > 0) {
                this.fade--;
            }
        } else {
            this.fade++;
        }
        this.f_119573_ = Mth.clamp(1.0F - (float) this.fade / 40.0F, 0.0F, 1.0F);
        if (this.fade > 40) {
            this.m_119609_();
        }
    }
}