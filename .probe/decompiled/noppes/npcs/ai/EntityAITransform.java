package noppes.npcs.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAITransform extends Goal {

    private EntityNPCInterface npc;

    public EntityAITransform(EntityNPCInterface npc) {
        this.npc = npc;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.npc.isKilled() && !this.npc.isAttacking() && !this.npc.transform.editingModus) {
            return this.npc.m_9236_().isDay() ? this.npc.transform.isActive : !this.npc.transform.isActive;
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.npc.transform.transform(!this.npc.transform.isActive);
    }
}