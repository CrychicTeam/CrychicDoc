package net.minecraft.world.entity.boss.enderdragon;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.dimension.end.EndDragonFight;

public class EndCrystal extends Entity {

    private static final EntityDataAccessor<Optional<BlockPos>> DATA_BEAM_TARGET = SynchedEntityData.defineId(EndCrystal.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Boolean> DATA_SHOW_BOTTOM = SynchedEntityData.defineId(EndCrystal.class, EntityDataSerializers.BOOLEAN);

    public int time;

    public EndCrystal(EntityType<? extends EndCrystal> entityTypeExtendsEndCrystal0, Level level1) {
        super(entityTypeExtendsEndCrystal0, level1);
        this.f_19850_ = true;
        this.time = this.f_19796_.nextInt(100000);
    }

    public EndCrystal(Level level0, double double1, double double2, double double3) {
        this(EntityType.END_CRYSTAL, level0);
        this.m_6034_(double1, double2, double3);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(DATA_BEAM_TARGET, Optional.empty());
        this.m_20088_().define(DATA_SHOW_BOTTOM, true);
    }

    @Override
    public void tick() {
        this.time++;
        if (this.m_9236_() instanceof ServerLevel) {
            BlockPos $$0 = this.m_20183_();
            if (((ServerLevel) this.m_9236_()).getDragonFight() != null && this.m_9236_().getBlockState($$0).m_60795_()) {
                this.m_9236_().setBlockAndUpdate($$0, BaseFireBlock.getState(this.m_9236_(), $$0));
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        if (this.getBeamTarget() != null) {
            compoundTag0.put("BeamTarget", NbtUtils.writeBlockPos(this.getBeamTarget()));
        }
        compoundTag0.putBoolean("ShowBottom", this.showsBottom());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        if (compoundTag0.contains("BeamTarget", 10)) {
            this.setBeamTarget(NbtUtils.readBlockPos(compoundTag0.getCompound("BeamTarget")));
        }
        if (compoundTag0.contains("ShowBottom", 1)) {
            this.setShowBottom(compoundTag0.getBoolean("ShowBottom"));
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_6673_(damageSource0)) {
            return false;
        } else if (damageSource0.getEntity() instanceof EnderDragon) {
            return false;
        } else {
            if (!this.m_213877_() && !this.m_9236_().isClientSide) {
                this.m_142687_(Entity.RemovalReason.KILLED);
                if (!damageSource0.is(DamageTypeTags.IS_EXPLOSION)) {
                    DamageSource $$2 = damageSource0.getEntity() != null ? this.m_269291_().explosion(this, damageSource0.getEntity()) : null;
                    this.m_9236_().explode(this, $$2, null, this.m_20185_(), this.m_20186_(), this.m_20189_(), 6.0F, false, Level.ExplosionInteraction.BLOCK);
                }
                this.onDestroyedBy(damageSource0);
            }
            return true;
        }
    }

    @Override
    public void kill() {
        this.onDestroyedBy(this.m_269291_().generic());
        super.kill();
    }

    private void onDestroyedBy(DamageSource damageSource0) {
        if (this.m_9236_() instanceof ServerLevel) {
            EndDragonFight $$1 = ((ServerLevel) this.m_9236_()).getDragonFight();
            if ($$1 != null) {
                $$1.onCrystalDestroyed(this, damageSource0);
            }
        }
    }

    public void setBeamTarget(@Nullable BlockPos blockPos0) {
        this.m_20088_().set(DATA_BEAM_TARGET, Optional.ofNullable(blockPos0));
    }

    @Nullable
    public BlockPos getBeamTarget() {
        return (BlockPos) this.m_20088_().get(DATA_BEAM_TARGET).orElse(null);
    }

    public void setShowBottom(boolean boolean0) {
        this.m_20088_().set(DATA_SHOW_BOTTOM, boolean0);
    }

    public boolean showsBottom() {
        return this.m_20088_().get(DATA_SHOW_BOTTOM);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double double0) {
        return super.shouldRenderAtSqrDistance(double0) || this.getBeamTarget() != null;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.END_CRYSTAL);
    }
}