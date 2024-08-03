package com.sihenzhang.crockpot.entity;

import com.sihenzhang.crockpot.block.BirdcageBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Birdcage extends Entity {

    public Birdcage(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.f_19794_ = true;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.m_9236_().isClientSide() && (this.m_20197_().isEmpty() || !(this.m_20075_().m_60734_() instanceof BirdcageBlock))) {
            this.m_146870_();
        }
    }
}