package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.block.DinosaurEggBlock;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.LaysEggs;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class AnimalLayEggGoal extends MoveToBlockGoal {

    private final Animal mob;

    private final LaysEggs laysEggs;

    private final int maxTime;

    private int layEggCounter;

    private float startBodyRot;

    public AnimalLayEggGoal(Animal mob, int maxTime, double speed) {
        super(mob, speed, 16);
        this.mob = mob;
        this.laysEggs = (LaysEggs) this.mob;
        this.maxTime = maxTime;
    }

    @Override
    public boolean canUse() {
        return this.laysEggs.hasEgg() ? super.canUse() : false;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.laysEggs.hasEgg();
    }

    @Override
    public void start() {
        super.start();
        this.layEggCounter = 0;
    }

    @Override
    public double acceptedDistance() {
        return 4.0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_25625_()) {
            this.laysEggs.onLayEggTick(this.f_25602_.above(), this.layEggCounter);
            if (this.layEggCounter++ > this.maxTime) {
                Level level = this.mob.m_9236_();
                level.playSound((Player) null, this.f_25602_, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
                BlockPos blockpos1 = this.f_25602_.above();
                BlockState blockstate = this.laysEggs.createEggBlockState();
                level.setBlockAndUpdate(blockpos1, blockstate);
                level.m_220407_(GameEvent.BLOCK_PLACE, blockpos1, GameEvent.Context.of(this.mob, blockstate));
                this.laysEggs.setHasEgg(false);
                this.mob.setInLoveTime(600);
                this.mob.m_9236_().broadcastEntityEvent(this.mob, (byte) 78);
                if (this.mob instanceof DinosaurEntity dinosaur && level.getBlockState(this.f_25602_).m_204336_(BlockTags.DIRT)) {
                    level.setBlockAndUpdate(this.f_25602_, dinosaur.createEggBeddingBlockState());
                }
            }
        } else {
            this.startBodyRot = this.mob.f_20883_;
            this.layEggCounter = 0;
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
        if (levelReader.isEmptyBlock(blockPos.above()) && this.mob instanceof DinosaurEntity dinosaur) {
            BlockState eggState = dinosaur.createEggBlockState();
            return eggState != null && eggState.m_60734_() instanceof DinosaurEggBlock dinosaurEggBlock ? dinosaurEggBlock.isProperHabitat(levelReader, blockPos.above()) : true;
        } else {
            return false;
        }
    }
}