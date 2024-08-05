package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.PackAnimal;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class UnderzealotSacrificeGoal extends Goal {

    private UnderzealotEntity entity;

    private int executionCooldown = 10;

    private int attemptToFollowTicks = 0;

    private BlockPos center;

    public UnderzealotSacrificeGoal(UnderzealotEntity entity) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        if (this.entity.sacrificeCooldown > 0) {
            return false;
        } else {
            if (this.entity.isPackFollower()) {
                UnderzealotEntity leader = (UnderzealotEntity) this.entity.getPackLeader();
                if (leader.isCarrying() && leader.getLastSacrificePos() != null && leader.m_20238_(Vec3.atCenterOf(leader.getLastSacrificePos())) < 2.5) {
                    this.center = leader.getLastSacrificePos();
                    return true;
                }
            } else if (this.entity.isCarrying()) {
                if (this.executionCooldown-- > 0) {
                    return false;
                }
                this.executionCooldown = 20 + this.entity.m_217043_().nextInt(100);
                BlockPos pos = null;
                if (this.entity.getLastSacrificePos() != null) {
                    if (this.isValidSacrificePos(this.entity.getLastSacrificePos())) {
                        this.executionCooldown = 10;
                        pos = this.entity.getLastSacrificePos();
                    } else {
                        this.entity.setLastSacrificePos(null);
                    }
                }
                if (pos == null) {
                    pos = this.findNearestSacrificePos();
                }
                if (pos != null) {
                    this.center = pos;
                    this.entity.setLastSacrificePos(this.center);
                    return true;
                }
            }
            return false;
        }
    }

    private BlockPos findNearestSacrificePos() {
        BlockPos.MutableBlockPos check = new BlockPos.MutableBlockPos();
        check.move(this.entity.m_20183_());
        check.move(0, -1, 0);
        if (this.isValidSacrificePos(check)) {
            return check.immutable();
        } else {
            for (int i = 0; i < 20; i++) {
                check.move(this.entity.m_20183_());
                check.move(this.entity.m_217043_().nextInt(20) - 10, 5, this.entity.m_217043_().nextInt(20) - 10);
                if (this.entity.m_9236_().isLoaded(check)) {
                    while (this.entity.m_9236_().m_46859_(check) && check.m_123342_() > this.entity.m_9236_().m_141937_()) {
                        check.move(0, -1, 0);
                    }
                    if (this.isValidSacrificePos(check) && this.canReach(check)) {
                        return check.immutable();
                    }
                }
            }
            return null;
        }
    }

    public boolean canReach(BlockPos target) {
        Path path = this.entity.m_21573_().createPath(target, 0);
        if (path == null) {
            return false;
        } else {
            Node node = path.getEndNode();
            if (node == null) {
                return false;
            } else {
                int i = node.x - target.m_123341_();
                int j = node.y - target.m_123342_();
                int k = node.z - target.m_123343_();
                return (double) (i * i + j * j + k * k) <= 3.0;
            }
        }
    }

    private boolean isValidSacrificePos(BlockPos pos) {
        if (this.entity.m_9236_().m_46859_(pos)) {
            return false;
        } else {
            BlockPos.MutableBlockPos aboveGround = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos below = new BlockPos.MutableBlockPos();
            int badSpots = 0;
            for (int i = -2; i <= -2; i++) {
                for (int j = -2; j <= -2; j++) {
                    aboveGround.set(pos.m_123341_() + i, pos.m_123342_() + 1, pos.m_123343_() + j);
                    below.set(pos.m_123341_() + i, pos.m_123342_(), pos.m_123343_() + j);
                    if (this.entity.m_9236_().m_46859_(below)) {
                        badSpots++;
                    }
                    if (badSpots > 5) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.center == null) {
            return false;
        } else if (this.entity.sacrificeCooldown > 0) {
            return false;
        } else if (!this.entity.isPackFollower()) {
            return this.entity.isCarrying();
        } else {
            UnderzealotEntity leader = (UnderzealotEntity) this.entity.getPackLeader();
            return leader != null && leader.isCarrying() && leader.m_20238_(Vec3.atCenterOf(this.center)) < 5.0;
        }
    }

    @Override
    public void tick() {
        if (!this.entity.isDiggingInProgress() && !this.entity.isBuried()) {
            if (this.center != null) {
                int worshippingTicks = this.entity.getWorshipTime();
                if (this.entity.isPackFollower()) {
                    UnderzealotEntity leader = (UnderzealotEntity) this.entity.getPackLeader();
                    float f = (float) this.getPackPosition() / ((float) this.entity.getPackSize() - 1.0F);
                    Vec3 offset = new Vec3(2.0, 0.0, 0.0).yRot(f * (float) (Math.PI * 2));
                    Vec3 at = this.groundOf(Vec3.atCenterOf(this.center).add(offset));
                    this.entity.m_21573_().moveTo(at.x, at.y, at.z, 1.0);
                    if (this.entity.isPraying()) {
                        this.attemptToFollowTicks = 0;
                    } else if ((this.entity.m_21573_().isStuck() || this.attemptToFollowTicks > 60) && this.entity.m_20238_(at) > 8.0) {
                        this.entity.setBuried(true);
                        this.entity.reemergeAt(BlockPos.containing(at).above(), 20 + this.entity.m_217043_().nextInt(20));
                    }
                    if (leader != null && leader.sacrificeCooldown > 0) {
                        this.entity.sacrificeCooldown = leader.sacrificeCooldown;
                    }
                    if (leader != null && this.entity.m_20238_(at) < 4.0) {
                        this.entity.setPraying(true);
                        Vec3 slightOffset = at.subtract(this.entity.m_20182_());
                        if (slightOffset.length() > 1.0) {
                            slightOffset = slightOffset.normalize();
                        }
                        this.entity.m_20256_(this.entity.m_20184_().add(slightOffset.scale(0.04F)));
                        this.entity.m_7618_(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(this.center));
                        this.entity.setParticlePos(leader.getLastSacrificePos().above(5));
                        this.entity.m_9236_().broadcastEntityEvent(this.entity, (byte) 77);
                        if (worshippingTicks % 10 == 0) {
                            this.entity.m_9236_().broadcastEntityEvent(this.entity, (byte) 61);
                        }
                        this.entity.setWorshipTime(worshippingTicks + 1);
                    } else {
                        this.entity.setWorshipTime(0);
                        this.attemptToFollowTicks++;
                    }
                } else {
                    this.entity.setParticlePos(this.center.above(5));
                    Vec3 atx = Vec3.atCenterOf(this.center);
                    if (this.entity.m_20238_(atx) < 4.0) {
                        Vec3 slightOffsetx = atx.subtract(this.entity.m_20182_());
                        if (slightOffsetx.length() > 1.0) {
                            slightOffsetx = slightOffsetx.normalize();
                        }
                        this.entity.setWorshipTime(worshippingTicks + 1);
                        this.entity.m_20256_(this.entity.m_20184_().add(slightOffsetx.scale(0.04F)));
                        if (worshippingTicks > 200 && this.entity.cloudCooldown <= 0 && this.entity.isSurroundedByPrayers() && this.entity.m_146895_() instanceof UnderzealotSacrifice underzealotSacrifice) {
                            underzealotSacrifice.triggerSacrificeIn(300);
                            this.entity.cloudCooldown = 400;
                            this.entity.m_9236_().broadcastEntityEvent(this.entity, (byte) 62);
                        }
                    } else {
                        this.entity.setWorshipTime(0);
                    }
                    this.entity.m_21573_().moveTo((double) this.center.m_123341_(), (double) this.center.m_123342_(), (double) this.center.m_123343_(), 1.0);
                }
            }
        } else {
            this.entity.setPraying(false);
        }
    }

    private Vec3 groundOf(Vec3 in) {
        BlockPos origin = BlockPos.containing(in);
        BlockPos.MutableBlockPos blockPos = origin.mutable();
        while (!this.entity.m_9236_().m_46859_(blockPos) && blockPos.m_123342_() < this.entity.m_9236_().m_151558_()) {
            blockPos.move(0, 1, 0);
        }
        while (this.entity.m_9236_().m_46859_(blockPos.m_7495_()) && blockPos.m_123342_() > this.entity.m_9236_().m_141937_()) {
            blockPos.move(0, -1, 0);
        }
        return new Vec3(in.x, (double) blockPos.m_123342_(), in.z);
    }

    private int getPackPosition() {
        PackAnimal leader = this.entity.getPackLeader();
        int i;
        for (i = 1; leader.getAfterPackMember() != null; i++) {
            if (leader.getAfterPackMember() == this.entity) {
                return i;
            }
            leader = leader.getAfterPackMember();
        }
        return i;
    }

    @Override
    public void stop() {
        this.attemptToFollowTicks = 0;
        this.entity.setWorshipTime(0);
        if (this.entity.sacrificeCooldown == 0) {
            this.entity.sacrificeCooldown = 100;
        }
        this.entity.m_21573_().stop();
        this.entity.m_20153_();
        this.entity.setPraying(false);
        this.entity.setParticlePos(null);
    }
}