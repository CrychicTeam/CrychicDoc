package noppes.npcs.ai;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAttackTarget extends Goal {

    private Level level;

    private EntityNPCInterface npc;

    private LivingEntity entityTarget;

    private int attackTick;

    private Path entityPathEntity;

    private int field_75445_i;

    private BlockPos startPos = BlockPos.ZERO;

    public EntityAIAttackTarget(EntityNPCInterface par1EntityLiving) {
        this.attackTick = 0;
        this.npc = par1EntityLiving;
        this.level = par1EntityLiving.m_9236_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity entitylivingbase = this.npc.m_5448_();
        if (entitylivingbase != null && entitylivingbase.isAlive()) {
            int melee = this.npc.stats.ranged.getMeleeRange();
            if (this.npc.inventory.getProjectile() == null || melee > 0 && this.npc.isInRange(entitylivingbase, (double) melee)) {
                this.entityTarget = entitylivingbase;
                this.entityPathEntity = this.npc.m_21573_().createPath(entitylivingbase, 0);
                return this.entityPathEntity != null;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        this.entityTarget = this.npc.m_5448_();
        if (this.entityTarget == null) {
            this.entityTarget = this.npc.m_21188_();
        }
        if (this.entityTarget == null || !this.entityTarget.isAlive()) {
            return false;
        } else if (!this.npc.isInRange(this.entityTarget, (double) this.npc.stats.aggroRange)) {
            return false;
        } else {
            int melee = this.npc.stats.ranged.getMeleeRange();
            return melee > 0 && !this.npc.isInRange(this.entityTarget, (double) melee) ? false : this.isWithinRestriction(this.entityTarget.m_20183_());
        }
    }

    public boolean isWithinRestriction(BlockPos pos) {
        int range = Math.max(this.npc.stats.aggroRange * 2, 64);
        return this.startPos.m_123331_(pos) < (double) (range * range);
    }

    @Override
    public void start() {
        this.startPos = this.npc.m_20183_();
        this.npc.m_21573_().moveTo(this.entityPathEntity, 1.3);
        this.field_75445_i = 0;
    }

    @Override
    public void stop() {
        if (this.entityTarget != null && !this.isWithinRestriction(this.entityTarget.m_20183_())) {
            this.npc.reset();
        }
        this.entityPathEntity = null;
        this.entityTarget = null;
        this.npc.m_21573_().stop();
    }

    @Override
    public void tick() {
        this.npc.m_21563_().setLookAt(this.entityTarget, 30.0F, 30.0F);
        if (--this.field_75445_i <= 0) {
            this.field_75445_i = 4 + this.npc.m_217043_().nextInt(7);
            this.npc.m_21573_().moveTo(this.entityTarget, 1.3F);
        }
        this.attackTick = Math.max(this.attackTick - 1, 0);
        double y = this.entityTarget.m_20186_();
        if (this.entityTarget.m_20191_() != null) {
            y = this.entityTarget.m_20191_().minY;
        }
        double distance = this.npc.m_20275_(this.entityTarget.m_20185_(), y, this.entityTarget.m_20189_());
        double range = (double) ((float) (this.npc.stats.melee.getRange() * this.npc.stats.melee.getRange()) + this.entityTarget.m_20205_());
        double minRange = (double) (this.npc.m_20205_() * 2.0F * this.npc.m_20205_() * 2.0F + this.entityTarget.m_20205_());
        if (minRange > range) {
            range = minRange;
        }
        if (distance <= range && (this.npc.canNpcSee(this.entityTarget) || distance < minRange) && this.attackTick <= 0) {
            this.attackTick = this.npc.stats.melee.getDelay();
            this.npc.m_6674_(InteractionHand.MAIN_HAND);
            this.npc.doHurtTarget(this.entityTarget);
        }
    }
}