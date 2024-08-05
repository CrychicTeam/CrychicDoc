package com.mna.entities.constructs.ai.base;

import com.mna.entities.constructs.animated.Construct;
import com.mna.entities.constructs.movement.ConstructPathNavigator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

public class ConstructDoorInteractGoal extends Goal {

    protected Construct mob;

    protected BlockPos doorPos = BlockPos.ZERO;

    protected boolean hasDoor;

    private boolean passed;

    private float doorOpenDirX;

    private float doorOpenDirZ;

    public ConstructDoorInteractGoal(Construct construct) {
        this.mob = construct;
    }

    protected boolean isOpen() {
        if (!this.hasDoor) {
            return false;
        } else {
            BlockState blockstate = this.mob.m_9236_().getBlockState(this.doorPos);
            if (!(blockstate.m_60734_() instanceof DoorBlock)) {
                this.hasDoor = false;
                return false;
            } else {
                return (Boolean) blockstate.m_61143_(DoorBlock.OPEN);
            }
        }
    }

    protected void setOpen(boolean boolean0) {
        if (this.hasDoor) {
            BlockState blockstate = this.mob.m_9236_().getBlockState(this.doorPos);
            if (blockstate.m_60734_() instanceof DoorBlock) {
                ((DoorBlock) blockstate.m_60734_()).setOpen(this.mob, this.mob.m_9236_(), blockstate, this.doorPos, boolean0);
            }
        }
    }

    @Override
    public boolean canUse() {
        if (!this.mob.f_19862_) {
            return false;
        } else {
            ConstructPathNavigator groundpathnavigation = (ConstructPathNavigator) this.mob.m_21573_();
            Path path = groundpathnavigation.m_26570_();
            if (path != null && !path.isDone()) {
                for (int i = 0; i < Math.min(path.getNextNodeIndex() + 2, path.getNodeCount()); i++) {
                    Node node = path.getNode(i);
                    this.doorPos = new BlockPos(node.x, node.y + 1, node.z);
                    if (!(this.mob.m_20275_((double) this.doorPos.m_123341_(), this.mob.m_20186_(), (double) this.doorPos.m_123343_()) > 2.25)) {
                        this.hasDoor = DoorBlock.isWoodenDoor(this.mob.m_9236_(), this.doorPos);
                        if (this.hasDoor) {
                            return true;
                        }
                    }
                }
                this.doorPos = this.mob.m_20183_().above();
                this.hasDoor = DoorBlock.isWoodenDoor(this.mob.m_9236_(), this.doorPos);
                return this.hasDoor;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.passed;
    }

    @Override
    public void start() {
        this.passed = false;
        this.doorOpenDirX = (float) ((double) this.doorPos.m_123341_() + 0.5 - this.mob.m_20185_());
        this.doorOpenDirZ = (float) ((double) this.doorPos.m_123343_() + 0.5 - this.mob.m_20189_());
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        float f = (float) ((double) this.doorPos.m_123341_() + 0.5 - this.mob.m_20185_());
        float f1 = (float) ((double) this.doorPos.m_123343_() + 0.5 - this.mob.m_20189_());
        float f2 = this.doorOpenDirX * f + this.doorOpenDirZ * f1;
        if (f2 < 0.0F) {
            this.passed = true;
        }
    }
}