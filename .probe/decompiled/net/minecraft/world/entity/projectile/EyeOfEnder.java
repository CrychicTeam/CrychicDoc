package net.minecraft.world.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EyeOfEnder extends Entity implements ItemSupplier {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(EyeOfEnder.class, EntityDataSerializers.ITEM_STACK);

    private double tx;

    private double ty;

    private double tz;

    private int life;

    private boolean surviveAfterDeath;

    public EyeOfEnder(EntityType<? extends EyeOfEnder> entityTypeExtendsEyeOfEnder0, Level level1) {
        super(entityTypeExtendsEyeOfEnder0, level1);
    }

    public EyeOfEnder(Level level0, double double1, double double2, double double3) {
        this(EntityType.EYE_OF_ENDER, level0);
        this.m_6034_(double1, double2, double3);
    }

    public void setItem(ItemStack itemStack0) {
        if (!itemStack0.is(Items.ENDER_EYE) || itemStack0.hasTag()) {
            this.m_20088_().set(DATA_ITEM_STACK, itemStack0.copyWithCount(1));
        }
    }

    private ItemStack getItemRaw() {
        return this.m_20088_().get(DATA_ITEM_STACK);
    }

    @Override
    public ItemStack getItem() {
        ItemStack $$0 = this.getItemRaw();
        return $$0.isEmpty() ? new ItemStack(Items.ENDER_EYE) : $$0;
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double double0) {
        double $$1 = this.m_20191_().getSize() * 4.0;
        if (Double.isNaN($$1)) {
            $$1 = 4.0;
        }
        $$1 *= 64.0;
        return double0 < $$1 * $$1;
    }

    public void signalTo(BlockPos blockPos0) {
        double $$1 = (double) blockPos0.m_123341_();
        int $$2 = blockPos0.m_123342_();
        double $$3 = (double) blockPos0.m_123343_();
        double $$4 = $$1 - this.m_20185_();
        double $$5 = $$3 - this.m_20189_();
        double $$6 = Math.sqrt($$4 * $$4 + $$5 * $$5);
        if ($$6 > 12.0) {
            this.tx = this.m_20185_() + $$4 / $$6 * 12.0;
            this.tz = this.m_20189_() + $$5 / $$6 * 12.0;
            this.ty = this.m_20186_() + 8.0;
        } else {
            this.tx = $$1;
            this.ty = (double) $$2;
            this.tz = $$3;
        }
        this.life = 0;
        this.surviveAfterDeath = this.f_19796_.nextInt(5) > 0;
    }

    @Override
    public void lerpMotion(double double0, double double1, double double2) {
        this.m_20334_(double0, double1, double2);
        if (this.f_19860_ == 0.0F && this.f_19859_ == 0.0F) {
            double $$3 = Math.sqrt(double0 * double0 + double2 * double2);
            this.m_146922_((float) (Mth.atan2(double0, double2) * 180.0F / (float) Math.PI));
            this.m_146926_((float) (Mth.atan2(double1, $$3) * 180.0F / (float) Math.PI));
            this.f_19859_ = this.m_146908_();
            this.f_19860_ = this.m_146909_();
        }
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 $$0 = this.m_20184_();
        double $$1 = this.m_20185_() + $$0.x;
        double $$2 = this.m_20186_() + $$0.y;
        double $$3 = this.m_20189_() + $$0.z;
        double $$4 = $$0.horizontalDistance();
        this.m_146926_(Projectile.lerpRotation(this.f_19860_, (float) (Mth.atan2($$0.y, $$4) * 180.0F / (float) Math.PI)));
        this.m_146922_(Projectile.lerpRotation(this.f_19859_, (float) (Mth.atan2($$0.x, $$0.z) * 180.0F / (float) Math.PI)));
        if (!this.m_9236_().isClientSide) {
            double $$5 = this.tx - $$1;
            double $$6 = this.tz - $$3;
            float $$7 = (float) Math.sqrt($$5 * $$5 + $$6 * $$6);
            float $$8 = (float) Mth.atan2($$6, $$5);
            double $$9 = Mth.lerp(0.0025, $$4, (double) $$7);
            double $$10 = $$0.y;
            if ($$7 < 1.0F) {
                $$9 *= 0.8;
                $$10 *= 0.8;
            }
            int $$11 = this.m_20186_() < this.ty ? 1 : -1;
            $$0 = new Vec3(Math.cos((double) $$8) * $$9, $$10 + ((double) $$11 - $$10) * 0.015F, Math.sin((double) $$8) * $$9);
            this.m_20256_($$0);
        }
        float $$12 = 0.25F;
        if (this.m_20069_()) {
            for (int $$13 = 0; $$13 < 4; $$13++) {
                this.m_9236_().addParticle(ParticleTypes.BUBBLE, $$1 - $$0.x * 0.25, $$2 - $$0.y * 0.25, $$3 - $$0.z * 0.25, $$0.x, $$0.y, $$0.z);
            }
        } else {
            this.m_9236_().addParticle(ParticleTypes.PORTAL, $$1 - $$0.x * 0.25 + this.f_19796_.nextDouble() * 0.6 - 0.3, $$2 - $$0.y * 0.25 - 0.5, $$3 - $$0.z * 0.25 + this.f_19796_.nextDouble() * 0.6 - 0.3, $$0.x, $$0.y, $$0.z);
        }
        if (!this.m_9236_().isClientSide) {
            this.m_6034_($$1, $$2, $$3);
            this.life++;
            if (this.life > 80 && !this.m_9236_().isClientSide) {
                this.m_5496_(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F);
                this.m_146870_();
                if (this.surviveAfterDeath) {
                    this.m_9236_().m_7967_(new ItemEntity(this.m_9236_(), this.m_20185_(), this.m_20186_(), this.m_20189_(), this.getItem()));
                } else {
                    this.m_9236_().m_46796_(2003, this.m_20183_(), 0);
                }
            }
        } else {
            this.m_20343_($$1, $$2, $$3);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        ItemStack $$1 = this.getItemRaw();
        if (!$$1.isEmpty()) {
            compoundTag0.put("Item", $$1.save(new CompoundTag()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        ItemStack $$1 = ItemStack.of(compoundTag0.getCompound("Item"));
        this.setItem($$1);
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}