package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class ThrownWasteDrumEntity extends Entity {

    private static final EntityDataAccessor<Integer> ON_GROUND_FOR = SynchedEntityData.defineId(ThrownWasteDrumEntity.class, EntityDataSerializers.INT);

    public static final int MAX_TIME = 20;

    private BlockPos removeWasteAt = null;

    public ThrownWasteDrumEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownWasteDrumEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.THROWN_WASTE_DRUM.get(), level);
        this.m_20011_(this.m_142242_());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        if (!this.m_20068_()) {
            this.m_20256_(this.m_20184_().add(0.0, -0.08, 0.0));
        }
        this.m_6478_(MoverType.SELF, this.m_20184_());
        this.m_20256_(this.m_20184_().scale(0.9));
        if (this.m_20096_()) {
            this.setOnGroundFor(this.getOnGroundFor() + 1);
            this.m_20256_(this.m_20184_().multiply(0.7, -0.7, 0.7));
        }
        if (this.m_20184_().length() > 0.03F) {
            AABB killBox = this.m_20191_();
            boolean b = false;
            for (LivingEntity entity : this.m_9236_().m_45976_(LivingEntity.class, killBox)) {
                if (!(entity instanceof BrainiacEntity)) {
                    b = true;
                    entity.hurt(ACDamageTypes.causeAcidDamage(this.m_9236_().registryAccess()), 2.0F);
                }
            }
            if (b) {
                this.m_20256_(this.m_20184_().multiply(0.2, 1.0, 0.2));
            }
        }
        if (this.getOnGroundFor() >= 20 && !this.m_9236_().isClientSide) {
            if (this.getOnGroundFor() == 20) {
                BlockPos landed = this.m_20183_();
                while (landed.m_123342_() < this.m_9236_().m_151558_() && (!this.m_9236_().getBlockState(landed).m_60795_() || !this.m_9236_().getBlockState(landed).m_60819_().isEmpty() && this.m_9236_().getBlockState(landed).m_60819_().getFluidType() != ACFluidRegistry.ACID_FLUID_TYPE.get())) {
                    landed = landed.above();
                }
                this.removeWasteAt = landed;
                if (this.m_9236_().getBlockState(this.removeWasteAt).m_60795_()) {
                    BlockState fluid = ACBlockRegistry.ACID.get().m_49966_();
                    this.m_9236_().setBlockAndUpdate(this.removeWasteAt, fluid);
                }
            }
            if (this.getOnGroundFor() >= 35 && this.removeWasteAt != null) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
                if (this.m_9236_().getFluidState(this.removeWasteAt).getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get()) {
                    this.m_9236_().setBlockAndUpdate(this.removeWasteAt, Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(ON_GROUND_FOR, 0);
    }

    public int getOnGroundFor() {
        return this.f_19804_.get(ON_GROUND_FOR);
    }

    public void setOnGroundFor(int time) {
        this.f_19804_.set(ON_GROUND_FOR, time);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }
}