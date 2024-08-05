package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityMantisShrimp;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;

public class MantisShrimpAIBreakBlocks extends Goal {

    private final EntityMantisShrimp mantisShrimp;

    private int idleAtFlowerTime = 0;

    private int timeoutCounter = 0;

    private int searchCooldown = 0;

    private boolean isAboveDestinationBear;

    private BlockPos destinationBlock;

    private final MantisShrimpAIBreakBlocks.BlockSorter targetSorter;

    public MantisShrimpAIBreakBlocks(EntityMantisShrimp mantisShrimp) {
        this.mantisShrimp = mantisShrimp;
        this.targetSorter = new MantisShrimpAIBreakBlocks.BlockSorter(mantisShrimp);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public boolean canUse() {
        if (!this.mantisShrimp.m_6162_() && (this.mantisShrimp.m_5448_() == null || !this.mantisShrimp.m_5448_().isAlive()) && this.mantisShrimp.getCommand() == 3 && !this.mantisShrimp.m_21205_().isEmpty()) {
            if (this.searchCooldown <= 0) {
                this.resetTarget();
                this.searchCooldown = 100 + this.mantisShrimp.m_217043_().nextInt(200);
                return this.destinationBlock != null;
            }
            this.searchCooldown--;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.destinationBlock != null && this.timeoutCounter < 1200 && (this.mantisShrimp.m_5448_() == null || !this.mantisShrimp.m_5448_().isAlive()) && this.mantisShrimp.getCommand() == 3 && !this.mantisShrimp.m_21205_().isEmpty();
    }

    @Override
    public void stop() {
        this.searchCooldown = 50;
        this.timeoutCounter = 0;
        this.destinationBlock = null;
    }

    public double getTargetDistanceSq() {
        return 2.3;
    }

    @Override
    public void tick() {
        BlockPos blockpos = this.destinationBlock;
        float yDist = (float) Math.abs((double) blockpos.m_123342_() - this.mantisShrimp.m_20186_() - (double) (this.mantisShrimp.m_20206_() / 2.0F));
        this.mantisShrimp.m_21573_().moveTo((double) ((float) blockpos.m_123341_()) + 0.5, (double) blockpos.m_123342_() + 0.5, (double) ((float) blockpos.m_123343_()) + 0.5, 1.0);
        if (this.isWithinXZDist(blockpos, this.mantisShrimp.m_20182_(), this.getTargetDistanceSq()) && !(yDist > 2.0F)) {
            this.isAboveDestinationBear = true;
            this.timeoutCounter--;
        } else {
            this.isAboveDestinationBear = false;
            this.timeoutCounter++;
        }
        if (this.timeoutCounter > 2400) {
            this.stop();
        }
        if (this.getIsAboveDestination()) {
            this.mantisShrimp.m_7618_(EntityAnchorArgument.Anchor.EYES, new Vec3((double) this.destinationBlock.m_123341_() + 0.5, (double) this.destinationBlock.m_123342_(), (double) this.destinationBlock.m_123343_() + 0.5));
            if (this.idleAtFlowerTime >= 2) {
                this.idleAtFlowerTime = 0;
                this.breakBlock();
                this.stop();
            } else {
                this.mantisShrimp.punch();
                this.idleAtFlowerTime++;
            }
        }
    }

    private void resetTarget() {
        List<BlockPos> allBlocks = new ArrayList();
        int radius = 16;
        for (BlockPos pos : (List) BlockPos.betweenClosedStream(this.mantisShrimp.m_20183_().offset(-radius, -radius, -radius), this.mantisShrimp.m_20183_().offset(radius, radius, radius)).map(BlockPos::m_7949_).collect(Collectors.toList())) {
            if (!this.mantisShrimp.m_9236_().m_46859_(pos) && this.shouldMoveTo(this.mantisShrimp.m_9236_(), pos) && (!this.mantisShrimp.m_20069_() || this.isBlockTouchingWater(pos))) {
                allBlocks.add(pos);
            }
        }
        if (!allBlocks.isEmpty()) {
            allBlocks.sort(this.targetSorter);
            for (BlockPos posx : allBlocks) {
                if (this.hasLineOfSightBlock(posx)) {
                    this.destinationBlock = posx;
                    return;
                }
            }
        }
        this.destinationBlock = null;
    }

    private boolean isBlockTouchingWater(BlockPos pos) {
        for (Direction dir : Direction.values()) {
            if (this.mantisShrimp.m_9236_().getFluidState(pos.relative(dir)).is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    private boolean isWithinXZDist(BlockPos blockpos, Vec3 positionVec, double distance) {
        return blockpos.m_123331_(AMBlockPos.fromCoords(positionVec.x(), (double) blockpos.m_123342_(), positionVec.z())) < distance * distance;
    }

    protected boolean getIsAboveDestination() {
        return this.isAboveDestinationBear;
    }

    private void breakBlock() {
        if (this.shouldMoveTo(this.mantisShrimp.m_9236_(), this.destinationBlock)) {
            BlockState state = this.mantisShrimp.m_9236_().getBlockState(this.destinationBlock);
            if (!this.mantisShrimp.m_9236_().m_46859_(this.destinationBlock) && ForgeHooks.canEntityDestroy(this.mantisShrimp.m_9236_(), this.destinationBlock, this.mantisShrimp) && state.m_60800_(this.mantisShrimp.m_9236_(), this.destinationBlock) >= 0.0F) {
                this.mantisShrimp.m_9236_().m_46961_(this.destinationBlock, true);
            }
        }
    }

    private boolean hasLineOfSightBlock(BlockPos destinationBlock) {
        Vec3 Vector3d = new Vec3(this.mantisShrimp.m_20185_(), this.mantisShrimp.m_20188_(), this.mantisShrimp.m_20189_());
        Vec3 blockVec = Vec3.atCenterOf(destinationBlock);
        BlockHitResult result = this.mantisShrimp.m_9236_().m_45547_(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mantisShrimp));
        return result.getBlockPos().equals(destinationBlock);
    }

    protected boolean shouldMoveTo(LevelReader worldIn, BlockPos pos) {
        Item blockItem = worldIn.m_8055_(pos).m_60734_().asItem();
        return this.mantisShrimp.m_21205_().getItem() == blockItem;
    }

    public static record BlockSorter(Entity entity) implements Comparator<BlockPos> {

        public int compare(BlockPos pos1, BlockPos pos2) {
            double distance1 = this.getDistance(pos1);
            double distance2 = this.getDistance(pos2);
            return Double.compare(distance1, distance2);
        }

        private double getDistance(BlockPos pos) {
            double deltaX = this.entity.getX() - ((double) pos.m_123341_() + 0.5);
            double deltaY = this.entity.getY() + (double) this.entity.getEyeHeight() - ((double) pos.m_123342_() + 0.5);
            double deltaZ = this.entity.getZ() - ((double) pos.m_123343_() + 0.5);
            return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
        }
    }
}