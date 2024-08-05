package dev.xkmc.l2weaponry.content.entity;

import dev.xkmc.l2weaponry.init.registrate.LWEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ThrowingAxeEntity extends BaseThrownWeaponEntity<ThrowingAxeEntity> {

    public ThrowingAxeEntity(EntityType<ThrowingAxeEntity> type, Level pLevel) {
        super(type, pLevel);
    }

    public ThrowingAxeEntity(Level pLevel, LivingEntity pShooter, ItemStack pStack, int slot) {
        super((EntityType<ThrowingAxeEntity>) LWEntities.ET_AXE.get(), pLevel, pShooter, pStack, slot);
    }
}