package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class AmphithereAIFollowOwner extends Goal {

    private final EntityAmphithere ampithere;

    private final double followSpeed;

    Level world;

    float maxDist;

    float minDist;

    private LivingEntity owner;

    private int timeToRecalcPath;

    private float oldWaterCost;

    public AmphithereAIFollowOwner(EntityAmphithere ampithereIn, double followSpeedIn, float minDistIn, float maxDistIn) {
        this.ampithere = ampithereIn;
        this.world = ampithereIn.m_9236_();
        this.followSpeed = followSpeedIn;
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity LivingEntity = this.ampithere.m_269323_();
        if (this.ampithere.getCommand() != 2) {
            return false;
        } else if (LivingEntity == null) {
            return false;
        } else if (LivingEntity instanceof Player && LivingEntity.m_5833_()) {
            return false;
        } else if (this.ampithere.isOrderedToSit()) {
            return false;
        } else if (this.ampithere.m_20280_(LivingEntity) < (double) (this.minDist * this.minDist)) {
            return false;
        } else {
            this.owner = LivingEntity;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.noPath() && this.ampithere.m_20280_(this.owner) > (double) (this.maxDist * this.maxDist) && !this.ampithere.isOrderedToSit();
    }

    private boolean noPath() {
        return !this.ampithere.isFlying() ? this.ampithere.m_21573_().isDone() : false;
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.ampithere.m_21439_(BlockPathTypes.WATER);
        this.ampithere.m_21441_(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.ampithere.m_21573_().stop();
        this.ampithere.m_21441_(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.ampithere.m_21563_().setLookAt(this.owner, 10.0F, (float) this.ampithere.m_8132_());
        if (!this.ampithere.isOrderedToSit() && --this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            this.tryMoveTo();
            if (!this.ampithere.m_21523_() && !this.ampithere.m_20159_() && this.ampithere.m_20280_(this.owner) >= 144.0) {
                int i = Mth.floor(this.owner.m_20185_()) - 2;
                int j = Mth.floor(this.owner.m_20189_()) - 2;
                int k = Mth.floor(this.owner.m_20191_().minY);
                for (int l = 0; l <= 4; l++) {
                    for (int i1 = 0; i1 <= 4; i1++) {
                        if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.canTeleportToBlock(new BlockPos(i, j, k))) {
                            this.ampithere.m_7678_((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.ampithere.m_146908_(), this.ampithere.m_146909_());
                            this.ampithere.m_21573_().stop();
                            return;
                        }
                    }
                }
            }
        }
    }

    protected boolean canTeleportToBlock(BlockPos pos) {
        BlockState blockstate = this.world.getBlockState(pos);
        return blockstate.m_60643_(this.world, pos, this.ampithere.m_6095_()) && this.world.m_46859_(pos.above()) && this.world.m_46859_(pos.above(2));
    }

    private boolean tryMoveTo() {
        if (!this.ampithere.isFlying()) {
            return this.ampithere.m_21573_().moveTo(this.owner, this.followSpeed);
        } else {
            this.ampithere.m_21566_().setWantedPosition(this.owner.m_20185_(), this.owner.m_20186_() + (double) this.owner.m_20192_() + 5.0 + (double) this.ampithere.m_217043_().nextInt(8), this.owner.m_20189_(), 0.25);
            return true;
        }
    }
}