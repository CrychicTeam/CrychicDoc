package com.github.alexmodguy.alexscaves.server.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class DirectAquaticMoveControl extends MoveControl {

    private final PathfinderMob entity;

    private final float speedMulti;

    private float yawLimit = 3.0F;

    public DirectAquaticMoveControl(PathfinderMob entity, float speedMulti) {
        super(entity);
        this.entity = entity;
        this.speedMulti = speedMulti;
    }

    public DirectAquaticMoveControl(PathfinderMob entity, float speedMulti, float yawLimit) {
        super(entity);
        this.entity = entity;
        this.yawLimit = yawLimit;
        this.speedMulti = speedMulti;
    }

    @Override
    public void tick() {
        if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
            Vec3 vector3d = new Vec3(this.f_24975_ - this.entity.m_20185_(), this.f_24976_ - this.entity.m_20186_(), this.f_24977_ - this.entity.m_20189_());
            double d5 = vector3d.length();
            if (d5 < 1.0) {
                this.f_24981_ = MoveControl.Operation.WAIT;
                this.entity.m_20256_(this.entity.m_20184_().scale(0.5));
            } else {
                this.entity.m_20256_(this.entity.m_20184_().add(vector3d.scale(this.f_24978_ * 0.1F / d5)));
                Vec3 delta = this.entity.m_20184_();
                float f = -((float) Mth.atan2(delta.x, delta.z)) * 180.0F / (float) Math.PI;
                this.entity.m_146922_(Mth.approachDegrees(this.entity.m_146908_(), f, this.yawLimit));
                this.entity.f_20883_ = this.entity.m_146908_();
            }
        }
    }
}