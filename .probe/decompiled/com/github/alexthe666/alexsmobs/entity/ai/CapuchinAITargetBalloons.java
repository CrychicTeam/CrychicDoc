package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityCapuchinMonkey;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Team;

public class CapuchinAITargetBalloons extends Goal {

    private final EntityCapuchinMonkey monkey;

    protected final boolean shouldCheckSight;

    private final boolean nearbyOnly;

    private int targetSearchStatus;

    private int targetSearchDelay;

    private int targetUnseenTicks;

    protected Entity target;

    protected int unseenMemoryTicks = 60;

    protected final int targetChance;

    public static final Predicate<Entity> TARGET_BLOON = balloon -> balloon.getEncodeId() != null && (balloon.getType().is(AMTagRegistry.MONKEY_TARGET_WITH_DART) || balloon.getEncodeId().contains("balloom"));

    public CapuchinAITargetBalloons(EntityCapuchinMonkey mobIn, boolean checkSight) {
        this(mobIn, checkSight, false, 40);
    }

    public CapuchinAITargetBalloons(EntityCapuchinMonkey mobIn, boolean checkSight, boolean nearbyOnlyIn, int targetChance) {
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
        this.monkey = mobIn;
        this.shouldCheckSight = checkSight;
        this.nearbyOnly = nearbyOnlyIn;
        this.targetChance = targetChance;
    }

    @Override
    public boolean canUse() {
        if (this.targetChance > 0 && this.monkey.m_217043_().nextInt(this.targetChance) != 0) {
            return false;
        } else {
            this.findNearestTarget();
            return this.target != null;
        }
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.monkey.m_20191_().inflate(targetDistance, targetDistance, targetDistance);
    }

    protected void findNearestTarget() {
        Entity closest = null;
        for (Entity bloon : this.monkey.m_9236_().m_6443_(Entity.class, this.getTargetableArea(this.getTargetDistance()), TARGET_BLOON)) {
            if (closest == null || closest.distanceTo(this.monkey) > bloon.distanceTo(this.monkey)) {
                closest = bloon;
            }
        }
        this.target = closest;
    }

    @Override
    public boolean canContinueToUse() {
        Entity livingentity = this.monkey.getDartTarget();
        if (livingentity == null) {
            livingentity = this.target;
        }
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else {
            Team team = this.monkey.m_5647_();
            Team team1 = livingentity.getTeam();
            if (team != null && team1 == team) {
                return false;
            } else {
                double d0 = this.getTargetDistance();
                if (this.monkey.m_20280_(livingentity) > d0 * d0) {
                    return false;
                } else {
                    if (this.shouldCheckSight) {
                        if (this.monkey.m_21574_().hasLineOfSight(livingentity)) {
                            this.targetUnseenTicks = 0;
                        } else if (++this.targetUnseenTicks > this.unseenMemoryTicks) {
                            return false;
                        }
                    }
                    if (livingentity instanceof Player && ((Player) livingentity).getAbilities().invulnerable) {
                        return false;
                    } else {
                        this.monkey.setDartTarget(livingentity);
                        return true;
                    }
                }
            }
        }
    }

    protected double getTargetDistance() {
        return this.monkey.m_21133_(Attributes.FOLLOW_RANGE);
    }

    @Override
    public void start() {
        this.monkey.setDartTarget(this.target);
        this.targetSearchStatus = 0;
        this.targetSearchDelay = 0;
        this.targetUnseenTicks = 0;
    }

    @Override
    public void stop() {
        this.monkey.m_6710_((LivingEntity) null);
        this.monkey.setDartTarget(null);
        this.target = null;
    }

    protected boolean isSuitableTarget(@Nullable LivingEntity potentialTarget, TargetingConditions targetPredicate) {
        if (potentialTarget == null) {
            return false;
        } else if (!targetPredicate.test(this.monkey, potentialTarget)) {
            return false;
        } else if (!this.monkey.m_21444_(potentialTarget.m_20183_())) {
            return false;
        } else {
            if (this.nearbyOnly) {
                if (--this.targetSearchDelay <= 0) {
                    this.targetSearchStatus = 0;
                }
                if (this.targetSearchStatus == 0) {
                    this.targetSearchStatus = this.canEasilyReach(potentialTarget) ? 1 : 2;
                }
                if (this.targetSearchStatus == 2) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean canEasilyReach(LivingEntity target) {
        this.targetSearchDelay = 10 + this.monkey.m_217043_().nextInt(5);
        Path path = this.monkey.m_21573_().createPath(target, 0);
        if (path == null) {
            return false;
        } else {
            Node pathpoint = path.getEndNode();
            if (pathpoint == null) {
                return false;
            } else {
                int i = pathpoint.x - Mth.floor(target.m_20185_());
                int j = pathpoint.z - Mth.floor(target.m_20189_());
                return (double) (i * i + j * j) <= 2.25;
            }
        }
    }

    public CapuchinAITargetBalloons setUnseenMemoryTicks(int unseenMemoryTicksIn) {
        this.unseenMemoryTicks = unseenMemoryTicksIn;
        return this;
    }
}