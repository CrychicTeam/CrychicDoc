package net.minecraft.world.entity.vehicle;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class MinecartTNT extends AbstractMinecart {

    private static final byte EVENT_PRIME = 10;

    private int fuse = -1;

    public MinecartTNT(EntityType<? extends MinecartTNT> entityTypeExtendsMinecartTNT0, Level level1) {
        super(entityTypeExtendsMinecartTNT0, level1);
    }

    public MinecartTNT(Level level0, double double1, double double2, double double3) {
        super(EntityType.TNT_MINECART, level0, double1, double2, double3);
    }

    @Override
    public AbstractMinecart.Type getMinecartType() {
        return AbstractMinecart.Type.TNT;
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.TNT.defaultBlockState();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.fuse > 0) {
            this.fuse--;
            this.m_9236_().addParticle(ParticleTypes.SMOKE, this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_(), 0.0, 0.0, 0.0);
        } else if (this.fuse == 0) {
            this.explode(this.m_20184_().horizontalDistanceSqr());
        }
        if (this.f_19862_) {
            double $$0 = this.m_20184_().horizontalDistanceSqr();
            if ($$0 >= 0.01F) {
                this.explode($$0);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (damageSource0.getDirectEntity() instanceof AbstractArrow $$3 && $$3.m_6060_()) {
            DamageSource $$4 = this.m_269291_().explosion(this, damageSource0.getEntity());
            this.explode($$4, $$3.m_20184_().lengthSqr());
        }
        return super.hurt(damageSource0, float1);
    }

    @Override
    public void destroy(DamageSource damageSource0) {
        double $$1 = this.m_20184_().horizontalDistanceSqr();
        if (!damageSource0.is(DamageTypeTags.IS_FIRE) && !damageSource0.is(DamageTypeTags.IS_EXPLOSION) && !($$1 >= 0.01F)) {
            super.destroy(damageSource0);
        } else {
            if (this.fuse < 0) {
                this.primeFuse();
                this.fuse = this.f_19796_.nextInt(20) + this.f_19796_.nextInt(20);
            }
        }
    }

    @Override
    protected Item getDropItem() {
        return Items.TNT_MINECART;
    }

    protected void explode(double double0) {
        this.explode(null, double0);
    }

    protected void explode(@Nullable DamageSource damageSource0, double double1) {
        if (!this.m_9236_().isClientSide) {
            double $$2 = Math.sqrt(double1);
            if ($$2 > 5.0) {
                $$2 = 5.0;
            }
            this.m_9236_().explode(this, damageSource0, null, this.m_20185_(), this.m_20186_(), this.m_20189_(), (float) (4.0 + this.f_19796_.nextDouble() * 1.5 * $$2), false, Level.ExplosionInteraction.TNT);
            this.m_146870_();
        }
    }

    @Override
    public boolean causeFallDamage(float float0, float float1, DamageSource damageSource2) {
        if (float0 >= 3.0F) {
            float $$3 = float0 / 10.0F;
            this.explode((double) ($$3 * $$3));
        }
        return super.m_142535_(float0, float1, damageSource2);
    }

    @Override
    public void activateMinecart(int int0, int int1, int int2, boolean boolean3) {
        if (boolean3 && this.fuse < 0) {
            this.primeFuse();
        }
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 10) {
            this.primeFuse();
        } else {
            super.m_7822_(byte0);
        }
    }

    public void primeFuse() {
        this.fuse = 80;
        if (!this.m_9236_().isClientSide) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 10);
            if (!this.m_20067_()) {
                this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    public int getFuse() {
        return this.fuse;
    }

    public boolean isPrimed() {
        return this.fuse > -1;
    }

    @Override
    public float getBlockExplosionResistance(Explosion explosion0, BlockGetter blockGetter1, BlockPos blockPos2, BlockState blockState3, FluidState fluidState4, float float5) {
        return !this.isPrimed() || !blockState3.m_204336_(BlockTags.RAILS) && !blockGetter1.getBlockState(blockPos2.above()).m_204336_(BlockTags.RAILS) ? super.m_7077_(explosion0, blockGetter1, blockPos2, blockState3, fluidState4, float5) : 0.0F;
    }

    @Override
    public boolean shouldBlockExplode(Explosion explosion0, BlockGetter blockGetter1, BlockPos blockPos2, BlockState blockState3, float float4) {
        return !this.isPrimed() || !blockState3.m_204336_(BlockTags.RAILS) && !blockGetter1.getBlockState(blockPos2.above()).m_204336_(BlockTags.RAILS) ? super.m_7349_(explosion0, blockGetter1, blockPos2, blockState3, float4) : false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        if (compoundTag0.contains("TNTFuse", 99)) {
            this.fuse = compoundTag0.getInt("TNTFuse");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putInt("TNTFuse", this.fuse);
    }
}