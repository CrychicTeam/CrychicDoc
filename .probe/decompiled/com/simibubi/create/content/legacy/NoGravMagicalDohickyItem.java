package com.simibubi.create.content.legacy;

import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NoGravMagicalDohickyItem extends Item {

    public NoGravMagicalDohickyItem(Item.Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        Level world = entity.m_9236_();
        Vec3 pos = entity.m_20182_();
        CompoundTag persistentData = entity.getPersistentData();
        if (world.isClientSide) {
            if (world.random.nextFloat() < this.getIdleParticleChance(entity)) {
                Vec3 ppos = VecHelper.offsetRandomly(pos, world.random, 0.5F);
                world.addParticle(ParticleTypes.END_ROD, ppos.x, pos.y, ppos.z, 0.0, -0.1F, 0.0);
            }
            if (entity.m_20067_() && !persistentData.getBoolean("PlayEffects")) {
                Vec3 basemotion = new Vec3(0.0, 1.0, 0.0);
                world.addParticle(ParticleTypes.FLASH, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
                for (int i = 0; i < 20; i++) {
                    Vec3 motion = VecHelper.offsetRandomly(basemotion, world.random, 1.0F);
                    world.addParticle(ParticleTypes.WITCH, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
                    world.addParticle(ParticleTypes.END_ROD, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
                }
                persistentData.putBoolean("PlayEffects", true);
            }
            return false;
        } else {
            entity.m_20242_(true);
            if (!persistentData.contains("JustCreated")) {
                return false;
            } else {
                this.onCreated(entity, persistentData);
                return false;
            }
        }
    }

    protected float getIdleParticleChance(ItemEntity entity) {
        return (float) Mth.clamp(entity.getItem().getCount() - 10, 5, 100) / 64.0F;
    }

    protected void onCreated(ItemEntity entity, CompoundTag persistentData) {
        entity.lifespan = 6000;
        persistentData.remove("JustCreated");
        entity.m_20225_(true);
    }
}