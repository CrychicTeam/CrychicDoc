package net.minecraft.world.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class ThrowableProjectile extends Projectile {

    protected ThrowableProjectile(EntityType<? extends ThrowableProjectile> entityTypeExtendsThrowableProjectile0, Level level1) {
        super(entityTypeExtendsThrowableProjectile0, level1);
    }

    protected ThrowableProjectile(EntityType<? extends ThrowableProjectile> entityTypeExtendsThrowableProjectile0, double double1, double double2, double double3, Level level4) {
        this(entityTypeExtendsThrowableProjectile0, level4);
        this.m_6034_(double1, double2, double3);
    }

    protected ThrowableProjectile(EntityType<? extends ThrowableProjectile> entityTypeExtendsThrowableProjectile0, LivingEntity livingEntity1, Level level2) {
        this(entityTypeExtendsThrowableProjectile0, livingEntity1.m_20185_(), livingEntity1.m_20188_() - 0.1F, livingEntity1.m_20189_(), level2);
        this.m_5602_(livingEntity1);
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

    @Override
    public void tick() {
        super.tick();
        HitResult $$0 = ProjectileUtil.getHitResultOnMoveVector(this, this::m_5603_);
        boolean $$1 = false;
        if ($$0.getType() == HitResult.Type.BLOCK) {
            BlockPos $$2 = ((BlockHitResult) $$0).getBlockPos();
            BlockState $$3 = this.m_9236_().getBlockState($$2);
            if ($$3.m_60713_(Blocks.NETHER_PORTAL)) {
                this.m_20221_($$2);
                $$1 = true;
            } else if ($$3.m_60713_(Blocks.END_GATEWAY)) {
                BlockEntity $$4 = this.m_9236_().getBlockEntity($$2);
                if ($$4 instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                    TheEndGatewayBlockEntity.teleportEntity(this.m_9236_(), $$2, $$3, this, (TheEndGatewayBlockEntity) $$4);
                }
                $$1 = true;
            }
        }
        if ($$0.getType() != HitResult.Type.MISS && !$$1) {
            this.m_6532_($$0);
        }
        this.m_20101_();
        Vec3 $$5 = this.m_20184_();
        double $$6 = this.m_20185_() + $$5.x;
        double $$7 = this.m_20186_() + $$5.y;
        double $$8 = this.m_20189_() + $$5.z;
        this.m_37283_();
        float $$11;
        if (this.m_20069_()) {
            for (int $$9 = 0; $$9 < 4; $$9++) {
                float $$10 = 0.25F;
                this.m_9236_().addParticle(ParticleTypes.BUBBLE, $$6 - $$5.x * 0.25, $$7 - $$5.y * 0.25, $$8 - $$5.z * 0.25, $$5.x, $$5.y, $$5.z);
            }
            $$11 = 0.8F;
        } else {
            $$11 = 0.99F;
        }
        this.m_20256_($$5.scale((double) $$11));
        if (!this.m_20068_()) {
            Vec3 $$13 = this.m_20184_();
            this.m_20334_($$13.x, $$13.y - (double) this.getGravity(), $$13.z);
        }
        this.m_6034_($$6, $$7, $$8);
    }

    protected float getGravity() {
        return 0.03F;
    }
}