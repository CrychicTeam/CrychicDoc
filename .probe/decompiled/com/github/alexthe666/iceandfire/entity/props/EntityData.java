package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.PacketDistributor;

public class EntityData {

    public FrozenData frozenData = new FrozenData();

    public ChainData chainData = new ChainData();

    public SirenData sirenData = new SirenData();

    public ChickenData chickenData = new ChickenData();

    public MiscData miscData = new MiscData();

    public void tick(LivingEntity entity) {
        this.frozenData.tickFrozen(entity);
        this.chainData.tickChain(entity);
        this.sirenData.tickCharmed(entity);
        this.chickenData.tickChicken(entity);
        this.miscData.tickMisc(entity);
        boolean triggerClientUpdate = this.frozenData.doesClientNeedUpdate();
        triggerClientUpdate = this.chainData.doesClientNeedUpdate() || triggerClientUpdate;
        triggerClientUpdate = this.sirenData.doesClientNeedUpdate() || triggerClientUpdate;
        triggerClientUpdate = this.miscData.doesClientNeedUpdate() || triggerClientUpdate;
        if (triggerClientUpdate && !entity.m_9236_().isClientSide()) {
            if (entity instanceof ServerPlayer serverPlayer) {
                IceAndFire.NETWORK_WRAPPER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverPlayer), new SyncEntityData(entity.m_19879_(), this.serialize()));
            } else {
                IceAndFire.NETWORK_WRAPPER.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SyncEntityData(entity.m_19879_(), this.serialize()));
            }
        }
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        this.frozenData.serialize(tag);
        this.chainData.serialize(tag);
        this.sirenData.serialize(tag);
        this.chickenData.serialize(tag);
        this.miscData.serialize(tag);
        return tag;
    }

    public void deserialize(CompoundTag tag) {
        this.frozenData.deserialize(tag);
        this.chainData.deserialize(tag);
        this.sirenData.deserialize(tag);
        this.chickenData.deserialize(tag);
        this.miscData.deserialize(tag);
    }
}