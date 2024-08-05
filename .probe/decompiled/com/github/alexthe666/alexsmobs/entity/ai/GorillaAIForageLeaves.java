package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityGorilla;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class GorillaAIForageLeaves extends MoveToBlockGoal {

    private final EntityGorilla gorilla;

    private int idleAtLeavesTime = 0;

    private boolean isAboveDestinationBear;

    public GorillaAIForageLeaves(EntityGorilla gorilla) {
        super(gorilla, 1.0, 32, 3);
        this.gorilla = gorilla;
    }

    @Override
    public boolean canUse() {
        return !this.gorilla.m_6162_() && this.gorilla.m_21205_().isEmpty() && super.canUse();
    }

    @Override
    public void stop() {
        this.idleAtLeavesTime = 0;
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
        if (this.isReachedTarget() && Math.abs(this.gorilla.m_20186_() - (double) this.f_25602_.m_123342_()) <= 3.0) {
            this.gorilla.m_7618_(EntityAnchorArgument.Anchor.EYES, new Vec3((double) this.f_25602_.m_123341_() + 0.5, (double) this.f_25602_.m_123342_(), (double) this.f_25602_.m_123343_() + 0.5));
            if (this.gorilla.m_20186_() + 2.0 < (double) this.f_25602_.m_123342_()) {
                this.gorilla.setAnimation(this.gorilla.m_217043_().nextBoolean() ? EntityGorilla.ANIMATION_BREAKBLOCK_L : EntityGorilla.ANIMATION_BREAKBLOCK_R);
                this.gorilla.maxStandTime = 60;
                this.gorilla.setStanding(true);
            } else if (this.gorilla.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                this.gorilla.setAnimation(this.gorilla.m_217043_().nextBoolean() ? EntityGorilla.ANIMATION_BREAKBLOCK_L : EntityGorilla.ANIMATION_BREAKBLOCK_R);
            }
            if (this.idleAtLeavesTime >= 20) {
                this.breakLeaves();
            } else {
                this.idleAtLeavesTime++;
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

    private void breakLeaves() {
        if (ForgeEventFactory.getMobGriefingEvent(this.gorilla.m_9236_(), this.gorilla)) {
            BlockState blockstate = this.gorilla.m_9236_().getBlockState(this.f_25602_);
            if (blockstate.m_204336_(AMTagRegistry.GORILLA_BREAKABLES)) {
                this.gorilla.m_9236_().m_46961_(this.f_25602_, false);
                RandomSource rand = this.gorilla.m_217043_();
                ItemStack stack = new ItemStack(blockstate.m_60734_().asItem());
                ItemEntity itementity = new ItemEntity(this.gorilla.m_9236_(), (double) ((float) this.f_25602_.m_123341_() + rand.nextFloat()), (double) ((float) this.f_25602_.m_123342_() + rand.nextFloat()), (double) ((float) this.f_25602_.m_123343_() + rand.nextFloat()), stack);
                itementity.setDefaultPickUpDelay();
                this.gorilla.m_9236_().m_7967_(itementity);
                if (blockstate.m_204336_(AMTagRegistry.DROPS_BANANAS) && rand.nextInt(30) == 0) {
                    ItemStack banana = new ItemStack(AMItemRegistry.BANANA.get());
                    ItemEntity itementity2 = new ItemEntity(this.gorilla.m_9236_(), (double) ((float) this.f_25602_.m_123341_() + rand.nextFloat()), (double) ((float) this.f_25602_.m_123342_() + rand.nextFloat()), (double) ((float) this.f_25602_.m_123343_() + rand.nextFloat()), banana);
                    itementity2.setDefaultPickUpDelay();
                    this.gorilla.m_9236_().m_7967_(itementity2);
                }
                this.stop();
            }
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        return worldIn.m_8055_(pos).m_204336_(AMTagRegistry.GORILLA_BREAKABLES);
    }
}