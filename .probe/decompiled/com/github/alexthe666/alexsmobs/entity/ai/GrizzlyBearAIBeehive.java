package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class GrizzlyBearAIBeehive extends MoveToBlockGoal {

    private final EntityGrizzlyBear bear;

    private int idleAtHiveTime = 0;

    private boolean isAboveDestinationBear;

    public GrizzlyBearAIBeehive(EntityGrizzlyBear bear) {
        super(bear, 1.0, 32, 8);
        this.bear = bear;
    }

    @Override
    public boolean canUse() {
        return !this.bear.m_6162_() && super.canUse();
    }

    @Override
    public void stop() {
        this.idleAtHiveTime = 0;
    }

    @Override
    public double acceptedDistance() {
        return 2.0;
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos blockpos = this.m_6669_();
        if (!this.isWithinXZDist(blockpos, this.f_25598_.m_20182_(), this.acceptedDistance())) {
            this.isAboveDestinationBear = false;
            this.f_25601_++;
            if (this.m_8064_()) {
                this.f_25598_.m_21573_().moveTo((double) ((float) blockpos.m_123341_()) + 0.5, (double) blockpos.m_123342_(), (double) ((float) blockpos.m_123343_()) + 0.5, this.f_25599_);
            }
        } else {
            this.isAboveDestinationBear = true;
            this.f_25601_--;
        }
        if (this.isReachedTarget() && Math.abs(this.bear.m_20186_() - (double) this.f_25602_.m_123342_()) <= 3.0) {
            this.bear.m_7618_(EntityAnchorArgument.Anchor.EYES, new Vec3((double) this.f_25602_.m_123341_() + 0.5, (double) this.f_25602_.m_123342_(), (double) this.f_25602_.m_123343_() + 0.5));
            if (this.bear.m_20186_() + 2.0 < (double) this.f_25602_.m_123342_()) {
                this.bear.setAnimation(EntityGrizzlyBear.ANIMATION_MAUL);
                this.bear.maxStandTime = 60;
                this.bear.setStanding(true);
            } else if (this.bear.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                this.bear.setAnimation(this.bear.m_217043_().nextBoolean() ? EntityGrizzlyBear.ANIMATION_SWIPE_L : EntityGrizzlyBear.ANIMATION_SWIPE_R);
            }
            if (this.idleAtHiveTime >= 20) {
                this.eatHive();
            } else {
                this.idleAtHiveTime++;
            }
        }
    }

    private boolean isWithinXZDist(BlockPos blockpos, Vec3 positionVec, double distance) {
        return blockpos.m_123331_(AMBlockPos.fromCoords(positionVec.x(), (double) blockpos.m_123342_(), positionVec.z())) < distance * distance;
    }

    @Override
    protected boolean isReachedTarget() {
        return this.isAboveDestinationBear;
    }

    private void eatHive() {
        if (ForgeEventFactory.getMobGriefingEvent(this.bear.m_9236_(), this.bear)) {
            BlockState blockstate = this.bear.m_9236_().getBlockState(this.f_25602_);
            if (blockstate.m_204336_(AMTagRegistry.GRIZZLY_BEEHIVE) && this.bear.m_9236_().getBlockEntity(this.f_25602_) instanceof BeehiveBlockEntity) {
                RandomSource rand = this.bear.m_217043_();
                BeehiveBlockEntity beehivetileentity = (BeehiveBlockEntity) this.bear.m_9236_().getBlockEntity(this.f_25602_);
                beehivetileentity.emptyAllLivingFromHive(null, blockstate, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
                this.bear.m_9236_().updateNeighbourForOutputSignal(this.f_25602_, blockstate.m_60734_());
                ItemStack stack = new ItemStack(Items.HONEYCOMB);
                int level = 0;
                if (blockstate.m_60734_() instanceof BeehiveBlock) {
                    level = (Integer) blockstate.m_61143_(BeehiveBlock.HONEY_LEVEL);
                }
                for (int i = 0; i < level; i++) {
                    ItemEntity itementity = new ItemEntity(this.bear.m_9236_(), (double) ((float) this.f_25602_.m_123341_() + rand.nextFloat()), (double) ((float) this.f_25602_.m_123342_() + rand.nextFloat()), (double) ((float) this.f_25602_.m_123343_() + rand.nextFloat()), stack);
                    itementity.setDefaultPickUpDelay();
                    this.bear.m_9236_().m_7967_(itementity);
                }
                this.bear.m_9236_().m_46961_(this.f_25602_, false);
                if (blockstate.m_60734_() instanceof BeehiveBlock) {
                    this.bear.m_9236_().setBlockAndUpdate(this.f_25602_, (BlockState) blockstate.m_61124_(BeehiveBlock.HONEY_LEVEL, 0));
                }
                double d0 = 15.0;
                for (Bee bee : this.bear.m_9236_().m_45976_(Bee.class, new AABB((double) this.f_25602_.m_123341_() - d0, (double) this.f_25602_.m_123342_() - d0, (double) this.f_25602_.m_123343_() - d0, (double) this.f_25602_.m_123341_() + d0, (double) this.f_25602_.m_123342_() + d0, (double) this.f_25602_.m_123343_() + d0))) {
                    bee.setRemainingPersistentAngerTime(100);
                    bee.m_6710_(this.bear);
                    bee.setStayOutOfHiveCountdown(400);
                }
                this.stop();
            }
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        if (worldIn.m_8055_(pos).m_204336_(AMTagRegistry.GRIZZLY_BEEHIVE) && worldIn.m_7702_(pos) instanceof BeehiveBlockEntity && worldIn.m_8055_(pos).m_60734_() instanceof BeehiveBlock) {
            int i = (Integer) worldIn.m_8055_(pos).m_61143_(BeehiveBlock.HONEY_LEVEL);
            return i > 0;
        } else {
            return false;
        }
    }
}