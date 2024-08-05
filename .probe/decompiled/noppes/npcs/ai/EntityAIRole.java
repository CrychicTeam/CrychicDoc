package noppes.npcs.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIRole extends Goal {

    private EntityNPCInterface npc;

    public EntityAIRole(EntityNPCInterface npc) {
        this.npc = npc;
    }

    @Override
    public boolean canUse() {
        return this.npc.isKilled() ? false : this.npc.role.aiShouldExecute();
    }

    @Override
    public void start() {
        this.npc.role.aiStartExecuting();
    }

    @Override
    public boolean canContinueToUse() {
        return this.npc.isKilled() ? false : this.npc.role.aiContinueExecute();
    }

    @Override
    public void tick() {
        this.npc.role.aiUpdateTask();
    }
}