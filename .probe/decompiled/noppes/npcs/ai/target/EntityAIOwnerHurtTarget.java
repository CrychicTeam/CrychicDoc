package noppes.npcs.ai.target;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIOwnerHurtTarget extends TargetGoal {

    EntityNPCInterface npc;

    LivingEntity theTarget;

    private int field_142050_e;

    public EntityAIOwnerHurtTarget(EntityNPCInterface npc) {
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
                this.theTarget = entitylivingbase.getLastHurtMob();
                int i = entitylivingbase.getLastHurtMobTimestamp();
                return i != this.field_142050_e && this.m_26150_(this.theTarget, TargetingConditions.DEFAULT);
            }
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.npc.setTarget(this.theTarget);
        LivingEntity entitylivingbase = this.npc.getOwner();
        if (entitylivingbase != null) {
            this.field_142050_e = entitylivingbase.getLastHurtMobTimestamp();
        }
        super.start();
    }
}