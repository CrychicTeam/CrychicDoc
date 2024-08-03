package io.redspace.ironsspellbooks.entity;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class VisualFallingBlockEntity extends FallingBlockEntity {

    int maxAge = 200;

    private double originalX;

    private double originalY;

    private double originalZ;

    private double ticks;

    private boolean particlesOnImpact;

    public VisualFallingBlockEntity(EntityType<? extends VisualFallingBlockEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void setOnGround(boolean pOnGround) {
    }

    @Override
    public boolean onGround() {
        return this.f_19797_ > 1 && this.m_20184_().lengthSqr() < 0.001F;
    }

    public VisualFallingBlockEntity(Level pLevel, double pX, double pY, double pZ, BlockState pState) {
        this(EntityRegistry.FALLING_BLOCK.get(), pLevel);
        this.originalX = pX;
        this.originalY = pY;
        this.originalZ = pZ;
        this.ticks = 0.0;
        this.f_19850_ = false;
        this.f_31946_ = pState;
        this.m_6034_(pX + 0.5, pY, pZ + 0.5);
        this.f_19854_ = pX;
        this.f_19855_ = pY;
        this.f_19856_ = pZ;
        this.m_31959_(this.m_20183_());
        this.f_31943_ = false;
        this.f_31947_ = true;
    }

    public VisualFallingBlockEntity(Level pLevel, double pX, double pY, double pZ, BlockState pState, int maxAge) {
        this(pLevel, pX, pY, pZ, pState);
        this.maxAge = maxAge;
    }

    public VisualFallingBlockEntity(Level pLevel, double pX, double pY, double pZ, BlockState pState, int maxAge, boolean particlesOnImpact) {
        this(pLevel, pX, pY, pZ, pState, maxAge);
        this.particlesOnImpact = particlesOnImpact;
    }

    @Override
    public void tick() {
        boolean onGround = this.onGround();
        if (!this.f_31946_.m_60795_() && !onGround && this.f_19797_ <= this.maxAge) {
            this.m_6478_(MoverType.SELF, this.m_20184_());
            if (!this.m_20068_() && !this.onGround()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.08, 0.0));
            }
        } else {
            if (onGround) {
                this.callOnBrokenAfterFall(this.f_19853_.getBlockState(this.m_20183_().below()).m_60734_(), this.m_20183_());
            }
            this.m_146870_();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.f_31943_ = false;
        this.f_31947_ = true;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void callOnBrokenAfterFall(Block pBlock, BlockPos pPos) {
        if (!this.f_19853_.isClientSide && this.particlesOnImpact) {
            MagicManager.spawnParticles(this.f_19853_, new BlockParticleOption(ParticleTypes.BLOCK, this.f_31946_), this.m_20185_(), this.m_20186_(), this.m_20189_(), 25, 0.25, 0.25, 0.25, 0.04, false);
        }
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }
}