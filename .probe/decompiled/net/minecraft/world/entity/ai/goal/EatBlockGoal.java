package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

public class EatBlockGoal extends Goal {

    private static final int EAT_ANIMATION_TICKS = 40;

    private static final Predicate<BlockState> IS_TALL_GRASS = BlockStatePredicate.forBlock(Blocks.GRASS);

    private final Mob mob;

    private final Level level;

    private int eatAnimationTick;

    public EatBlockGoal(Mob mob0) {
        this.mob = mob0;
        this.level = mob0.m_9236_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.mob.m_217043_().nextInt(this.mob.m_6162_() ? 50 : 1000) != 0) {
            return false;
        } else {
            BlockPos $$0 = this.mob.m_20183_();
            return IS_TALL_GRASS.test(this.level.getBlockState($$0)) ? true : this.level.getBlockState($$0.below()).m_60713_(Blocks.GRASS_BLOCK);
        }
    }

    @Override
    public void start() {
        this.eatAnimationTick = this.m_183277_(40);
        this.level.broadcastEntityEvent(this.mob, (byte) 10);
        this.mob.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.eatAnimationTick = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.eatAnimationTick > 0;
    }

    public int getEatAnimationTick() {
        return this.eatAnimationTick;
    }

    @Override
    public void tick() {
        this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        if (this.eatAnimationTick == this.m_183277_(4)) {
            BlockPos $$0 = this.mob.m_20183_();
            if (IS_TALL_GRASS.test(this.level.getBlockState($$0))) {
                if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    this.level.m_46961_($$0, false);
                }
                this.mob.ate();
            } else {
                BlockPos $$1 = $$0.below();
                if (this.level.getBlockState($$1).m_60713_(Blocks.GRASS_BLOCK)) {
                    if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                        this.level.m_46796_(2001, $$1, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
                        this.level.setBlock($$1, Blocks.DIRT.defaultBlockState(), 2);
                    }
                    this.mob.ate();
                }
            }
        }
    }
}