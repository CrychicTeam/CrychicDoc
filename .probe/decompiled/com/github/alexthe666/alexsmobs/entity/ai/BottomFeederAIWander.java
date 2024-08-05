package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.ISemiAquatic;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public class BottomFeederAIWander extends RandomStrollGoal {

    private int waterChance = 0;

    private int landChance = 0;

    private int range = 5;

    public BottomFeederAIWander(PathfinderMob creature, double speed, int waterChance, int landChance) {
        super(creature, speed, waterChance);
        this.waterChance = waterChance;
        this.landChance = landChance;
    }

    public BottomFeederAIWander(PathfinderMob creature, double speed, int waterChance, int landChance, int range) {
        super(creature, speed, waterChance);
        this.waterChance = waterChance;
        this.landChance = landChance;
        this.range = range;
    }

    @Override
    public boolean canUse() {
        if (this.f_25725_ instanceof ISemiAquatic && ((ISemiAquatic) this.f_25725_).shouldStopMoving()) {
            return false;
        } else {
            this.f_25730_ = this.f_25725_.m_20069_() ? this.waterChance : this.landChance;
            return super.canUse();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.f_25725_ instanceof ISemiAquatic && ((ISemiAquatic) this.f_25725_).shouldStopMoving() ? false : super.canContinueToUse();
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        if (!this.f_25725_.m_20069_()) {
            return super.getPosition();
        } else {
            BlockPos blockpos = null;
            RandomSource random = this.f_25725_.m_217043_();
            for (int i = 0; i < 15; i++) {
                BlockPos blockPos = this.f_25725_.m_20183_().offset(random.nextInt(this.range) - this.range / 2, 3, random.nextInt(this.range) - this.range / 2);
                while ((this.f_25725_.m_9236_().m_46859_(blockPos) || this.f_25725_.m_9236_().getFluidState(blockPos).is(FluidTags.WATER)) && blockPos.m_123342_() > 1) {
                    blockPos = blockPos.below();
                }
                if (this.isBottomOfSeafloor(this.f_25725_.m_9236_(), blockPos.above())) {
                    blockpos = blockPos;
                }
            }
            return blockpos != null ? new Vec3((double) ((float) blockpos.m_123341_() + 0.5F), (double) ((float) blockpos.m_123342_() + 0.5F), (double) ((float) blockpos.m_123343_() + 0.5F)) : null;
        }
    }

    private boolean isBottomOfSeafloor(LevelAccessor world, BlockPos pos) {
        return world.m_6425_(pos).is(FluidTags.WATER) && world.m_6425_(pos.below()).isEmpty() && world.m_8055_(pos.below()).m_60815_();
    }
}