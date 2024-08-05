package com.github.alexthe666.alexsmobs.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class EtherealMoveController extends MoveControl {

    private final Mob parentEntity;

    private final float speedGeneral;

    public EtherealMoveController(Mob parentEntity, float speedGeneral) {
        super(parentEntity);
        this.parentEntity = parentEntity;
        this.speedGeneral = speedGeneral;
    }

    @Override
    public void tick() {
        if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
            Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
            double d0 = vector3d.length();
            this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d.scale(this.f_24978_ * (double) this.speedGeneral * 0.025 / d0)));
            double yAdd = this.f_24976_ - this.parentEntity.m_20186_();
            if (d0 > (double) this.parentEntity.m_20205_()) {
                this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(0.0, (double) this.parentEntity.m_6113_() * (double) this.speedGeneral * Mth.clamp(yAdd, -1.0, 1.0) * 0.6F, 0.0));
                Vec3 vector3d1 = this.parentEntity.m_20184_();
                this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
            }
        } else if (this.f_24981_ == MoveControl.Operation.STRAFE || this.f_24981_ == MoveControl.Operation.JUMPING) {
            this.f_24981_ = MoveControl.Operation.WAIT;
        }
    }
}