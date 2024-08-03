package noppes.npcs.ai;

import java.util.EnumSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIPounceTarget extends Goal {

    private EntityNPCInterface npc;

    private LivingEntity leapTarget;

    private float leapSpeed = 1.3F;

    public EntityAIPounceTarget(EntityNPCInterface leapingEntity) {
        this.npc = leapingEntity;
        this.m_7021_(EnumSet.of(Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (!this.npc.m_20096_()) {
            return false;
        } else {
            this.leapTarget = this.npc.m_5448_();
            if (this.leapTarget != null && this.npc.m_21574_().hasLineOfSight(this.leapTarget)) {
                return !this.npc.isInRange(this.leapTarget, 4.0) && this.npc.isInRange(this.leapTarget, 8.0) ? this.npc.m_217043_().nextInt(5) == 0 : false;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.npc.m_20096_();
    }

    @Override
    public void start() {
        double varX = this.leapTarget.m_20185_() - this.npc.m_20185_();
        double varY = this.leapTarget.m_20191_().minY - this.npc.m_20191_().minY;
        double varZ = this.leapTarget.m_20189_() - this.npc.m_20189_();
        float varF = (float) Math.sqrt(varX * varX + varZ * varZ);
        float angle = this.getAngleForXYZ(varX, varY, varZ, (double) varF);
        float yaw = (float) (Math.atan2(varX, varZ) * 180.0 / Math.PI);
        Vec3 mo = new Vec3((double) (Mth.sin(yaw / 180.0F * (float) Math.PI) * Mth.cos(angle / 180.0F * (float) Math.PI)), (double) Mth.sin((angle + 1.0F) / 180.0F * (float) Math.PI), (double) (Mth.cos(yaw / 180.0F * (float) Math.PI) * Mth.cos(angle / 180.0F * (float) Math.PI)));
        mo.scale((double) this.leapSpeed);
        this.npc.m_20256_(mo);
    }

    public float getAngleForXYZ(double varX, double varY, double varZ, double horiDist) {
        float g = 0.1F;
        float var1x = this.leapSpeed * this.leapSpeed;
        double var2 = (double) g * horiDist;
        double var3x = (double) g * horiDist * horiDist + 2.0 * varY * (double) var1x;
        double var4 = (double) (var1x * var1x) - (double) g * var3x;
        if (var4 < 0.0) {
            return 90.0F;
        } else {
            float var6 = var1x - (float) Math.sqrt(var4);
            return (float) (Math.atan2((double) var6, var2) * 180.0 / Math.PI);
        }
    }
}