package noppes.npcs.ai.selector;

import com.google.common.base.Predicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.constants.EnumCompanionJobs;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobGuard;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.companion.CompanionGuard;

public class NPCAttackSelector implements Predicate<LivingEntity> {

    private EntityNPCInterface npc;

    public NPCAttackSelector(EntityNPCInterface npc) {
        this.npc = npc;
    }

    public boolean isEntityApplicable(LivingEntity entity) {
        if (!entity.isAlive() || entity == this.npc || !this.npc.isInRange(entity, (double) this.npc.stats.aggroRange) || entity.getHealth() < 1.0F) {
            return false;
        } else if (this.npc.ais.directLOS && !this.npc.m_21574_().hasLineOfSight(entity)) {
            return false;
        } else {
            if (!this.npc.isFollower() && this.npc.ais.shouldReturnHome()) {
                int allowedDistance = this.npc.stats.aggroRange * 2;
                if (this.npc.ais.getMovingType() == 1) {
                    allowedDistance += this.npc.ais.walkingRange;
                }
                double distance = entity.m_20275_((double) this.npc.getStartXPos(), this.npc.getStartYPos(), (double) this.npc.getStartZPos());
                if (this.npc.ais.getMovingType() == 2) {
                    int[] arr = this.npc.ais.getCurrentMovingPath();
                    distance = entity.m_20275_((double) arr[0], (double) arr[1], (double) arr[2]);
                }
                if (distance > (double) (allowedDistance * allowedDistance)) {
                    return false;
                }
            }
            if (this.npc.job.getType() == 3 && ((JobGuard) this.npc.job).isEntityApplicable(entity)) {
                return true;
            } else {
                if (this.npc.role.getType() == 6) {
                    RoleCompanion role = (RoleCompanion) this.npc.role;
                    if (role.companionJobInterface.getType() == EnumCompanionJobs.GUARD && ((CompanionGuard) role.companionJobInterface).isEntityApplicable(entity)) {
                        return true;
                    }
                }
                if (entity instanceof ServerPlayer player) {
                    return this.npc.faction.isAggressiveToPlayer(player) && !player.m_150110_().invulnerable;
                } else {
                    if (entity instanceof EntityNPCInterface) {
                        if (((EntityNPCInterface) entity).isKilled()) {
                            return false;
                        }
                        if (this.npc.advanced.attackOtherFactions) {
                            return this.npc.faction.isAggressiveToNpc((EntityNPCInterface) entity);
                        }
                    }
                    return false;
                }
            }
        }
    }

    public boolean apply(LivingEntity ob) {
        return this.isEntityApplicable(ob);
    }
}