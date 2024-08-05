package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

public class BreathAirGoal extends Goal {

    private final PathfinderMob mob;

    public BreathAirGoal(PathfinderMob pathfinderMob0) {
        this.mob = pathfinderMob0;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.mob.m_20146_() < 140;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        this.findAirPosition();
    }

    private void findAirPosition() {
        Iterable<BlockPos> $$0 = BlockPos.betweenClosed(Mth.floor(this.mob.m_20185_() - 1.0), this.mob.m_146904_(), Mth.floor(this.mob.m_20189_() - 1.0), Mth.floor(this.mob.m_20185_() + 1.0), Mth.floor(this.mob.m_20186_() + 8.0), Mth.floor(this.mob.m_20189_() + 1.0));
        BlockPos $$1 = null;
        for (BlockPos $$2 : $$0) {
            if (this.givesAir(this.mob.m_9236_(), $$2)) {
                $$1 = $$2;
                break;
            }
        }
        if ($$1 == null) {
            $$1 = BlockPos.containing(this.mob.m_20185_(), this.mob.m_20186_() + 8.0, this.mob.m_20189_());
        }
        this.mob.m_21573_().moveTo((double) $$1.m_123341_(), (double) ($$1.m_123342_() + 1), (double) $$1.m_123343_(), 1.0);
    }

    @Override
    public void tick() {
        this.findAirPosition();
        this.mob.m_19920_(0.02F, new Vec3((double) this.mob.f_20900_, (double) this.mob.f_20901_, (double) this.mob.f_20902_));
        this.mob.m_6478_(MoverType.SELF, this.mob.m_20184_());
    }

    private boolean givesAir(LevelReader levelReader0, BlockPos blockPos1) {
        BlockState $$2 = levelReader0.m_8055_(blockPos1);
        return (levelReader0.m_6425_(blockPos1).isEmpty() || $$2.m_60713_(Blocks.BUBBLE_COLUMN)) && $$2.m_60647_(levelReader0, blockPos1, PathComputationType.LAND);
    }
}