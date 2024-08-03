package noppes.npcs.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWaterNav extends Goal {

    private EntityNPCInterface entity;

    public EntityAIWaterNav(EntityNPCInterface npc) {
        this.entity = npc;
        npc.m_21573_().setCanFloat(true);
    }

    @Override
    public boolean canUse() {
        if (!this.entity.m_20069_() && !this.entity.m_20077_()) {
            return false;
        } else {
            return this.entity.ais.canSwim ? true : this.entity.f_19862_;
        }
    }

    @Override
    public void tick() {
        if (this.entity.m_217043_().nextFloat() < 0.8F) {
            this.entity.m_21569_().jump();
        }
    }
}