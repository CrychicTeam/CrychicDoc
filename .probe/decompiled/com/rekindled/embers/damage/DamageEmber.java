package com.rekindled.embers.damage;

import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class DamageEmber extends DamageSource {

    boolean indirect = false;

    public DamageEmber(Holder<DamageType> type, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 damageSourcePosition) {
        super(type, directEntity, causingEntity, damageSourcePosition);
    }

    public DamageEmber(Holder<DamageType> type, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
        super(type, directEntity, causingEntity, null);
    }

    public DamageEmber(Holder<DamageType> type, Vec3 damageSourcePosition) {
        super(type, null, null, damageSourcePosition);
    }

    public DamageEmber(Holder<DamageType> type, @Nullable Entity entity) {
        super(type, entity, entity);
    }

    public DamageEmber(Holder<DamageType> type, @Nullable Entity entity, boolean indirect) {
        super(type, entity, entity);
        indirect = true;
    }

    public DamageEmber(Holder<DamageType> type) {
        super(type, null, null, null);
    }

    @Override
    public boolean isIndirect() {
        return this.indirect || super.isIndirect();
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity livingEntity) {
        String s = "death.attack." + this.m_269415_().msgId();
        Entity entity = this.m_7639_();
        if (entity == null) {
            entity = this.m_7640_();
        }
        if (entity == null) {
            entity = livingEntity.getKillCredit();
        }
        ItemStack itemstack;
        if (entity instanceof LivingEntity living) {
            itemstack = living.getMainHandItem();
        } else {
            itemstack = ItemStack.EMPTY;
        }
        if (entity == null) {
            return Component.translatable(s, livingEntity.m_5446_());
        } else {
            return !itemstack.isEmpty() && itemstack.hasCustomHoverName() ? Component.translatable(s + ".item", livingEntity.m_5446_(), entity.getDisplayName(), itemstack.getDisplayName()) : Component.translatable(s + ".player", livingEntity.m_5446_(), entity.getDisplayName());
        }
    }
}