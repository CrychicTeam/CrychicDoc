package noppes.npcs.ai;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.CustomNpcs;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIReturn extends Goal {

    public static final int MaxTotalTicks = 600;

    private final EntityNPCInterface npc;

    private int stuckTicks = 0;

    private int totalTicks = 0;

    private double endPosX;

    private double endPosY;

    private double endPosZ;

    private boolean wasAttacked = false;

    private double[] preAttackPos;

    private int stuckCount = 0;

    public EntityAIReturn(EntityNPCInterface npc) {
        this.npc = npc;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.npc.hasOwner() && !this.npc.m_20159_() && this.npc.ais.shouldReturnHome() && !this.npc.isKilled() && this.npc.m_21573_().isDone() && !this.npc.isInteracting()) {
            if (this.npc.ais.findShelter == 0 && (!this.npc.m_9236_().isDay() || this.npc.m_9236_().isRaining()) && !this.npc.m_9236_().dimensionType().hasSkyLight()) {
                BlockPos pos = new BlockPos((int) this.npc.getStartXPos(), (int) this.npc.getStartYPos(), (int) this.npc.getStartZPos());
                if (this.npc.m_9236_().m_45527_(pos) || this.npc.m_9236_().m_7146_(pos) <= 8) {
                    return false;
                }
            } else if (this.npc.ais.findShelter == 1 && this.npc.m_9236_().isDay()) {
                BlockPos pos = new BlockPos((int) this.npc.getStartXPos(), (int) this.npc.getStartYPos(), (int) this.npc.getStartZPos());
                if (this.npc.m_9236_().m_45527_(pos)) {
                    return false;
                }
            }
            if (this.npc.isAttacking()) {
                if (!this.wasAttacked) {
                    this.wasAttacked = true;
                    this.preAttackPos = new double[] { this.npc.m_20185_(), this.npc.m_20186_(), this.npc.m_20189_() };
                }
                return false;
            } else if (!this.npc.isAttacking() && this.wasAttacked) {
                return true;
            } else if (this.npc.ais.getMovingType() == 2 && this.npc.ais.distanceToSqrToPathPoint() < (double) (CustomNpcs.NpcNavRange * CustomNpcs.NpcNavRange)) {
                return false;
            } else if (this.npc.ais.getMovingType() == 1) {
                double x = this.npc.m_20185_() - (double) this.npc.getStartXPos();
                double z = this.npc.m_20189_() - (double) this.npc.getStartZPos();
                return !this.npc.isInRange((double) this.npc.getStartXPos(), -6666.0, (double) this.npc.getStartZPos(), (double) this.npc.ais.walkingRange);
            } else {
                return this.npc.ais.getMovingType() == 0 ? !this.npc.isVeryNearAssignedPlace() : false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.npc.isFollower() || this.npc.isKilled() || this.npc.isAttacking() || this.npc.isVeryNearAssignedPlace() || this.npc.isInteracting() || this.npc.m_20159_()) {
            return false;
        } else {
            return this.npc.m_21573_().isDone() && this.wasAttacked && !this.isTooFar() ? false : this.totalTicks <= 600;
        }
    }

    @Override
    public void tick() {
        this.totalTicks++;
        if (this.totalTicks > 600) {
            this.npc.m_6034_(this.endPosX, this.endPosY, this.endPosZ);
            this.npc.m_21573_().stop();
        } else {
            if (this.stuckTicks > 0) {
                this.stuckTicks--;
            } else if (this.npc.m_21573_().isDone()) {
                this.stuckCount++;
                this.stuckTicks = 10;
                if ((this.totalTicks <= 30 || !this.wasAttacked || !this.isTooFar()) && this.stuckCount <= 5) {
                    this.navigate(this.stuckCount % 2 == 1);
                } else {
                    this.npc.m_6034_(this.endPosX, this.endPosY, this.endPosZ);
                    this.npc.m_21573_().stop();
                }
            } else {
                this.stuckCount = 0;
            }
        }
    }

    private boolean isTooFar() {
        int allowedDistance = this.npc.stats.aggroRange * 2;
        if (this.npc.ais.getMovingType() == 1) {
            allowedDistance += this.npc.ais.walkingRange;
        }
        double x = this.npc.m_20185_() - this.endPosX;
        double z = this.npc.m_20189_() - this.endPosZ;
        return x * x + z * z > (double) (allowedDistance * allowedDistance);
    }

    @Override
    public void start() {
        this.stuckTicks = 0;
        this.totalTicks = 0;
        this.stuckCount = 0;
        this.navigate(false);
    }

    private void navigate(boolean towards) {
        if (!this.wasAttacked) {
            this.endPosX = (double) this.npc.getStartXPos();
            this.endPosY = this.npc.getStartYPos();
            this.endPosZ = (double) this.npc.getStartZPos();
        } else {
            this.endPosX = this.preAttackPos[0];
            this.endPosY = this.preAttackPos[1];
            this.endPosZ = this.preAttackPos[2];
        }
        double posX = this.endPosX;
        double posY = this.endPosY;
        double posZ = this.endPosZ;
        double range = Math.sqrt(this.npc.m_20275_(posX, posY, posZ));
        if (range > (double) CustomNpcs.NpcNavRange || towards) {
            int distance = (int) range;
            if (distance > CustomNpcs.NpcNavRange) {
                distance = CustomNpcs.NpcNavRange / 2;
            } else {
                distance /= 2;
            }
            if (distance > 2) {
                Vec3 start = new Vec3(posX, posY, posZ);
                Vec3 pos = DefaultRandomPos.getPosTowards(this.npc, distance / 2, distance / 2 > 7 ? 7 : distance / 2, start, Math.PI / 2);
                if (pos != null) {
                    posX = pos.x;
                    posY = pos.y;
                    posZ = pos.z;
                }
            }
        }
        this.npc.m_21573_().stop();
        this.npc.m_21573_().moveTo(posX, posY, posZ, 1.0);
    }

    @Override
    public void stop() {
        this.wasAttacked = false;
        this.npc.m_21573_().stop();
    }
}