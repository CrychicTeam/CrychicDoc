package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.EntityLeafcutterAnt;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class LeafcutterAntAIForageLeaves extends MoveToBlockGoal {

    private final EntityLeafcutterAnt ant;

    private int idleAtLeavesTime = 0;

    private int randomLeafCheckCooldown = 40;

    private BlockPos logStartPos = null;

    private BlockPos logTopPos = null;

    private final int searchRange;

    private final int verticalSearchRange;

    private int moveToCooldown = 0;

    public LeafcutterAntAIForageLeaves(EntityLeafcutterAnt LeafcutterAnt) {
        super(LeafcutterAnt, 1.0, 15, 3);
        this.searchRange = 15;
        this.verticalSearchRange = 3;
        this.ant = LeafcutterAnt;
    }

    @Override
    public boolean canUse() {
        return !this.ant.m_6162_() && !this.ant.hasLeaf() && !this.ant.m_6162_() && !this.ant.isQueen() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !this.ant.hasLeaf();
    }

    @Override
    public void stop() {
        this.idleAtLeavesTime = 0;
        this.logStartPos = null;
        this.logTopPos = null;
    }

    @Override
    public void start() {
        this.moveToCooldown = 10 + this.ant.m_217043_().nextInt(10);
    }

    @Override
    public double acceptedDistance() {
        return 2.0;
    }

    @Override
    public boolean shouldRecalculatePath() {
        return this.f_25601_ % 40 == 0 && this.logStartPos == null;
    }

    @Override
    public void tick() {
        if (this.moveToCooldown > 0) {
            this.moveToCooldown--;
        }
        if (this.randomLeafCheckCooldown > 0) {
            this.randomLeafCheckCooldown--;
        } else {
            this.randomLeafCheckCooldown = 30 + this.ant.m_217043_().nextInt(50);
            for (Direction dir : Direction.values()) {
                BlockPos offset = this.ant.m_20183_().relative(dir);
                if (this.isValidTarget(this.ant.m_9236_(), offset) && this.ant.m_217043_().nextInt(1) == 0) {
                    this.f_25602_ = offset;
                    this.logStartPos = null;
                }
            }
        }
        if (this.ant.getAttachmentFacing() == Direction.UP) {
            this.ant.m_21566_().setWantedPosition((double) ((float) this.f_25602_.m_123341_() + 0.5F), (double) this.f_25602_.m_123342_() - 1.0, (double) ((float) this.f_25602_.m_123343_() + 0.5F), 1.0);
            this.ant.m_20256_(this.ant.m_20184_().add(0.0, 0.5, 0.0));
            if (this.ant.m_217043_().nextInt(2) == 0 && this.isValidTarget(this.ant.m_9236_(), this.ant.m_20183_().above())) {
                this.f_25602_ = this.ant.m_20183_().above();
            }
        } else if (!((double) this.f_25602_.m_123342_() > this.ant.m_20186_() + 2.0) && this.logStartPos == null) {
            super.tick();
            this.logStartPos = null;
        } else {
            this.ant.m_21573_().stop();
            if (this.ant.m_217043_().nextInt(5) == 0 && this.isValidTarget(this.ant.m_9236_(), this.ant.m_20183_().below())) {
                this.f_25602_ = this.ant.m_20183_().below();
            }
            if (this.logStartPos == null) {
                for (int i = 0; i < 15; i++) {
                    BlockPos test = this.f_25602_.offset(6 - this.ant.m_217043_().nextInt(12), -this.ant.m_217043_().nextInt(7), 6 - this.ant.m_217043_().nextInt(12));
                    if (this.ant.m_9236_().getBlockState(test).m_204336_(BlockTags.LOGS)) {
                        this.logStartPos = test;
                        break;
                    }
                }
            } else {
                double xDif = (double) this.logStartPos.m_123341_() + 0.5 - this.ant.m_20185_();
                double zDif = (double) this.logStartPos.m_123343_() + 0.5 - this.ant.m_20189_();
                float f = (float) (Mth.atan2(zDif, xDif) * 180.0F / (float) Math.PI) - 90.0F;
                this.ant.m_146922_(f);
                this.ant.f_20883_ = this.ant.m_146908_();
                Vec3 vec = new Vec3((double) this.logStartPos.m_123341_() + 0.5, this.ant.m_20186_(), (double) this.logStartPos.m_123343_() + 0.5);
                vec = vec.subtract(this.ant.m_20182_());
                if (this.ant.m_20096_() || this.ant.onClimbable()) {
                    this.ant.m_20256_(vec.normalize().multiply(0.1, 0.0, 0.1).add(0.0, this.ant.m_20184_().y, 0.0));
                }
                if (this.moveToCooldown <= 0) {
                    this.moveToCooldown = 20 + this.ant.m_217043_().nextInt(30);
                    this.ant.m_21573_().moveTo((double) this.logStartPos.m_123341_(), this.ant.m_20186_(), (double) this.logStartPos.m_123343_(), 1.0);
                }
                if (Math.abs(xDif) < 0.6 && Math.abs(zDif) < 0.6) {
                    this.ant.m_20256_(this.ant.m_20184_().multiply(0.0, 1.0, 0.0));
                    this.ant.m_21566_().setWantedPosition((double) this.logStartPos.m_123341_() + 0.5, this.ant.m_20186_() + 2.0, (double) this.logStartPos.m_123343_() + 0.5, 1.0);
                    BlockPos test = new BlockPos(this.logStartPos.m_123341_(), (int) this.ant.m_20186_(), this.logStartPos.m_123343_());
                    if (!this.ant.m_9236_().getBlockState(test).m_204336_(BlockTags.LOGS) && this.ant.getAttachmentFacing() == Direction.DOWN) {
                        this.stop();
                        return;
                    }
                }
            }
            this.f_25601_++;
        }
        if (this.m_25625_() || this.ant.m_20183_().above().equals(this.f_25602_)) {
            this.ant.m_7618_(EntityAnchorArgument.Anchor.EYES, new Vec3((double) this.f_25602_.m_123341_() + 0.5, (double) this.f_25602_.m_123342_(), (double) this.f_25602_.m_123343_() + 0.5));
            this.ant.setAnimation(EntityLeafcutterAnt.ANIMATION_BITE);
            if (this.idleAtLeavesTime >= 6) {
                this.ant.setLeafHarvestedPos(this.f_25602_);
                this.ant.setLeafHarvestedState(this.ant.m_9236_().getBlockState(this.f_25602_));
                if (!this.ant.hasLeaf()) {
                    this.breakLeaves();
                }
                this.ant.setLeaf(true);
                this.stop();
                this.idleAtLeavesTime = 0;
            } else {
                this.idleAtLeavesTime++;
            }
        }
    }

    private void breakLeaves() {
        BlockState blockstate = this.ant.m_9236_().getBlockState(this.f_25602_);
        if (blockstate.m_204336_(AMTagRegistry.LEAFCUTTER_ANT_BREAKABLES) && ForgeEventFactory.getMobGriefingEvent(this.ant.m_9236_(), this.ant)) {
            this.ant.m_9236_().m_46961_(this.f_25602_, false);
            if ((double) this.ant.m_217043_().nextFloat() > AMConfig.leafcutterAntBreakLeavesChance) {
                this.ant.m_9236_().setBlockAndUpdate(this.f_25602_, blockstate);
            }
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        return worldIn.m_8055_(pos).m_204336_(AMTagRegistry.LEAFCUTTER_ANT_BREAKABLES);
    }

    @Override
    protected boolean findNearestBlock() {
        int i = this.searchRange;
        int j = this.verticalSearchRange;
        BlockPos blockpos = this.f_25598_.m_20183_();
        if (this.ant.hasHive() && this.ant.getHivePos() != null) {
            blockpos = this.ant.getHivePos();
            i *= 2;
        }
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k = this.f_25603_; k <= j; k = k > 0 ? -k : 1 - k) {
            for (int l = 0; l < i; l++) {
                for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                    for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                        blockpos$mutableblockpos.setWithOffset(blockpos, i1, k - 1, j1);
                        if (this.f_25598_.m_21444_(blockpos$mutableblockpos) && this.isValidTarget(this.f_25598_.m_9236_(), blockpos$mutableblockpos)) {
                            this.f_25602_ = blockpos$mutableblockpos;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}