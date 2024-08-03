package net.minecraft.world.entity.item;

import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;

public class PrimedTnt extends Entity implements TraceableEntity {

    private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(PrimedTnt.class, EntityDataSerializers.INT);

    private static final int DEFAULT_FUSE_TIME = 80;

    @Nullable
    private LivingEntity owner;

    public PrimedTnt(EntityType<? extends PrimedTnt> entityTypeExtendsPrimedTnt0, Level level1) {
        super(entityTypeExtendsPrimedTnt0, level1);
        this.f_19850_ = true;
    }

    public PrimedTnt(Level level0, double double1, double double2, double double3, @Nullable LivingEntity livingEntity4) {
        this(EntityType.TNT, level0);
        this.m_6034_(double1, double2, double3);
        double $$5 = level0.random.nextDouble() * (float) (Math.PI * 2);
        this.m_20334_(-Math.sin($$5) * 0.02, 0.2F, -Math.cos($$5) * 0.02);
        this.setFuse(80);
        this.f_19854_ = double1;
        this.f_19855_ = double2;
        this.f_19856_ = double3;
        this.owner = livingEntity4;
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(DATA_FUSE_ID, 80);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public boolean isPickable() {
        return !this.m_213877_();
    }

    @Override
    public void tick() {
        if (!this.m_20068_()) {
            this.m_20256_(this.m_20184_().add(0.0, -0.04, 0.0));
        }
        this.m_6478_(MoverType.SELF, this.m_20184_());
        this.m_20256_(this.m_20184_().scale(0.98));
        if (this.m_20096_()) {
            this.m_20256_(this.m_20184_().multiply(0.7, -0.5, 0.7));
        }
        int $$0 = this.getFuse() - 1;
        this.setFuse($$0);
        if ($$0 <= 0) {
            this.m_146870_();
            if (!this.m_9236_().isClientSide) {
                this.explode();
            }
        } else {
            this.m_20073_();
            if (this.m_9236_().isClientSide) {
                this.m_9236_().addParticle(ParticleTypes.SMOKE, this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_(), 0.0, 0.0, 0.0);
            }
        }
    }

    private void explode() {
        float $$0 = 4.0F;
        this.m_9236_().explode(this, this.m_20185_(), this.m_20227_(0.0625), this.m_20189_(), 4.0F, Level.ExplosionInteraction.TNT);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        compoundTag0.putShort("Fuse", (short) this.getFuse());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        this.setFuse(compoundTag0.getShort("Fuse"));
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }

    @Override
    protected float getEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 0.15F;
    }

    public void setFuse(int int0) {
        this.f_19804_.set(DATA_FUSE_ID, int0);
    }

    public int getFuse() {
        return this.f_19804_.get(DATA_FUSE_ID);
    }
}