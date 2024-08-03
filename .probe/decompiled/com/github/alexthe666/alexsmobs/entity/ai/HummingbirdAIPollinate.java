package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityHummingbird;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class HummingbirdAIPollinate extends MoveToBlockGoal {

    private final EntityHummingbird bird;

    private int idleAtFlowerTime = 0;

    private boolean isAboveDestinationBear;

    public HummingbirdAIPollinate(EntityHummingbird bird) {
        super(bird, 1.0, 32, 8);
        this.bird = bird;
    }

    @Override
    public boolean canUse() {
        return !this.bird.m_6162_() && this.bird.pollinateCooldown == 0 && super.canUse();
    }

    @Override
    public void stop() {
        this.idleAtFlowerTime = 0;
    }

    @Override
    public double acceptedDistance() {
        return 3.0;
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos blockpos = this.m_6669_();
        if (!this.isWithinXZDist(blockpos, this.f_25598_.m_20182_(), this.acceptedDistance())) {
            this.isAboveDestinationBear = false;
            this.f_25601_++;
            double speedLoc = this.f_25599_;
            if (this.f_25598_.m_20275_((double) blockpos.m_123341_() + 0.5, (double) blockpos.m_123342_() + 0.5, (double) blockpos.m_123343_() + 0.5) >= 3.0) {
                speedLoc = this.f_25599_ * 0.3;
            }
            this.f_25598_.m_21566_().setWantedPosition((double) ((float) blockpos.m_123341_()) + 0.5, (double) blockpos.m_123342_(), (double) ((float) blockpos.m_123343_()) + 0.5, speedLoc);
        } else {
            this.isAboveDestinationBear = true;
            this.f_25601_--;
        }
        if (this.isReachedTarget() && Math.abs(this.bird.m_20186_() - (double) this.f_25602_.m_123342_()) <= 2.0) {
            this.bird.m_7618_(EntityAnchorArgument.Anchor.EYES, new Vec3((double) this.f_25602_.m_123341_() + 0.5, (double) this.f_25602_.m_123342_(), (double) this.f_25602_.m_123343_() + 0.5));
            if (this.idleAtFlowerTime >= 20) {
                this.pollinate();
                this.stop();
            } else {
                this.idleAtFlowerTime++;
            }
        }
    }

    private boolean isGrowable(BlockPos pos, ServerLevel world) {
        BlockState blockstate = world.m_8055_(pos);
        Block block = blockstate.m_60734_();
        return block instanceof CropBlock && !((CropBlock) block).isMaxAge(blockstate);
    }

    private boolean isWithinXZDist(BlockPos blockpos, Vec3 positionVec, double distance) {
        return blockpos.m_123331_(AMBlockPos.fromCoords(positionVec.x(), (double) blockpos.m_123342_(), positionVec.z())) < distance * distance;
    }

    @Override
    protected boolean isReachedTarget() {
        return this.isAboveDestinationBear;
    }

    private void pollinate() {
        this.bird.m_9236_().m_46796_(2005, this.f_25602_, 0);
        this.bird.setCropsPollinated(this.bird.getCropsPollinated() + 1);
        this.bird.pollinateCooldown = 200;
        if (this.bird.getCropsPollinated() > 3) {
            if (this.isGrowable(this.f_25602_, (ServerLevel) this.bird.m_9236_())) {
                BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), this.bird.m_9236_(), this.f_25602_);
            }
            this.bird.setCropsPollinated(0);
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        return !worldIn.m_8055_(pos).m_204336_(BlockTags.BEE_GROWABLES) && !worldIn.m_8055_(pos).m_204336_(BlockTags.FLOWERS) ? false : this.bird.pollinateCooldown == 0 && this.bird.canBlockBeSeen(pos);
    }
}