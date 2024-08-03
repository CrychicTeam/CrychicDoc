package net.minecraft.world.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;

public class ArrowItem extends Item {

    public ArrowItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    public AbstractArrow createArrow(Level level0, ItemStack itemStack1, LivingEntity livingEntity2) {
        Arrow $$3 = new Arrow(level0, livingEntity2);
        $$3.setEffectsFromItem(itemStack1);
        return $$3;
    }
}