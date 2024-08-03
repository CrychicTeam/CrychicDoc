package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityMantisShrimp;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MantisShrimpAIFryRice extends MoveToBlockGoal {

    private final EntityMantisShrimp mantisShrimp;

    private boolean wasLitPrior = false;

    private int cookingTicks = 0;

    public MantisShrimpAIFryRice(EntityMantisShrimp entityMantisShrimp) {
        super(entityMantisShrimp, 1.0, 8);
        this.mantisShrimp = entityMantisShrimp;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public void stop() {
        this.cookingTicks = 0;
        if (!this.wasLitPrior) {
            BlockPos blockpos = this.m_6669_().below();
            BlockState state = this.mantisShrimp.m_9236_().getBlockState(blockpos);
            if (state.m_60734_() instanceof AbstractFurnaceBlock && !this.wasLitPrior) {
                this.mantisShrimp.m_9236_().setBlockAndUpdate(blockpos, (BlockState) state.m_61124_(AbstractFurnaceBlock.LIT, false));
            }
        }
        super.m_8041_();
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos blockpos = this.m_6669_().below();
        if (this.m_25625_()) {
            BlockState state = this.mantisShrimp.m_9236_().getBlockState(blockpos);
            if (this.mantisShrimp.punchProgress == 0.0F) {
                this.mantisShrimp.punch();
            }
            if (state.m_60734_() instanceof AbstractFurnaceBlock && !this.wasLitPrior) {
                this.mantisShrimp.m_9236_().setBlockAndUpdate(blockpos, (BlockState) state.m_61124_(AbstractFurnaceBlock.LIT, true));
            }
            this.cookingTicks++;
            if (this.cookingTicks > 200) {
                this.cookingTicks = 0;
                ItemStack rice = new ItemStack(AMItemRegistry.SHRIMP_FRIED_RICE.get());
                rice.setCount(this.mantisShrimp.m_21205_().getCount());
                this.mantisShrimp.m_21008_(InteractionHand.MAIN_HAND, rice);
            }
        } else {
            this.cookingTicks = 0;
        }
    }

    @Override
    public boolean canUse() {
        return this.mantisShrimp.m_21205_().is(AMTagRegistry.SHRIMP_RICE_FRYABLES) && !this.mantisShrimp.isSitting() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.mantisShrimp.m_21205_().is(AMTagRegistry.SHRIMP_RICE_FRYABLES) && !this.mantisShrimp.isSitting() && super.canContinueToUse();
    }

    @Override
    public double acceptedDistance() {
        return 3.9F;
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        if (!worldIn.isEmptyBlock(pos.above())) {
            return false;
        } else {
            BlockState blockstate = worldIn.m_8055_(pos);
            if (blockstate.m_60734_() instanceof AbstractFurnaceBlock) {
                this.wasLitPrior = (Boolean) blockstate.m_61143_(AbstractFurnaceBlock.LIT);
                return true;
            } else {
                return blockstate.m_204336_(BlockTags.CAMPFIRES);
            }
        }
    }
}