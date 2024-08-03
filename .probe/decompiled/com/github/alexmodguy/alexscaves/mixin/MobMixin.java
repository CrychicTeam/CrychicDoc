package com.github.alexmodguy.alexscaves.mixin;

import com.github.alexmodguy.alexscaves.server.entity.util.EntityDropChanceAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ Mob.class })
public abstract class MobMixin extends LivingEntity implements EntityDropChanceAccessor {

    @Shadow
    private boolean canPickUpLoot;

    @Shadow
    protected abstract float getEquipmentDropChance(EquipmentSlot var1);

    @Shadow
    public abstract void setDropChance(EquipmentSlot var1, float var2);

    @Shadow
    @Override
    protected abstract void dropCustomDeathLoot(DamageSource var1, int var2, boolean var3);

    public MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public float ac_getEquipmentDropChance(EquipmentSlot equipmentSlot) {
        return this.getEquipmentDropChance(equipmentSlot);
    }

    @Override
    public void ac_setDropChance(EquipmentSlot equipmentSlot, float chance) {
        this.setDropChance(equipmentSlot, chance);
    }

    @Override
    public void ac_dropCustomDeathLoot(DamageSource damageSource, int i1, boolean idk) {
        this.dropCustomDeathLoot(damageSource, i1, idk);
    }
}