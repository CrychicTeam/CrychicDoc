package com.mna.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class ItemInUseLoopingSound extends AbstractTickableSoundInstance {

    private final Player player;

    public ItemInUseLoopingSound(SoundEvent soundIn, Player playerIn) {
        super(soundIn, SoundSource.PLAYERS, playerIn.m_9236_().getRandom());
        this.player = playerIn;
        this.f_119578_ = true;
        this.f_119579_ = 0;
        this.f_119573_ = 0.15F;
    }

    @Override
    public void tick() {
        if (this.player.m_21212_() <= 0) {
            this.f_119573_ -= 0.1F;
            if (this.f_119573_ <= 0.0F) {
                this.m_119609_();
            }
        }
    }
}