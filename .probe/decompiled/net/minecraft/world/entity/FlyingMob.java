package net.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class FlyingMob extends Mob {

    protected FlyingMob(EntityType<? extends FlyingMob> entityTypeExtendsFlyingMob0, Level level1) {
        super(entityTypeExtendsFlyingMob0, level1);
    }

    @Override
    protected void checkFallDamage(double double0, boolean boolean1, BlockState blockState2, BlockPos blockPos3) {
    }

    @Override
    public void travel(Vec3 vec0) {
        if (this.m_6109_()) {
            if (this.m_20069_()) {
                this.m_19920_(0.02F, vec0);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.8F));
            } else if (this.m_20077_()) {
                this.m_19920_(0.02F, vec0);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.5));
            } else {
                float $$1 = 0.91F;
                if (this.m_20096_()) {
                    $$1 = this.m_9236_().getBlockState(this.m_20099_()).m_60734_().getFriction() * 0.91F;
                }
                float $$2 = 0.16277137F / ($$1 * $$1 * $$1);
                $$1 = 0.91F;
                if (this.m_20096_()) {
                    $$1 = this.m_9236_().getBlockState(this.m_20099_()).m_60734_().getFriction() * 0.91F;
                }
                this.m_19920_(this.m_20096_() ? 0.1F * $$2 : 0.02F, vec0);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale((double) $$1));
            }
        }
        this.m_267651_(false);
    }

    @Override
    public boolean onClimbable() {
        return false;
    }
}