package io.redspace.ironsspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.entity.mobs.necromancer.NecromancerEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class CatacombsSkeletonHorse extends SkeletonHorse {

    public CatacombsSkeletonHorse(EntityType<? extends SkeletonHorse> pEntityType, Level level) {
        super(EntityType.SKELETON_HORSE, level);
        this.m_30651_(true);
        NecromancerEntity necromancer = EntityRegistry.NECROMANCER.get().create(level);
        if (necromancer != null) {
            necromancer.m_21530_();
            necromancer.m_8061_(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
            necromancer.m_8061_(EquipmentSlot.HEAD, new ItemStack(ItemRegistry.TARNISHED_CROWN.get()));
            necromancer.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
            necromancer.m_20329_(this);
        }
    }
}