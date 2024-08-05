package noppes.npcs.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.entity.EntityNPCInterface;

public class FlyingMoveHelper extends MoveControl {

    private EntityNPCInterface entity;

    private int courseChangeCooldown;

    public FlyingMoveHelper(EntityNPCInterface entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    public void tick() {
        if (this.f_24981_ == MoveControl.Operation.MOVE_TO && this.courseChangeCooldown-- <= 0) {
            this.courseChangeCooldown = 4;
            double d0 = this.m_25000_() - this.entity.m_20185_();
            double d1 = this.m_25001_() - this.entity.m_20186_();
            double d2 = this.m_25002_() - this.entity.m_20189_();
            Vec3 vector3d = new Vec3(this.m_25000_() - this.entity.m_20185_(), this.m_25001_() - this.entity.m_20186_(), this.m_25002_() - this.entity.m_20189_());
            double length = vector3d.length();
            vector3d = vector3d.normalize();
            if (length > 0.5 && this.isNotColliding(vector3d, Mth.ceil(length))) {
                double speed = this.entity.m_21051_(Attributes.MOVEMENT_SPEED).getValue() / 2.5;
                if (length < 3.0 && speed > 0.1F) {
                    speed = 0.1F;
                }
                Vec3 m = this.entity.m_20184_().add(vector3d.scale(speed));
                this.entity.m_20256_(m);
                this.entity.m_146922_(-((float) Math.atan2(m.x, m.z)) * 180.0F / (float) Math.PI);
                this.entity.f_20883_ = this.entity.m_146908_();
            } else {
                this.f_24981_ = MoveControl.Operation.WAIT;
            }
        }
    }

    private boolean isNotColliding(Vec3 vec, int length) {
        AABB axisalignedbb = this.entity.m_20191_();
        for (int i = 1; i < length; i++) {
            axisalignedbb = axisalignedbb.move(vec);
            if (!this.entity.m_9236_().m_45756_(this.entity, axisalignedbb)) {
                return false;
            }
        }
        return true;
    }
}