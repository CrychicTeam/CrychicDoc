package org.violetmoon.quark.content.mobs.ai;

import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FavorBlockGoal extends Goal {

    private final PathfinderMob creature;

    private final double movementSpeed;

    private final Predicate<BlockState> targetBlock;

    protected int runDelay;

    private int timeoutCounter;

    private int maxStayTicks;

    protected BlockPos destinationBlock = BlockPos.ZERO;

    public FavorBlockGoal(PathfinderMob creature, double speed, Predicate<BlockState> predicate) {
        this.creature = creature;
        this.movementSpeed = speed;
        this.targetBlock = predicate;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    public FavorBlockGoal(PathfinderMob creature, double speed, TagKey<Block> tag) {
        this(creature, speed, state -> state.m_204336_(tag));
    }

    public FavorBlockGoal(PathfinderMob creature, double speed, Block block) {
        this(creature, speed, state -> state.m_60734_() == block);
    }

    @Override
    public boolean canUse() {
        if (this.runDelay > 0) {
            this.runDelay--;
            return false;
        } else {
            this.runDelay = 200 + this.creature.m_217043_().nextInt(200);
            return this.searchForDestination();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.timeoutCounter >= -this.maxStayTicks && this.timeoutCounter <= 1200 && this.targetBlock.test(this.creature.m_9236_().getBlockState(this.destinationBlock));
    }

    @Override
    public void start() {
        this.creature.m_21573_().moveTo((double) this.destinationBlock.m_123341_() + 0.5, (double) (this.destinationBlock.m_123342_() + 1), (double) this.destinationBlock.m_123343_() + 0.5, this.movementSpeed);
        this.timeoutCounter = 0;
        this.maxStayTicks = this.creature.m_217043_().nextInt(this.creature.m_217043_().nextInt(1200) + 1200) + 1200;
    }

    @Override
    public void tick() {
        if (this.creature.m_20238_(new Vec3((double) this.destinationBlock.m_123341_(), (double) this.destinationBlock.m_123342_(), (double) this.destinationBlock.m_123343_()).add(0.5, 1.5, 0.5)) > 1.0) {
            this.timeoutCounter++;
            if (this.timeoutCounter % 40 == 0) {
                this.creature.m_21573_().moveTo((double) this.destinationBlock.m_123341_() + 0.5, (double) (this.destinationBlock.m_123342_() + 1), (double) this.destinationBlock.m_123343_() + 0.5, this.movementSpeed);
            }
        } else {
            this.timeoutCounter--;
        }
    }

    private boolean searchForDestination() {
        double followRange = this.creature.m_21051_(Attributes.FOLLOW_RANGE).getValue();
        Vec3 cpos = this.creature.m_20182_();
        double xBase = cpos.x;
        double yBase = cpos.y;
        double zBase = cpos.z;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int yShift = 0; yShift <= 1; yShift = yShift > 0 ? -yShift : 1 - yShift) {
            for (int seekDist = 0; (double) seekDist < followRange; seekDist++) {
                for (int xShift = 0; xShift <= seekDist; xShift = xShift > 0 ? -xShift : 1 - xShift) {
                    for (int zShift = xShift < seekDist && xShift > -seekDist ? seekDist : 0; zShift <= seekDist; zShift = zShift > 0 ? -zShift : 1 - zShift) {
                        pos.set(xBase + (double) xShift, yBase + (double) yShift - 1.0, zBase + (double) zShift);
                        if (this.creature.m_21444_(pos) && this.targetBlock.test(this.creature.m_9236_().getBlockState(pos))) {
                            this.destinationBlock = pos;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}