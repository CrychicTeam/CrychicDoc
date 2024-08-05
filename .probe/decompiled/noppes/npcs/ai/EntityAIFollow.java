package noppes.npcs.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIFollow extends Goal {

    private EntityNPCInterface npc;

    private LivingEntity owner;

    public int updateTick = 0;

    public EntityAIFollow(EntityNPCInterface npc) {
        this.npc = npc;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return !this.canExcute() ? false : !this.npc.isInRange(this.owner, (double) this.npc.followRange());
    }

    public boolean canExcute() {
        return this.npc.isAlive() && this.npc.isFollower() && !this.npc.isAttacking() && (this.owner = this.npc.getOwner()) != null && this.npc.ais.animationType != 1;
    }

    @Override
    public void start() {
        this.updateTick = 10;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.npc.m_21573_().isDone() && !this.npc.isInRange(this.owner, 2.0) && this.canExcute();
    }

    @Override
    public void stop() {
        this.owner = null;
        this.npc.m_21573_().stop();
    }

    @Override
    public void tick() {
        this.updateTick++;
        if (this.updateTick >= 10) {
            this.updateTick = 0;
            this.npc.m_21563_().setLookAt(this.owner, 10.0F, (float) this.npc.m_8132_());
            double distance = this.npc.m_20280_(this.owner);
            double speed = 1.0 + distance / 150.0;
            if (speed > 3.0) {
                speed = 3.0;
            }
            if (this.owner.m_20142_()) {
                speed += 0.5;
            }
            if (!this.npc.m_21573_().moveTo(this.owner, speed) && !this.npc.isInRange(this.owner, 16.0)) {
                this.npc.tpTo(this.owner);
            }
        }
    }
}