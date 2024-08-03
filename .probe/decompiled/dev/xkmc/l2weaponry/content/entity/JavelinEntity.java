package dev.xkmc.l2weaponry.content.entity;

import dev.xkmc.l2weaponry.init.registrate.LWEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class JavelinEntity extends BaseThrownWeaponEntity<JavelinEntity> {

    public JavelinEntity(EntityType<JavelinEntity> type, Level pLevel) {
        super(type, pLevel);
    }

    public JavelinEntity(Level pLevel, LivingEntity pShooter, ItemStack pStack, int slot) {
        super((EntityType<JavelinEntity>) LWEntities.TE_JAVELIN.get(), pLevel, pShooter, pStack, slot);
        this.m_36767_((byte) 32);
    }
}