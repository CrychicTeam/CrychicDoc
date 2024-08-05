package com.github.alexmodguy.alexscaves.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class BlockEntityTickableSound<T extends BlockEntity> extends AbstractTickableSoundInstance implements UnlimitedPitch {

    protected final T blockEntity;

    public BlockEntityTickableSound(SoundEvent soundEvent, T blockEntity) {
        super(soundEvent, SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
        this.blockEntity = blockEntity;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119578_ = true;
        this.f_119575_ = (double) this.blockEntity.getBlockPos().m_123341_() + 0.5;
        this.f_119576_ = (double) this.blockEntity.getBlockPos().m_123342_() + 0.5;
        this.f_119577_ = (double) this.blockEntity.getBlockPos().m_123343_() + 0.5;
        this.f_119579_ = 0;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameBlockEntity(T blockEntity) {
        return this.blockEntity.getBlockPos().equals(blockEntity.getBlockPos());
    }
}