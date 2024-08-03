package noppes.npcs.ai;

import java.util.EnumSet;
import java.util.List;
import net.minecraft.world.entity.ai.goal.Goal;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIMovingPath extends Goal {

    private EntityNPCInterface npc;

    private int[] pos;

    private int retries = 0;

    public EntityAIMovingPath(EntityNPCInterface iNpc) {
        this.npc = iNpc;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.npc.isAttacking() && !this.npc.isInteracting() && (this.npc.m_217043_().nextInt(40) == 0 || !this.npc.ais.movingPause) && this.npc.m_21573_().isDone()) {
            List<int[]> list = this.npc.ais.getMovingPath();
            if (list.size() < 2) {
                return false;
            } else {
                this.npc.ais.incrementMovingPath();
                this.pos = this.npc.ais.getCurrentMovingPath();
                this.retries = 0;
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.npc.isAttacking() || this.npc.isInteracting()) {
            this.npc.ais.decreaseMovingPath();
            return false;
        } else if (this.npc.m_21573_().isDone()) {
            this.npc.m_21573_().stop();
            if (this.npc.m_20275_((double) this.pos[0], (double) this.pos[1], (double) this.pos[2]) < 3.0) {
                return false;
            } else if (this.retries++ < 3) {
                this.start();
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void start() {
        this.npc.m_21573_().moveTo((double) this.pos[0] + 0.5, (double) this.pos[1], (double) this.pos[2] + 0.5, 1.0);
    }
}