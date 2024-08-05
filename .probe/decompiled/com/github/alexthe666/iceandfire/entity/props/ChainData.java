package com.github.alexthe666.iceandfire.entity.props;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ChainData {

    @Nullable
    public List<Entity> chainedTo;

    @Nullable
    private List<Integer> chainedToIds;

    @Nullable
    private List<UUID> chainedToUUIDs;

    private boolean isInitialized;

    private boolean triggerClientUpdate;

    public void tickChain(LivingEntity entity) {
        if (!this.isInitialized) {
            this.initialize(entity.m_9236_());
        }
        if (this.chainedTo != null) {
            for (Entity chain : this.chainedTo) {
                double distance = (double) chain.distanceTo(entity);
                if (distance > 7.0) {
                    double x = (chain.getX() - entity.m_20185_()) / distance;
                    double y = (chain.getY() - entity.m_20186_()) / distance;
                    double z = (chain.getZ() - entity.m_20189_()) / distance;
                    entity.m_20256_(entity.m_20184_().add(x * Math.abs(x) * 0.4, y * Math.abs(y) * 0.2, z * Math.abs(z) * 0.4));
                }
            }
        }
    }

    public List<Entity> getChainedTo() {
        return (List<Entity>) Objects.requireNonNullElse(this.chainedTo, Collections.emptyList());
    }

    public void clearChains() {
        if (this.chainedTo != null) {
            this.chainedTo = null;
            this.triggerClientUpdate = true;
        }
    }

    public void attachChain(Entity chain) {
        if (this.chainedTo == null) {
            this.chainedTo = new ArrayList();
        } else if (this.chainedTo.contains(chain)) {
            return;
        }
        this.chainedTo.add(chain);
        this.triggerClientUpdate = true;
    }

    public void removeChain(Entity chain) {
        if (this.chainedTo != null) {
            this.chainedTo.remove(chain);
            this.triggerClientUpdate = true;
            if (this.chainedTo.isEmpty()) {
                this.chainedTo = null;
            }
        }
    }

    public boolean isChainedTo(Entity toCheck) {
        return this.chainedTo != null && !this.chainedTo.isEmpty() ? this.chainedTo.contains(toCheck) : false;
    }

    public void serialize(CompoundTag tag) {
        CompoundTag chainedData = new CompoundTag();
        ListTag uuids = new ListTag();
        if (this.chainedTo != null) {
            int[] ids = new int[this.chainedTo.size()];
            for (int i = 0; i < this.chainedTo.size(); i++) {
                Entity entity = (Entity) this.chainedTo.get(i);
                ids[i] = entity.getId();
                uuids.add(NbtUtils.createUUID(entity.getUUID()));
            }
            chainedData.putIntArray("chainedToIds", ids);
            chainedData.put("chainedToUUIDs", uuids);
        }
        tag.put("chainedData", chainedData);
    }

    public void deserialize(CompoundTag tag) {
        CompoundTag chainedData = tag.getCompound("chainedData");
        int[] loadedChainedToIds = chainedData.getIntArray("chainedToIds");
        ListTag uuids = chainedData.getList("chainedToUUIDs", 11);
        this.isInitialized = false;
        if (loadedChainedToIds.length > 0) {
            this.chainedToIds = new ArrayList();
            for (int loadedChainedToId : loadedChainedToIds) {
                this.chainedToIds.add(loadedChainedToId);
            }
        } else {
            this.chainedToIds = null;
        }
        if (!uuids.isEmpty()) {
            this.chainedToUUIDs = new ArrayList();
            for (Tag uuid : uuids) {
                this.chainedToUUIDs.add(NbtUtils.loadUUID(uuid));
            }
        } else {
            this.chainedToUUIDs = null;
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

    private void initialize(Level level) {
        List<Entity> entities = new ArrayList();
        if (this.chainedToUUIDs != null && level instanceof ServerLevel serverLevel) {
            for (UUID uuid : this.chainedToUUIDs) {
                Entity entity = serverLevel.getEntity(uuid);
                if (entity != null) {
                    entities.add(entity);
                }
            }
            this.triggerClientUpdate = true;
        } else if (this.chainedToIds != null) {
            for (int id : this.chainedToIds) {
                if (id != -1) {
                    Entity entity = level.getEntity(id);
                    if (entity != null) {
                        entities.add(entity);
                    }
                }
            }
        }
        if (!entities.isEmpty()) {
            this.chainedTo = entities;
        } else {
            this.chainedTo = null;
        }
        this.chainedToIds = null;
        this.chainedToUUIDs = null;
        this.isInitialized = true;
    }
}