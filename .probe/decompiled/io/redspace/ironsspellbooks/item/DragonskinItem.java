package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DragonskinItem extends Item {

    public DragonskinItem() {
        super(ItemPropertiesHelper.material());
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    public Entity createEntity(Level world, Entity entity, ItemStack itemstack) {
        if (!world.m_45976_(EnderDragon.class, entity.getBoundingBox().inflate(5.0)).isEmpty()) {
            entity.setNoGravity(true);
            if (!world.isClientSide) {
                MagicManager.spawnParticles(world, ParticleHelper.UNSTABLE_ENDER, entity.getX(), entity.getY(), entity.getZ(), 100, 0.0, 0.0, 0.0, 2.0, true);
            }
            entity.setDeltaMovement(0.0, -0.01F, 0.0);
            entity.setGlowingTag(true);
        }
        return null;
    }
}