package net.minecraft.world.entity.ai.control;

import java.util.Optional;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class LookControl implements Control {

    protected final Mob mob;

    protected float yMaxRotSpeed;

    protected float xMaxRotAngle;

    protected int lookAtCooldown;

    protected double wantedX;

    protected double wantedY;

    protected double wantedZ;

    public LookControl(Mob mob0) {
        this.mob = mob0;
    }

    public void setLookAt(Vec3 vec0) {
        this.setLookAt(vec0.x, vec0.y, vec0.z);
    }

    public void setLookAt(Entity entity0) {
        this.setLookAt(entity0.getX(), getWantedY(entity0), entity0.getZ());
    }

    public void setLookAt(Entity entity0, float float1, float float2) {
        this.setLookAt(entity0.getX(), getWantedY(entity0), entity0.getZ(), float1, float2);
    }

    public void setLookAt(double double0, double double1, double double2) {
        this.setLookAt(double0, double1, double2, (float) this.mob.getHeadRotSpeed(), (float) this.mob.getMaxHeadXRot());
    }

    public void setLookAt(double double0, double double1, double double2, float float3, float float4) {
        this.wantedX = double0;
        this.wantedY = double1;
        this.wantedZ = double2;
        this.yMaxRotSpeed = float3;
        this.xMaxRotAngle = float4;
        this.lookAtCooldown = 2;
    }

    public void tick() {
        if (this.resetXRotOnTick()) {
            this.mob.m_146926_(0.0F);
        }
        if (this.lookAtCooldown > 0) {
            this.lookAtCooldown--;
            this.getYRotD().ifPresent(p_287447_ -> this.mob.f_20885_ = this.rotateTowards(this.mob.f_20885_, p_287447_, this.yMaxRotSpeed));
            this.getXRotD().ifPresent(p_289400_ -> this.mob.m_146926_(this.rotateTowards(this.mob.m_146909_(), p_289400_, this.xMaxRotAngle)));
        } else {
            this.mob.f_20885_ = this.rotateTowards(this.mob.f_20885_, this.mob.f_20883_, 10.0F);
        }
        this.clampHeadRotationToBody();
    }

    protected void clampHeadRotationToBody() {
        if (!this.mob.getNavigation().isDone()) {
            this.mob.f_20885_ = Mth.rotateIfNecessary(this.mob.f_20885_, this.mob.f_20883_, (float) this.mob.getMaxHeadYRot());
        }
    }

    protected boolean resetXRotOnTick() {
        return true;
    }

    public boolean isLookingAtTarget() {
        return this.lookAtCooldown > 0;
    }

    public double getWantedX() {
        return this.wantedX;
    }

    public double getWantedY() {
        return this.wantedY;
    }

    public double getWantedZ() {
        return this.wantedZ;
    }

    protected Optional<Float> getXRotD() {
        double $$0 = this.wantedX - this.mob.m_20185_();
        double $$1 = this.wantedY - this.mob.m_20188_();
        double $$2 = this.wantedZ - this.mob.m_20189_();
        double $$3 = Math.sqrt($$0 * $$0 + $$2 * $$2);
        return !(Math.abs($$1) > 1.0E-5F) && !(Math.abs($$3) > 1.0E-5F) ? Optional.empty() : Optional.of((float) (-(Mth.atan2($$1, $$3) * 180.0F / (float) Math.PI)));
    }

    protected Optional<Float> getYRotD() {
        double $$0 = this.wantedX - this.mob.m_20185_();
        double $$1 = this.wantedZ - this.mob.m_20189_();
        return !(Math.abs($$1) > 1.0E-5F) && !(Math.abs($$0) > 1.0E-5F) ? Optional.empty() : Optional.of((float) (Mth.atan2($$1, $$0) * 180.0F / (float) Math.PI) - 90.0F);
    }

    protected float rotateTowards(float float0, float float1, float float2) {
        float $$3 = Mth.degreesDifference(float0, float1);
        float $$4 = Mth.clamp($$3, -float2, float2);
        return float0 + $$4;
    }

    private static double getWantedY(Entity entity0) {
        return entity0 instanceof LivingEntity ? entity0.getEyeY() : (entity0.getBoundingBox().minY + entity0.getBoundingBox().maxY) / 2.0;
    }
}