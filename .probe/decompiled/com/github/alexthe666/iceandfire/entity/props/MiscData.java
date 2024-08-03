package com.github.alexthe666.iceandfire.entity.props;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class MiscData {

    public int loveTicks;

    public int lungeTicks;

    public boolean hasDismounted;

    @Nullable
    public List<LivingEntity> targetedByScepter;

    @Nullable
    private List<Integer> targetedByScepterIds;

    private boolean isInitialized;

    private boolean triggerClientUpdate;

    public void tickMisc(LivingEntity entity) {
        if (!this.isInitialized) {
            this.initialize(entity.m_9236_());
        }
        if (this.loveTicks > 0) {
            this.loveTicks--;
            if (this.loveTicks == 0) {
                this.triggerClientUpdate = true;
                return;
            }
            if (entity instanceof Mob mob) {
                mob.m_6598_(null);
                mob.m_6703_(null);
                mob.setTarget(null);
                mob.setAggressive(false);
            }
            this.createLoveParticles(entity);
        }
    }

    public List<LivingEntity> getTargetedByScepter() {
        return (List<LivingEntity>) Objects.requireNonNullElse(this.targetedByScepter, Collections.emptyList());
    }

    public void addScepterTarget(LivingEntity target) {
        if (this.targetedByScepter == null) {
            this.targetedByScepter = new ArrayList();
        } else if (this.targetedByScepter.contains(target)) {
            return;
        }
        this.targetedByScepter.add(target);
        this.triggerClientUpdate = true;
    }

    public void removeScepterTarget(LivingEntity target) {
        if (this.targetedByScepter != null) {
            this.targetedByScepter.remove(target);
            this.triggerClientUpdate = true;
        }
    }

    public void setLoveTicks(int loveTicks) {
        this.loveTicks = loveTicks;
        this.triggerClientUpdate = true;
    }

    public void setLungeTicks(int lungeTicks) {
        this.lungeTicks = lungeTicks;
        this.triggerClientUpdate = true;
    }

    public void setDismounted(boolean hasDismounted) {
        this.hasDismounted = hasDismounted;
        this.triggerClientUpdate = true;
    }

    public void serialize(CompoundTag tag) {
        CompoundTag miscData = new CompoundTag();
        miscData.putInt("loveTicks", this.loveTicks);
        miscData.putInt("lungeTicks", this.lungeTicks);
        miscData.putBoolean("hasDismounted", this.hasDismounted);
        if (this.targetedByScepter != null) {
            int[] ids = new int[this.targetedByScepter.size()];
            for (int i = 0; i < this.targetedByScepter.size(); i++) {
                ids[i] = ((LivingEntity) this.targetedByScepter.get(i)).m_19879_();
            }
            tag.putIntArray("targetedByScepterIds", ids);
        }
        tag.put("miscData", miscData);
    }

    public void deserialize(CompoundTag tag) {
        CompoundTag miscData = tag.getCompound("miscData");
        this.loveTicks = miscData.getInt("loveTicks");
        this.lungeTicks = miscData.getInt("lungeTicks");
        this.hasDismounted = miscData.getBoolean("hasDismounted");
        int[] loadedChainedToIds = miscData.getIntArray("targetedByScepterIds");
        this.isInitialized = false;
        if (loadedChainedToIds.length > 0) {
            this.targetedByScepterIds = new ArrayList();
            for (int loadedChainedToId : loadedChainedToIds) {
                this.targetedByScepterIds.add(loadedChainedToId);
            }
        }
    }

    public boolean doesClientNeedUpdate() {
        if (this.triggerClientUpdate) {
            this.triggerClientUpdate = false;
            return true;
        } else {
            return false;
        }
    }

    private void createLoveParticles(LivingEntity entity) {
        if (entity.getRandom().nextInt(7) == 0) {
            for (int i = 0; i < 5; i++) {
                entity.m_9236_().addParticle(ParticleTypes.HEART, entity.m_20185_() + (entity.getRandom().nextDouble() - 0.5) * 3.0, entity.m_20186_() + (entity.getRandom().nextDouble() - 0.5) * 3.0, entity.m_20189_() + (entity.getRandom().nextDouble() - 0.5) * 3.0, 0.0, 0.0, 0.0);
            }
        }
    }

    private void initialize(Level level) {
        List<LivingEntity> entities = new ArrayList();
        if (this.targetedByScepterIds != null) {
            for (int id : this.targetedByScepterIds) {
                if (id != -1 && level.getEntity(id) instanceof LivingEntity livingEntity) {
                    entities.add(livingEntity);
                }
            }
        }
        if (!entities.isEmpty()) {
            this.targetedByScepter = entities;
        } else {
            this.targetedByScepter = null;
        }
        this.targetedByScepterIds = null;
        this.isInitialized = true;
    }
}