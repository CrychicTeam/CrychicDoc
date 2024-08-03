package net.minecraft.world.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class DolphinJumpGoal extends JumpGoal {

    private static final int[] STEPS_TO_CHECK = new int[] { 0, 1, 4, 5, 6, 7 };

    private final Dolphin dolphin;

    private final int interval;

    private boolean breached;

    public DolphinJumpGoal(Dolphin dolphin0, int int1) {
        this.dolphin = dolphin0;
        this.interval = m_186073_(int1);
    }

    @Override
    public boolean canUse() {
        if (this.dolphin.m_217043_().nextInt(this.interval) != 0) {
            return false;
        } else {
            Direction $$0 = this.dolphin.m_6374_();
            int $$1 = $$0.getStepX();
            int $$2 = $$0.getStepZ();
            BlockPos $$3 = this.dolphin.m_20183_();
            for (int $$4 : STEPS_TO_CHECK) {
                if (!this.waterIsClear($$3, $$1, $$2, $$4) || !this.surfaceIsClear($$3, $$1, $$2, $$4)) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean waterIsClear(BlockPos blockPos0, int int1, int int2, int int3) {
        BlockPos $$4 = blockPos0.offset(int1 * int3, 0, int2 * int3);
        return this.dolphin.m_9236_().getFluidState($$4).is(FluidTags.WATER) && !this.dolphin.m_9236_().getBlockState($$4).m_280555_();
    }

    private boolean surfaceIsClear(BlockPos blockPos0, int int1, int int2, int int3) {
        return this.dolphin.m_9236_().getBlockState(blockPos0.offset(int1 * int3, 1, int2 * int3)).m_60795_() && this.dolphin.m_9236_().getBlockState(blockPos0.offset(int1 * int3, 2, int2 * int3)).m_60795_();
    }

    @Override
    public boolean canContinueToUse() {
        double $$0 = this.dolphin.m_20184_().y;
        return (!($$0 * $$0 < 0.03F) || this.dolphin.m_146909_() == 0.0F || !(Math.abs(this.dolphin.m_146909_()) < 10.0F) || !this.dolphin.m_20069_()) && !this.dolphin.m_20096_();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        Direction $$0 = this.dolphin.m_6374_();
        this.dolphin.m_20256_(this.dolphin.m_20184_().add((double) $$0.getStepX() * 0.6, 0.7, (double) $$0.getStepZ() * 0.6));
        this.dolphin.m_21573_().stop();
    }

    @Override
    public void stop() {
        this.dolphin.m_146926_(0.0F);
    }

    @Override
    public void tick() {
        boolean $$0 = this.breached;
        if (!$$0) {
            FluidState $$1 = this.dolphin.m_9236_().getFluidState(this.dolphin.m_20183_());
            this.breached = $$1.is(FluidTags.WATER);
        }
        if (this.breached && !$$0) {
            this.dolphin.m_5496_(SoundEvents.DOLPHIN_JUMP, 1.0F, 1.0F);
        }
        Vec3 $$2 = this.dolphin.m_20184_();
        if ($$2.y * $$2.y < 0.03F && this.dolphin.m_146909_() != 0.0F) {
            this.dolphin.m_146926_(Mth.rotLerp(0.2F, this.dolphin.m_146909_(), 0.0F));
        } else if ($$2.length() > 1.0E-5F) {
            double $$3 = $$2.horizontalDistance();
            double $$4 = Math.atan2(-$$2.y, $$3) * 180.0F / (float) Math.PI;
            this.dolphin.m_146926_((float) $$4);
        }
    }
}