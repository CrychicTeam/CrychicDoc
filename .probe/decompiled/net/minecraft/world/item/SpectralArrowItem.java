package net.minecraft.world.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.level.Level;

public class SpectralArrowItem extends ArrowItem {

    public SpectralArrowItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public AbstractArrow createArrow(Level level0, ItemStack itemStack1, LivingEntity livingEntity2) {
        return new SpectralArrow(level0, livingEntity2);
    }
}