package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.FallingTreeBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.RelicheirusEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.MovingBlockData;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class RelicheirusPushTreesGoal extends MoveToBlockGoal {

    private static final int MAXIMUM_BLOCKS_PUSHED = 300;

    public static final int MAX_TREE_SPREAD = 12;

    private RelicheirusEntity relicheirus;

    private boolean madeTreeEntity = false;

    public RelicheirusPushTreesGoal(RelicheirusEntity relicheirus, int range) {
        super(relicheirus, 1.0, range, 6);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        this.relicheirus = relicheirus;
    }

    @Override
    public boolean canUse() {
        return this.relicheirus.getPushingTreesFor() > 0 && !this.relicheirus.m_6162_() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !this.madeTreeEntity;
    }

    @Override
    protected int nextStartTick(PathfinderMob mob) {
        return m_186073_(10 + this.relicheirus.m_217043_().nextInt(20));
    }

    @Override
    public double acceptedDistance() {
        return 4.0;
    }

    @Override
    protected boolean isReachedTarget() {
        BlockPos target = this.getMoveToTarget();
        return target != null && this.relicheirus.m_20275_((double) ((float) target.m_123341_() + 0.5F), this.relicheirus.m_20186_(), (double) ((float) target.m_123343_() + 0.5F)) < this.acceptedDistance();
    }

    @Override
    protected BlockPos getMoveToTarget() {
        return this.relicheirus.getStandAtTreePos(this.getBottomOfTree(this.relicheirus.m_9236_(), this.f_25602_));
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos target = this.getMoveToTarget();
        if (target != null) {
            if (this.isReachedTarget()) {
                if (this.relicheirus.lockTreePosition(this.f_25602_)) {
                    if (this.relicheirus.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                        this.relicheirus.setPeckY(this.f_25602_.m_123342_());
                        this.relicheirus.setAnimation(RelicheirusEntity.ANIMATION_PUSH_TREE);
                    } else if (this.relicheirus.getAnimation() == RelicheirusEntity.ANIMATION_PUSH_TREE && this.relicheirus.getAnimationTick() >= 35 && !this.madeTreeEntity) {
                        this.madeTreeEntity = true;
                        this.relicheirus.m_216990_(ACSoundRegistry.RELICHEIRUS_TOPPLE.get());
                        List<BlockPos> gathered = new ArrayList();
                        this.gatherAttachedBlocks(this.f_25602_, this.f_25602_, gathered);
                        if (!gathered.isEmpty()) {
                            List<MovingBlockData> allData = new ArrayList();
                            for (BlockPos pos : gathered) {
                                BlockState moveState = this.relicheirus.m_9236_().getBlockState(pos);
                                BlockEntity te = this.relicheirus.m_9236_().getBlockEntity(pos);
                                BlockPos offset = pos.subtract(this.f_25602_);
                                MovingBlockData data = new MovingBlockData(moveState, moveState.m_60808_(this.relicheirus.m_9236_(), pos), offset, te == null ? null : te.saveWithoutMetadata());
                                this.relicheirus.m_9236_().removeBlockEntity(pos);
                                allData.add(data);
                            }
                            for (BlockPos pos : gathered) {
                                this.relicheirus.m_9236_().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                            }
                            FallingTreeBlockEntity fallingTree = ACEntityRegistry.FALLING_TREE_BLOCK.get().create(this.relicheirus.m_9236_());
                            fallingTree.m_20219_(Vec3.atCenterOf(this.f_25602_));
                            fallingTree.setAllBlockData(FallingTreeBlockEntity.createTagFromData(allData));
                            fallingTree.setPlacementCooldown(1);
                            Vec3 vec3 = Vec3.atCenterOf(this.f_25602_).subtract(this.relicheirus.m_20182_());
                            float f = -((float) Mth.atan2(vec3.x, vec3.z)) * 180.0F / (float) Math.PI;
                            fallingTree.setFallDirection(Direction.fromYRot((double) f));
                            this.relicheirus.m_9236_().m_7967_(fallingTree);
                        }
                    }
                }
            } else if (this.relicheirus.m_21573_().isDone()) {
                Vec3 vec31 = Vec3.atCenterOf(target);
                Vec3 vec32 = vec31.subtract(this.relicheirus.m_20182_());
                if (vec32.length() > 1.0) {
                    vec32 = vec32.normalize();
                }
                Vec3 delta = new Vec3(vec32.x * 0.1F, 0.0, vec32.z * 0.1F);
                this.relicheirus.m_20256_(this.relicheirus.m_20184_().add(delta));
            }
        }
    }

    @Override
    protected void moveMobToBlock() {
        BlockPos pos = this.getMoveToTarget();
        this.f_25598_.m_21573_().moveTo((double) ((float) pos.m_123341_()) + 0.5, (double) pos.m_123342_(), (double) ((float) pos.m_123343_()) + 0.5, this.f_25599_);
    }

    @Override
    public void stop() {
        this.f_25602_ = BlockPos.ZERO;
        this.madeTreeEntity = false;
        super.m_8041_();
    }

    private BlockPos getBottomOfTree(LevelReader worldIn, BlockPos pos) {
        while (pos.m_123342_() > worldIn.getMinBuildHeight() && (worldIn.m_8055_(pos).m_204336_(ACTagRegistry.RELICHEIRUS_KNOCKABLE_LEAVES) || worldIn.m_8055_(pos).m_60795_() || worldIn.m_8055_(pos).m_204336_(ACTagRegistry.RELICHEIRUS_KNOCKABLE_LOGS))) {
            pos = pos.below();
        }
        return pos;
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        if (worldIn.m_8055_(pos).m_204336_(ACTagRegistry.RELICHEIRUS_KNOCKABLE_LOGS)) {
            BlockPos treeTop = new BlockPos(pos);
            while (worldIn.m_8055_(treeTop).m_204336_(ACTagRegistry.RELICHEIRUS_KNOCKABLE_LOGS) && treeTop.m_123342_() < worldIn.m_151558_()) {
                treeTop = treeTop.above();
            }
            if (worldIn.m_8055_(treeTop).m_204336_(ACTagRegistry.RELICHEIRUS_KNOCKABLE_LEAVES)) {
                return true;
            }
        }
        return false;
    }

    public void gatherAttachedBlocks(BlockPos origin, BlockPos pos, List<BlockPos> list) {
        if (list.size() < 300 && !list.contains(pos)) {
            list.add(pos);
            for (BlockPos blockpos1 : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                if (!blockpos1.equals(pos) && pos.m_203198_((double) origin.m_123341_(), (double) pos.m_123342_(), (double) origin.m_123343_()) < 12.0 && this.isTreePart(blockpos1)) {
                    this.gatherAttachedBlocks(origin, blockpos1.immutable(), list);
                }
            }
        }
    }

    public boolean isTreePart(BlockPos pos) {
        BlockState state = this.relicheirus.m_9236_().getBlockState(pos);
        return !state.m_60795_() && !state.m_204336_(ACTagRegistry.UNMOVEABLE) ? state.m_204336_(ACTagRegistry.RELICHEIRUS_KNOCKABLE_LOGS) || state.m_204336_(ACTagRegistry.RELICHEIRUS_KNOCKABLE_LEAVES) : false;
    }
}