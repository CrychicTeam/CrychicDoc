package noppes.npcs.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAISprintToTarget extends Goal {

    private EntityNPCInterface npc;

    public EntityAISprintToTarget(EntityNPCInterface par1EntityLiving) {
        this.npc = par1EntityLiving;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity runTarget = this.npc.m_5448_();
        if (runTarget != null && !this.npc.m_21573_().isDone()) {
            switch(this.npc.ais.onAttack) {
                case 0:
                    return !this.npc.isInRange(runTarget, 8.0) ? this.npc.m_20096_() : false;
                case 2:
                    return this.npc.isInRange(runTarget, 7.0) ? this.npc.m_20096_() : false;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        Vec3 mo = this.npc.m_20184_();
        return this.npc.isAlive() && this.npc.m_20096_() && this.npc.f_20916_ <= 0 && mo.x != 0.0 && mo.z != 0.0;
    }

    @Override
    public void start() {
        this.npc.m_6858_(true);
    }

    @Override
    public void stop() {
        this.npc.m_6858_(false);
    }
}