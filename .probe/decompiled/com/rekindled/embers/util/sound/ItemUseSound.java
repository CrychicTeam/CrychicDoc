package com.rekindled.embers.util.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ItemUseSound extends AbstractTickableSoundInstance {

    LivingEntity linkedEntity;

    Item itemType;

    public ItemUseSound(LivingEntity linkedEntity, Item itemType, SoundEvent soundIn, SoundSource categoryIn, boolean repeatIn, float volumeIn, float pitchIn) {
        super(soundIn, categoryIn, linkedEntity.m_9236_().getRandom());
        this.linkedEntity = linkedEntity;
        this.itemType = itemType;
        this.f_119573_ = volumeIn;
        this.f_119574_ = pitchIn;
        this.f_119578_ = repeatIn;
    }

    @Override
    public void tick() {
        if (this.linkedEntity == null) {
            this.m_119609_();
        } else {
            ItemStack heldItem = this.linkedEntity.getItemInHand(this.linkedEntity.getUsedItemHand());
            if (!this.linkedEntity.m_213877_() && this.linkedEntity.isUsingItem() && heldItem.getItem() == this.itemType) {
                this.f_119575_ = this.linkedEntity.m_20185_();
                this.f_119576_ = this.linkedEntity.m_20186_();
                this.f_119577_ = this.linkedEntity.m_20189_();
            } else {
                this.m_119609_();
            }
        }
    }
}