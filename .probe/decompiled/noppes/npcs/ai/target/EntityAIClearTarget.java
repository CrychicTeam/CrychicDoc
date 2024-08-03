package noppes.npcs.ai.target;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIClearTarget extends Goal {

    private EntityNPCInterface npc;

    private LivingEntity target;

    public EntityAIClearTarget(EntityNPCInterface npc) {
        this.npc = npc;
    }

    @Override
    public boolean canUse() {
        this.target = this.npc.m_5448_();
        if (this.target == null) {
            return false;
        } else {
            return this.npc.getOwner() != null && !this.npc.isInRange(this.npc.getOwner(), (double) (this.npc.stats.aggroRange * 2)) ? true : this.npc.combatHandler.checkTarget();
        }
    }

    @Override
    public void start() {
        this.npc.setTarget(null);
        if (this.target == this.npc.m_21188_()) {
            this.npc.m_6703_(null);
        }
        super.start();
    }

    @Override
    public void stop() {
        this.npc.m_21573_().stop();
    }
}