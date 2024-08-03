package noppes.npcs.ai;

import java.util.EnumSet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIRangedAttack extends Goal {

    private final EntityNPCInterface npc;

    private LivingEntity attackTarget;

    private int rangedAttackTime = 0;

    private int moveTries = 0;

    private int burstCount = 0;

    private int attackTick = 0;

    private boolean hasFired = false;

    public EntityAIRangedAttack(RangedAttackMob par1RangedAttackMob) {
        if (!(par1RangedAttackMob instanceof LivingEntity)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        } else {
            this.npc = (EntityNPCInterface) par1RangedAttackMob;
            this.rangedAttackTime = this.npc.stats.ranged.getDelayMin() / 2;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }
    }

    @Override
    public boolean canUse() {
        this.attackTarget = this.npc.m_5448_();
        return this.attackTarget != null && this.attackTarget.isAlive() && this.npc.isInRange(this.attackTarget, (double) this.npc.stats.aggroRange) && this.npc.inventory.getProjectile() != null ? this.npc.stats.ranged.getMeleeRange() < 1 || !this.npc.isInRange(this.attackTarget, (double) this.npc.stats.ranged.getMeleeRange()) : false;
    }

    @Override
    public void stop() {
        this.attackTarget = null;
        this.npc.setTarget(null);
        this.npc.m_21573_().stop();
        this.moveTries = 0;
        this.hasFired = false;
        this.rangedAttackTime = this.npc.stats.ranged.getDelayMin() / 2;
    }

    @Override
    public void tick() {
        this.npc.m_21563_().setLookAt(this.attackTarget, 30.0F, 30.0F);
        double var1 = this.npc.m_20275_(this.attackTarget.m_20185_(), this.attackTarget.m_20191_().minY, this.attackTarget.m_20189_());
        float range = (float) (this.npc.stats.ranged.getRange() * this.npc.stats.ranged.getRange());
        if (this.npc.ais.directLOS) {
            if (this.npc.m_21574_().hasLineOfSight(this.attackTarget)) {
                this.moveTries++;
            } else {
                this.moveTries = 0;
            }
            int v = 15;
            if (var1 <= (double) range && this.moveTries >= v) {
                this.npc.m_21573_().stop();
            } else {
                this.npc.m_21573_().moveTo(this.attackTarget, 1.0);
            }
        }
        if (this.rangedAttackTime-- <= 0 && var1 <= (double) range && (this.npc.m_21574_().hasLineOfSight(this.attackTarget) || this.npc.stats.ranged.getFireType() == 2)) {
            if (this.burstCount++ <= this.npc.stats.ranged.getBurst()) {
                this.rangedAttackTime = this.npc.stats.ranged.getBurstDelay();
            } else {
                this.burstCount = 0;
                this.hasFired = true;
                this.rangedAttackTime = this.npc.stats.ranged.getDelayRNG();
            }
            if (this.burstCount > 1) {
                boolean indirect = false;
                switch(this.npc.stats.ranged.getFireType()) {
                    case 1:
                        indirect = var1 > (double) range / 2.0;
                        break;
                    case 2:
                        indirect = !this.npc.m_21574_().hasLineOfSight(this.attackTarget);
                }
                this.npc.performRangedAttack(this.attackTarget, indirect ? 1.0F : 0.0F);
                if (this.npc.currentAnimation != 6) {
                    this.npc.m_6674_(InteractionHand.MAIN_HAND);
                }
            }
        }
    }

    public boolean hasFired() {
        return this.hasFired;
    }
}