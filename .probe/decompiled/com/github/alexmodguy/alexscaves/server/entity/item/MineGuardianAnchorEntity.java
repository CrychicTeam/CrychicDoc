package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class MineGuardianAnchorEntity extends Entity {

    private static final EntityDataAccessor<Optional<UUID>> GUARDIAN_UUID = SynchedEntityData.defineId(MineGuardianAnchorEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> GUARDIAN_ID = SynchedEntityData.defineId(MineGuardianAnchorEntity.class, EntityDataSerializers.INT);

    public MineGuardianAnchorEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public MineGuardianAnchorEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.MINE_GUARDIAN_ANCHOR.get(), level);
        this.m_20011_(this.m_142242_());
    }

    public MineGuardianAnchorEntity(MineGuardianEntity mineGuardianEntity) {
        this(ACEntityRegistry.MINE_GUARDIAN_ANCHOR.get(), mineGuardianEntity.m_9236_());
        this.setGuardianUUID(mineGuardianEntity.m_20148_());
        this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
        this.m_146884_(mineGuardianEntity.m_20182_().add(0.0, 0.5, 0.0));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(GUARDIAN_UUID, Optional.empty());
        this.f_19804_.define(GUARDIAN_ID, -1);
    }

    @Override
    public void tick() {
        super.tick();
        Entity guardian = this.getGuardian();
        if (!this.m_20096_()) {
            this.m_20256_(this.m_20184_().add(0.0, -0.08, 0.0));
        }
        this.m_6478_(MoverType.SELF, this.m_20184_().scale(0.9F));
        this.m_20256_(this.m_20184_().multiply(0.9F, 0.9F, 0.9F));
        if (!this.m_9236_().isClientSide) {
            if (guardian == null && this.f_19797_ > 20) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
            if (guardian instanceof MineGuardianEntity mineGuardian) {
                Entity attackTarget = mineGuardian.m_5448_();
                boolean hasTarget = attackTarget != null && attackTarget.isAlive();
                this.f_19804_.set(GUARDIAN_ID, mineGuardian.m_19879_());
                mineGuardian.setAnchorUUID(this.m_20148_());
                double distance = (double) this.m_20270_(mineGuardian);
                int i = mineGuardian.getMaxChainLength();
                double distanceGoal = (mineGuardian.m_20072_() ? (double) i + Math.sin((double) ((float) this.f_19797_ * 0.1F + (float) i * 0.5F)) * 0.25 : 5.0) + (double) (hasTarget ? 5 : 0);
                double waterHeight = mineGuardian.getFluidTypeHeight(ForgeMod.WATER_TYPE.get());
                double waterUp = Math.min(waterHeight, 1.0) * 0.005F;
                if (mineGuardian.m_20072_() && !hasTarget) {
                    double f = this.m_20185_() + (double) ((float) (-Math.sin((double) ((float) this.f_19797_ * 0.025F + (float) i))) * 0.5F);
                    double f1 = this.m_20189_() + (double) ((float) Math.cos((double) ((float) this.f_19797_ * 0.025F + (float) i)) * 0.5F);
                    double f2 = this.m_20186_() + distanceGoal;
                    Vec3 vec3 = new Vec3(f, f2, f1).subtract(guardian.position());
                    mineGuardian.m_20256_(mineGuardian.m_20184_().add(vec3.scale(waterUp)));
                }
                if (distance > distanceGoal) {
                    double disRem = Math.min(distance - distanceGoal, 1.0) * 0.1F;
                    Vec3 moveTo = this.getChainFrom(1.0F).subtract(mineGuardian.m_20182_());
                    if (moveTo.length() > 1.0) {
                        moveTo = moveTo.normalize();
                    }
                    mineGuardian.m_20256_(mineGuardian.m_20184_().multiply(hasTarget ? 1.0 : 0.8F, hasTarget ? 1.0 : 0.8F, hasTarget ? 1.0 : 0.8F).add(moveTo.scale(disRem)));
                }
            }
        }
    }

    public UUID getGuardianUUID() {
        return (UUID) this.f_19804_.get(GUARDIAN_UUID).orElse(null);
    }

    public void setGuardianUUID(@Nullable UUID uniqueId) {
        this.f_19804_.set(GUARDIAN_UUID, Optional.ofNullable(uniqueId));
    }

    public Entity getGuardian() {
        if (!this.m_9236_().isClientSide) {
            UUID id = this.getGuardianUUID();
            return id == null ? null : ((ServerLevel) this.m_9236_()).getEntity(id);
        } else {
            int id = this.f_19804_.get(GUARDIAN_ID);
            return id == -1 ? null : this.m_9236_().getEntity(id);
        }
    }

    public void linkWithGuardian(Entity head) {
        this.setGuardianUUID(head.getUUID());
        this.f_19804_.set(GUARDIAN_ID, head.getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("GuardianUUID")) {
            this.setGuardianUUID(compound.getUUID("GuardianUUID"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        if (this.getGuardianUUID() != null) {
            compound.putUUID("GuardianUUID", this.getGuardianUUID());
        }
    }

    public Vec3 getChainTo(float partialTicks) {
        return this.getGuardian() instanceof MineGuardianEntity mineGuardianEntity ? mineGuardianEntity.m_20318_(partialTicks) : this.m_20318_(partialTicks).add(0.0, 1.0, 0.0);
    }

    public Vec3 getChainFrom(float partialTicks) {
        return this.m_20318_(partialTicks).add(0.0, 1.0, 0.0);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.m_213877_();
    }

    @Override
    public boolean isPushable() {
        return !this.m_213877_();
    }

    @Override
    public boolean isAttackable() {
        return !this.m_213877_();
    }
}