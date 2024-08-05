package noppes.npcs.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWatchClosest extends Goal {

    private EntityNPCInterface npc;

    protected Entity closestEntity;

    private float maxDistance;

    private int lookTime;

    private float change;

    private Class<? extends LivingEntity> watchedClass;

    protected final TargetingConditions predicate;

    public EntityAIWatchClosest(EntityNPCInterface par1EntityLiving, Class<? extends LivingEntity> limbSwingAmountClass, float par3) {
        this.npc = par1EntityLiving;
        this.watchedClass = limbSwingAmountClass;
        this.maxDistance = par3;
        this.change = 0.002F;
        this.m_7021_(EnumSet.of(Goal.Flag.LOOK));
        this.predicate = TargetingConditions.forNonCombat().range((double) par3);
    }

    @Override
    public boolean canUse() {
        if (!(this.npc.m_217043_().nextFloat() >= this.change) && !this.npc.isInteracting()) {
            if (this.npc.m_5448_() != null) {
                this.closestEntity = this.npc.m_5448_();
            }
            if (this.watchedClass == Player.class) {
                this.closestEntity = this.npc.m_9236_().m_45930_(this.npc, (double) this.maxDistance);
            } else {
                this.closestEntity = this.npc.m_9236_().m_45963_(this.watchedClass, this.predicate, this.npc, this.npc.m_20185_(), this.npc.m_20188_(), this.npc.m_20189_(), this.npc.m_20191_().inflate((double) this.maxDistance, 3.0, (double) this.maxDistance));
                if (this.closestEntity != null) {
                    return this.npc.canNpcSee(this.closestEntity);
                }
            }
            return this.closestEntity != null;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.npc.isInteracting() && !this.npc.isAttacking() && this.closestEntity.isAlive() && this.npc.isAlive()) {
            return !this.npc.isInRange(this.closestEntity, (double) this.maxDistance) ? false : this.lookTime > 0;
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.lookTime = 60 + this.npc.m_217043_().nextInt(60);
    }

    @Override
    public void stop() {
        this.closestEntity = null;
    }

    @Override
    public void tick() {
        this.npc.m_21563_().setLookAt(this.closestEntity.getX(), this.closestEntity.getY() + (double) this.closestEntity.getEyeHeight(), this.closestEntity.getZ(), 10.0F, (float) this.npc.m_8132_());
        this.lookTime--;
    }
}