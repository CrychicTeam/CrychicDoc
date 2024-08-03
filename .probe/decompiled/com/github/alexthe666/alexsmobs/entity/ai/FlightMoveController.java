package com.github.alexthe666.alexsmobs.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FlightMoveController extends MoveControl {

    private final Mob parentEntity;

    private final float speedGeneral;

    private final boolean shouldLookAtTarget;

    private final boolean needsYSupport;

    public FlightMoveController(Mob bird, float speedGeneral, boolean shouldLookAtTarget, boolean needsYSupport) {
        super(bird);
        this.parentEntity = bird;
        this.shouldLookAtTarget = shouldLookAtTarget;
        this.speedGeneral = speedGeneral;
        this.needsYSupport = needsYSupport;
    }

    public FlightMoveController(Mob bird, float speedGeneral, boolean shouldLookAtTarget) {
        this(bird, speedGeneral, shouldLookAtTarget, false);
    }

    public FlightMoveController(Mob bird, float speedGeneral) {
        this(bird, speedGeneral, true);
    }

    @Override
    public void tick() {
        if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
            Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
            double d0 = vector3d.length();
            if (d0 < this.parentEntity.m_20191_().getSize()) {
                this.f_24981_ = MoveControl.Operation.WAIT;
                this.parentEntity.m_20256_(this.parentEntity.m_20184_().scale(0.5));
            } else {
                this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d.scale(this.f_24978_ * (double) this.speedGeneral * 0.05 / d0)));
                if (this.needsYSupport) {
                    double d1 = this.f_24976_ - this.parentEntity.m_20186_();
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(0.0, (double) this.parentEntity.m_6113_() * (double) this.speedGeneral * Mth.clamp(d1, -1.0, 1.0) * 0.6F, 0.0));
                }
                if (this.parentEntity.getTarget() != null && this.shouldLookAtTarget) {
                    double d2 = this.parentEntity.getTarget().m_20185_() - this.parentEntity.m_20185_();
                    double d1 = this.parentEntity.getTarget().m_20189_() - this.parentEntity.m_20189_();
                    this.parentEntity.m_146922_(-((float) Mth.atan2(d2, d1)) * (180.0F / (float) Math.PI));
                    this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                } else {
                    Vec3 vector3d1 = this.parentEntity.m_20184_();
                    this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                    this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                }
            }
        } else if (this.f_24981_ == MoveControl.Operation.STRAFE) {
            this.f_24981_ = MoveControl.Operation.WAIT;
        }
    }

    private boolean canReach(Vec3 p_220673_1_, int p_220673_2_) {
        AABB axisalignedbb = this.parentEntity.m_20191_();
        for (int i = 1; i < p_220673_2_; i++) {
            axisalignedbb = axisalignedbb.move(p_220673_1_);
            if (!this.parentEntity.m_9236_().m_45756_(this.parentEntity, axisalignedbb)) {
                return false;
            }
        }
        return true;
    }
}