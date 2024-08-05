package com.mna.entities.sorcery;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.entities.IAffinityEntity;
import com.mna.blocks.ritual.ChimeriteCrystalBlock;
import com.mna.entities.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class AffinityIcon extends Entity implements IAffinityEntity {

    private static final String KEY_NBT_AFFINITY = "affinity";

    private static final EntityDataAccessor<String> AFFINITY = SynchedEntityData.defineId(AffinityIcon.class, EntityDataSerializers.STRING);

    private boolean invalid = false;

    public AffinityIcon(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public AffinityIcon(Level worldIn) {
        super(EntityInit.AFFINITY_ICON.get(), worldIn);
    }

    @Override
    public Affinity getAffinity() {
        try {
            return Affinity.valueOf(this.f_19804_.get(AFFINITY));
        } catch (Exception var2) {
            if (!this.m_9236_().isClientSide()) {
                this.invalid = true;
                ManaAndArtifice.LOGGER.error("Failed to load affinity for Affinity Icon Entry.  Marked as invalid and will remove.");
            }
            return Affinity.UNKNOWN;
        }
    }

    public void setAffinity(Affinity affinity) {
        this.f_19804_.set(AFFINITY, affinity.toString());
    }

    @Override
    public void tick() {
        if (!this.m_9236_().isClientSide()) {
            if (this.m_9236_().getGameTime() % 400L == 0L) {
                this.invalid = true;
                BlockPos checkPos = this.m_20183_();
                for (int i = -1; i <= 1 && this.invalid; i++) {
                    for (int j = -1; j <= 1 && this.invalid; j++) {
                        for (int k = -1; k <= 1 && this.invalid; k++) {
                            if (this.m_9236_().getBlockState(checkPos.offset(i, j, k)).m_60734_() instanceof ChimeriteCrystalBlock) {
                                this.invalid = false;
                            }
                        }
                    }
                }
            }
            if (this.invalid) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(AFFINITY, Affinity.UNKNOWN.toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("affinity")) {
            this.f_19804_.set(AFFINITY, compound.getString("affinity"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putString("affinity", this.f_19804_.get(AFFINITY));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}