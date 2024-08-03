package noppes.npcs.ai;

import java.util.EnumSet;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAvoidTarget extends Goal {

    private EntityNPCInterface npc;

    private Entity closestLivingEntity;

    private float distanceFromEntity;

    private float health;

    private Path entityPathEntity;

    private PathNavigation entityPathNavigate;

    private Class targetEntityClass;

    public EntityAIAvoidTarget(EntityNPCInterface par1EntityNPC) {
        this.npc = par1EntityNPC;
        this.distanceFromEntity = (float) this.npc.stats.aggroRange;
        this.health = this.npc.m_21223_();
        this.entityPathNavigate = par1EntityNPC.m_21573_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.npc.m_5448_();
        if (target == null) {
            return false;
        } else {
            this.targetEntityClass = target.getClass();
            if (this.targetEntityClass == Player.class) {
                this.closestLivingEntity = this.npc.m_9236_().m_45930_(this.npc, (double) this.distanceFromEntity);
                if (this.closestLivingEntity == null) {
                    return false;
                }
            } else {
                List var1 = this.npc.m_9236_().m_45976_(this.targetEntityClass, this.npc.m_20191_().inflate((double) this.distanceFromEntity, 3.0, (double) this.distanceFromEntity));
                if (var1.isEmpty()) {
                    return false;
                }
                this.closestLivingEntity = (Entity) var1.get(0);
            }
            if (!this.npc.m_21574_().hasLineOfSight(this.closestLivingEntity) && this.npc.ais.directLOS) {
                return false;
            } else {
                Vec3 var2 = DefaultRandomPos.getPosAway(this.npc, 16, 7, new Vec3(this.closestLivingEntity.getX(), this.closestLivingEntity.getY(), this.closestLivingEntity.getZ()));
                if (var2 != null && var2 != Vec3.ZERO) {
                    if (this.closestLivingEntity.distanceToSqr(var2.x, var2.y, var2.z) < this.closestLivingEntity.distanceToSqr(this.npc)) {
                        return false;
                    } else {
                        this.entityPathEntity = this.entityPathNavigate.createPath(var2.x, var2.y, var2.z, 0);
                        return this.entityPathEntity != null;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.entityPathNavigate.isDone();
    }

    @Override
    public void start() {
        this.entityPathNavigate.moveTo(this.entityPathEntity, 1.0);
    }

    @Override
    public void stop() {
        this.closestLivingEntity = null;
        this.npc.setTarget(null);
    }

    @Override
    public void tick() {
        if (this.npc.isInRange(this.closestLivingEntity, 7.0)) {
            this.npc.m_21573_().setSpeedModifier(1.2);
        } else {
            this.npc.m_21573_().setSpeedModifier(1.0);
        }
    }
}