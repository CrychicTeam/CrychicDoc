package net.minecraft.world.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

public abstract class DoorInteractGoal extends Goal {

    protected Mob mob;

    protected BlockPos doorPos = BlockPos.ZERO;

    protected boolean hasDoor;

    private boolean passed;

    private float doorOpenDirX;

    private float doorOpenDirZ;

    public DoorInteractGoal(Mob mob0) {
        this.mob = mob0;
        if (!GoalUtils.hasGroundPathNavigation(mob0)) {
            throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
        }
    }

    protected boolean isOpen() {
        if (!this.hasDoor) {
            return false;
        } else {
            BlockState $$0 = this.mob.m_9236_().getBlockState(this.doorPos);
            if (!($$0.m_60734_() instanceof DoorBlock)) {
                this.hasDoor = false;
                return false;
            } else {
                return (Boolean) $$0.m_61143_(DoorBlock.OPEN);
            }
        }
    }

    protected void setOpen(boolean boolean0) {
        if (this.hasDoor) {
            BlockState $$1 = this.mob.m_9236_().getBlockState(this.doorPos);
            if ($$1.m_60734_() instanceof DoorBlock) {
                ((DoorBlock) $$1.m_60734_()).setOpen(this.mob, this.mob.m_9236_(), $$1, this.doorPos, boolean0);
            }
        }
    }

    @Override
    public boolean canUse() {
        if (!GoalUtils.hasGroundPathNavigation(this.mob)) {
            return false;
        } else if (!this.mob.f_19862_) {
            return false;
        } else {
            GroundPathNavigation $$0 = (GroundPathNavigation) this.mob.getNavigation();
            Path $$1 = $$0.m_26570_();
            if ($$1 != null && !$$1.isDone() && $$0.canOpenDoors()) {
                for (int $$2 = 0; $$2 < Math.min($$1.getNextNodeIndex() + 2, $$1.getNodeCount()); $$2++) {
                    Node $$3 = $$1.getNode($$2);
                    this.doorPos = new BlockPos($$3.x, $$3.y + 1, $$3.z);
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
        float $$0 = (float) ((double) this.doorPos.m_123341_() + 0.5 - this.mob.m_20185_());
        float $$1 = (float) ((double) this.doorPos.m_123343_() + 0.5 - this.mob.m_20189_());
        float $$2 = this.doorOpenDirX * $$0 + this.doorOpenDirZ * $$1;
        if ($$2 < 0.0F) {
            this.passed = true;
        }
    }
}