package noppes.npcs.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIJob extends Goal {

    private EntityNPCInterface npc;

    public EntityAIJob(EntityNPCInterface npc) {
        this.npc = npc;
    }

    @Override
    public boolean canUse() {
        return this.npc.isKilled() ? false : this.npc.job.aiShouldExecute();
    }

    @Override
    public void start() {
        this.npc.job.aiStartExecuting();
    }

    @Override
    public boolean canContinueToUse() {
        return this.npc.isKilled() ? false : this.npc.job.aiContinueExecute();
    }

    @Override
    public void tick() {
        this.npc.job.aiUpdateTask();
    }

    @Override
    public void stop() {
        this.npc.job.stop();
    }

    @Override
    public EnumSet<Goal.Flag> getFlags() {
        return this.npc.job.getFlags();
    }
}