package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.RelicheirusEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class RelicheirusNibblePewensGoal extends MoveToBlockGoal {

    private RelicheirusEntity relicheirus;

    private boolean stopFlag = false;

    private int reachCheckTime = 50;

    public RelicheirusNibblePewensGoal(RelicheirusEntity relicheirus, int range) {
        super(relicheirus, 1.0, range, 6);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        this.relicheirus = relicheirus;
    }

    @Override
    protected int nextStartTick(PathfinderMob mob) {
        return m_186073_(220 + this.relicheirus.m_217043_().nextInt(500));
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
        return this.relicheirus.getStandAtTreePos(this.f_25602_);
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos target = this.getMoveToTarget();
        if (target != null) {
            if (this.reachCheckTime > 0) {
                this.reachCheckTime--;
            } else {
                this.reachCheckTime = 50 + this.relicheirus.m_217043_().nextInt(100);
                if (!this.canReach(target)) {
                    this.stopFlag = true;
                    this.f_25602_ = BlockPos.ZERO;
                    return;
                }
            }
            if (this.isReachedTarget()) {
                if (this.relicheirus.lockTreePosition(this.f_25602_)) {
                    if (this.relicheirus.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                        this.relicheirus.setPeckY(this.f_25602_.m_123342_());
                        this.relicheirus.setAnimation(RelicheirusEntity.ANIMATION_EAT_TREE);
                    } else if (this.relicheirus.getAnimation() == RelicheirusEntity.ANIMATION_EAT_TREE) {
                        if (this.relicheirus.getAnimationTick() >= 30) {
                            this.stopFlag = true;
                            this.f_25602_ = BlockPos.ZERO;
                            return;
                        }
                        if (this.relicheirus.getAnimationTick() % 8 == 0) {
                            BlockState back = this.relicheirus.m_9236_().getBlockState(this.f_25602_);
                            this.relicheirus.m_9236_().m_46953_(this.f_25602_, false, this.relicheirus);
                            this.relicheirus.m_9236_().setBlock(this.f_25602_, back, 3);
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
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !this.stopFlag;
    }

    @Override
    public void stop() {
        this.f_25602_ = BlockPos.ZERO;
        super.m_8041_();
        this.stopFlag = false;
    }

    private int getHeightOfBlock(LevelReader worldIn, BlockPos pos) {
        int i;
        for (i = 0; pos.m_123342_() > worldIn.getMinBuildHeight() && (worldIn.m_8055_(pos).m_204336_(ACTagRegistry.RELICHEIRUS_NIBBLES) || worldIn.m_8055_(pos).m_60795_() || worldIn.m_8055_(pos).m_204336_(ACTagRegistry.RELICHEIRUS_KNOCKABLE_LOGS)); i++) {
            pos = pos.below();
        }
        return i;
    }

    private boolean highEnough(LevelReader worldIn, BlockPos pos) {
        int height = this.getHeightOfBlock(worldIn, pos);
        return this.relicheirus.m_6162_() ? height <= 1 : height > 3 && height < 7;
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        return worldIn.m_8055_(pos).m_204336_(ACTagRegistry.RELICHEIRUS_NIBBLES) && this.highEnough(worldIn, pos);
    }

    private boolean canReach(BlockPos target) {
        Path path = this.relicheirus.m_21573_().createPath(target, 0);
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
}