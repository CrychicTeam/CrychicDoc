package noppes.npcs.ai.target;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIOwnerHurtByTarget extends TargetGoal {

    EntityNPCInterface npc;

    LivingEntity theOwnerAttacker;

    private int timer;

    public EntityAIOwnerHurtByTarget(EntityNPCInterface npc) {
        super(npc, false);
        this.npc = npc;
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.npc.isFollower() && this.npc.role.defendOwner()) {
            LivingEntity entitylivingbase = this.npc.getOwner();
            if (entitylivingbase == null) {
                return false;
            } else {
                this.theOwnerAttacker = entitylivingbase.getLastHurtByMob();
                int i = entitylivingbase.getLastHurtByMobTimestamp();
                return i != this.timer && this.m_26150_(this.theOwnerAttacker, TargetingConditions.DEFAULT);
            }
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.npc.setTarget(this.theOwnerAttacker);
        LivingEntity entitylivingbase = this.npc.getOwner();
        if (entitylivingbase != null) {
            this.timer = entitylivingbase.getLastHurtByMobTimestamp();
        }
        super.start();
    }
}