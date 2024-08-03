package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityElephant;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class ElephantAIForageLeaves extends MoveToBlockGoal {

    private final EntityElephant elephant;

    private int idleAtLeavesTime = 0;

    private boolean isAboveDestinationBear;

    private int moveCooldown = 0;

    public ElephantAIForageLeaves(EntityElephant elephant) {
        super(elephant, 0.7, 32, 5);
        this.elephant = elephant;
    }

    @Override
    public boolean canUse() {
        return !this.elephant.m_6162_() && this.elephant.getControllingPassenger() == null && this.elephant.getControllingVillager() == null && this.elephant.m_21205_().isEmpty() && !this.elephant.aiItemFlag && super.canUse();
    }

    @Override
    public void stop() {
        this.idleAtLeavesTime = 0;
    }

    @Override
    public void start() {
        super.start();
        this.moveCooldown = 30 + this.elephant.m_217043_().nextInt(50);
    }

    @Override
    public double acceptedDistance() {
        return 4.0;
    }

    @Override
    public boolean shouldRecalculatePath() {
        return this.moveCooldown == 0;
    }

    @Override
    public void tick() {
        if (this.moveCooldown > 0) {
            this.moveCooldown--;
        }
        BlockPos blockpos = this.m_6669_();
        if (!this.isWithinXZDist(blockpos, this.f_25598_.m_20182_(), this.acceptedDistance())) {
            this.isAboveDestinationBear = false;
            this.f_25601_++;
            if (this.shouldRecalculatePath()) {
                this.moveCooldown = 30 + this.elephant.m_217043_().nextInt(50);
                this.f_25598_.m_21573_().moveTo((double) ((float) blockpos.m_123341_()) + 0.5, (double) blockpos.m_123342_(), (double) ((float) blockpos.m_123343_()) + 0.5, this.f_25599_);
            }
        } else {
            this.isAboveDestinationBear = true;
            this.f_25601_ = 0;
        }
        if (this.isReachedTarget() && Math.abs(this.elephant.m_20186_() - (double) this.f_25602_.m_123342_()) <= 3.0) {
            this.elephant.m_7618_(EntityAnchorArgument.Anchor.EYES, new Vec3((double) this.f_25602_.m_123341_() + 0.5, (double) this.f_25602_.m_123342_(), (double) this.f_25602_.m_123343_() + 0.5));
            if (this.elephant.m_20186_() + 2.0 < (double) this.f_25602_.m_123342_()) {
                if (this.elephant.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    this.elephant.setAnimation(EntityElephant.ANIMATION_BREAKLEAVES);
                }
                this.elephant.setStanding(true);
                this.elephant.maxStandTime = 15;
            } else {
                this.elephant.setAnimation(EntityElephant.ANIMATION_BREAKLEAVES);
                this.elephant.setStanding(false);
            }
            if (this.idleAtLeavesTime >= 10) {
                this.breakLeaves();
            } else {
                this.idleAtLeavesTime++;
            }
        }
    }

    @Override
    protected void moveMobToBlock() {
    }

    @Override
    protected int nextStartTick(PathfinderMob p_203109_1_) {
        return 100 + p_203109_1_.m_217043_().nextInt(200);
    }

    private boolean isWithinXZDist(BlockPos blockpos, Vec3 positionVec, double distance) {
        return blockpos.m_123331_(new BlockPos((int) positionVec.x(), blockpos.m_123342_(), (int) positionVec.z())) < distance * distance;
    }

    @Override
    protected boolean isReachedTarget() {
        return this.isAboveDestinationBear;
    }

    private void breakLeaves() {
        if (ForgeEventFactory.getMobGriefingEvent(this.elephant.m_9236_(), this.elephant)) {
            BlockState blockstate = this.elephant.m_9236_().getBlockState(this.f_25602_);
            if (blockstate.m_204336_(AMTagRegistry.ELEPHANT_FOODBLOCKS)) {
                this.elephant.m_9236_().m_46961_(this.f_25602_, false);
                RandomSource rand = this.elephant.m_217043_();
                ItemStack stack = new ItemStack(blockstate.m_60734_().asItem());
                ItemEntity itementity = new ItemEntity(this.elephant.m_9236_(), (double) ((float) this.f_25602_.m_123341_() + rand.nextFloat()), (double) ((float) this.f_25602_.m_123342_() + rand.nextFloat()), (double) ((float) this.f_25602_.m_123343_() + rand.nextFloat()), stack);
                itementity.setDefaultPickUpDelay();
                this.elephant.m_9236_().m_7967_(itementity);
                if (blockstate.m_204336_(AMTagRegistry.DROPS_ACACIA_BLOSSOMS) && rand.nextInt(30) == 0) {
                    ItemStack banana = new ItemStack(AMItemRegistry.ACACIA_BLOSSOM.get());
                    ItemEntity itementity2 = new ItemEntity(this.elephant.m_9236_(), (double) ((float) this.f_25602_.m_123341_() + rand.nextFloat()), (double) ((float) this.f_25602_.m_123342_() + rand.nextFloat()), (double) ((float) this.f_25602_.m_123343_() + rand.nextFloat()), banana);
                    itementity2.setDefaultPickUpDelay();
                    this.elephant.m_9236_().m_7967_(itementity2);
                }
                this.stop();
            }
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        return !this.elephant.aiItemFlag && worldIn.m_8055_(pos).m_204336_(AMTagRegistry.ELEPHANT_FOODBLOCKS);
    }
}