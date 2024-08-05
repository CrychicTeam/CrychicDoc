package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;

public class FrozenData {

    public int frozenTicks;

    public boolean isFrozen;

    private boolean triggerClientUpdate;

    public void tickFrozen(LivingEntity entity) {
        if (this.isFrozen) {
            if (entity instanceof EntityIceDragon) {
                this.clearFrozen(entity);
            } else if (entity.m_6060_()) {
                this.clearFrozen(entity);
                entity.m_20095_();
            } else if (entity.isDeadOrDying()) {
                this.clearFrozen(entity);
            } else {
                if (this.frozenTicks > 0) {
                    this.frozenTicks--;
                } else {
                    this.clearFrozen(entity);
                }
                if (this.isFrozen) {
                    if (entity instanceof Player player && player.isCreative()) {
                        return;
                    }
                    entity.m_20256_(entity.m_20184_().multiply(0.25, 1.0, 0.25));
                    if (!(entity instanceof EnderDragon) && !entity.m_20096_()) {
                        entity.m_20256_(entity.m_20184_().add(0.0, -0.2, 0.0));
                    }
                }
            }
        }
    }

    public void setFrozen(LivingEntity target, int duration) {
        if (!this.isFrozen) {
            target.m_5496_(SoundEvents.GLASS_PLACE, 1.0F, 1.0F);
        }
        this.frozenTicks = duration;
        this.isFrozen = true;
        this.triggerClientUpdate = true;
    }

    private void clearFrozen(LivingEntity entity) {
        for (int i = 0; i < 15; i++) {
            entity.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, IafBlockRegistry.DRAGON_ICE.get().defaultBlockState()), entity.m_20185_() + (entity.getRandom().nextDouble() - 0.5) * (double) entity.m_20205_(), entity.m_20186_() + entity.getRandom().nextDouble() * (double) entity.m_20206_(), entity.m_20189_() + (entity.getRandom().nextDouble() - 0.5) * (double) entity.m_20205_(), 0.0, 0.0, 0.0);
        }
        entity.m_5496_(SoundEvents.GLASS_BREAK, 3.0F, 1.0F);
        this.isFrozen = false;
        this.frozenTicks = 0;
        this.triggerClientUpdate = true;
    }

    public void serialize(CompoundTag tag) {
        CompoundTag frozenData = new CompoundTag();
        frozenData.putInt("frozenTicks", this.frozenTicks);
        frozenData.putBoolean("isFrozen", this.isFrozen);
        tag.put("frozenData", frozenData);
    }

    public void deserialize(CompoundTag tag) {
        CompoundTag frozenData = tag.getCompound("frozenData");
        this.frozenTicks = frozenData.getInt("frozenTicks");
        this.isFrozen = frozenData.getBoolean("isFrozen");
    }

    public boolean doesClientNeedUpdate() {
        if (this.triggerClientUpdate) {
            this.triggerClientUpdate = false;
            return true;
        } else {
            return false;
        }
    }
}