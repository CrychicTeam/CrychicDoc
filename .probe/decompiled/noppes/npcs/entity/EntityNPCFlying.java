package noppes.npcs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class EntityNPCFlying extends EntityNPCInterface {

    public EntityNPCFlying(EntityType<? extends PathfinderMob> type, Level world) {
        super(type, world);
    }

    @Override
    public boolean canFly() {
        return this.ais.movementType > 0;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return !this.canFly() ? super.causeFallDamage(distance, damageMultiplier, source) : false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        if (!this.canFly()) {
            super.m_7840_(y, onGroundIn, state, pos);
        }
    }

    @Override
    public void travel(Vec3 v) {
        if (!this.canFly()) {
            super.travel(v);
        } else {
            Vec3 m = this.m_20184_();
            if (!this.m_20069_() && this.ais.movementType == 2) {
                m = m.subtract(0.0, 0.15, 0.0);
            }
            if (this.m_20069_() && this.ais.movementType == 1) {
                this.m_19920_(0.02F, v);
                this.m_6478_(MoverType.SELF, m);
                m = this.m_20184_().scale(0.8);
            } else if (this.m_20077_()) {
                this.m_19920_(0.02F, v);
                this.m_6478_(MoverType.SELF, m);
                m = this.m_20184_().scale(0.5);
            } else {
                BlockPos ground = new BlockPos((int) this.m_20185_(), (int) (this.m_20186_() - 1.0), (int) this.m_20189_());
                float f = 0.91F;
                if (this.m_20096_()) {
                    f = this.m_9236_().getBlockState(ground).getFriction(this.m_9236_(), ground, this) * 0.91F;
                }
                float f1 = 0.16277137F / (f * f * f);
                f = 0.91F;
                if (this.m_20096_()) {
                    f = this.m_9236_().getBlockState(ground).getFriction(this.m_9236_(), ground, this) * 0.91F;
                }
                this.m_19920_(this.m_20096_() ? 0.1F * f1 : 0.02F, v);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                m = this.m_20184_().scale((double) f);
            }
            this.m_20256_(m);
            this.m_267651_(false);
        }
    }

    @Override
    public boolean onClimbable() {
        return false;
    }
}