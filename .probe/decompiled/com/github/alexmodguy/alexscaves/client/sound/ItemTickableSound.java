package com.github.alexmodguy.alexscaves.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public abstract class ItemTickableSound extends AbstractTickableSoundInstance {

    protected final LivingEntity user;

    public ItemTickableSound(LivingEntity user, SoundEvent soundEvent) {
        super(soundEvent, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.user = user;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119578_ = true;
        this.f_119575_ = (double) ((float) this.user.m_20185_());
        this.f_119576_ = (double) ((float) this.user.m_20186_());
        this.f_119577_ = (double) ((float) this.user.m_20189_());
        this.f_119579_ = 0;
    }

    @Override
    public boolean canPlaySound() {
        return !this.user.m_20067_() && this.user.isUsingItem() && (this.isValidItem(this.user.getItemInHand(InteractionHand.MAIN_HAND)) || this.isValidItem(this.user.getItemInHand(InteractionHand.OFF_HAND)));
    }

    @Override
    public void tick() {
        ItemStack itemStack = ItemStack.EMPTY;
        if (this.user.isUsingItem()) {
            if (this.isValidItem(this.user.getItemInHand(InteractionHand.MAIN_HAND))) {
                itemStack = this.user.getItemInHand(InteractionHand.MAIN_HAND);
            }
            if (this.isValidItem(this.user.getItemInHand(InteractionHand.OFF_HAND))) {
                itemStack = this.user.getItemInHand(InteractionHand.OFF_HAND);
            }
        }
        if (this.user.isAlive() && !itemStack.isEmpty()) {
            this.f_119575_ = (double) ((float) this.user.m_20185_());
            this.f_119576_ = (double) ((float) this.user.m_20186_());
            this.f_119577_ = (double) ((float) this.user.m_20189_());
            this.tickVolume(itemStack);
        } else {
            this.m_119609_();
        }
    }

    protected abstract void tickVolume(ItemStack var1);

    public abstract boolean isValidItem(ItemStack var1);

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(LivingEntity user) {
        return this.user.isAlive() && this.user.m_19879_() == user.m_19879_();
    }
}