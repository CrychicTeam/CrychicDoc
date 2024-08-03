package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;

public class ClimbOnTopOfPowderSnowGoal extends Goal {

    private final Mob mob;

    private final Level level;

    public ClimbOnTopOfPowderSnowGoal(Mob mob0, Level level1) {
        this.mob = mob0;
        this.level = level1;
        this.m_7021_(EnumSet.of(Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        boolean $$0 = this.mob.f_146809_ || this.mob.f_146808_;
        if ($$0 && this.mob.m_6095_().is(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
            BlockPos $$1 = this.mob.m_20183_().above();
            BlockState $$2 = this.level.getBlockState($$1);
            return $$2.m_60713_(Blocks.POWDER_SNOW) || $$2.m_60812_(this.level, $$1) == Shapes.empty();
        } else {
            return false;
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.mob.getJumpControl().jump();
    }
}