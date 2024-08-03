package org.violetmoon.quark.content.mobs.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.mobs.entity.Foxhound;

public class FindPlaceToSleepGoal extends MoveToBlockGoal {

    private final Foxhound foxhound;

    private final FindPlaceToSleepGoal.Target target;

    private boolean hadSlept = false;

    public FindPlaceToSleepGoal(Foxhound foxhound, double speed, FindPlaceToSleepGoal.Target target) {
        super(foxhound, speed, 8);
        this.foxhound = foxhound;
        this.target = target;
    }

    @Override
    public boolean canUse() {
        return this.foxhound.m_21824_() && !this.foxhound.m_21827_() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return (!this.hadSlept || this.foxhound.m_5803_()) && super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        this.hadSlept = false;
        this.foxhound.m_21839_(false);
        this.foxhound.getSleepGoal().setSleeping(false);
        this.foxhound.m_21837_(false);
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.hadSlept = false;
        this.foxhound.m_21839_(false);
        this.foxhound.getSleepGoal().setSleeping(false);
        this.foxhound.m_21837_(false);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = this.foxhound.m_20184_();
        if (!this.m_25625_() || motion.x > 0.0 || motion.z > 0.0) {
            this.foxhound.m_21839_(false);
            this.foxhound.getSleepGoal().setSleeping(false);
            this.foxhound.m_21837_(false);
        } else if (!this.foxhound.m_21827_()) {
            this.foxhound.m_21839_(true);
            this.foxhound.getSleepGoal().setSleeping(true);
            this.foxhound.m_21837_(true);
            this.foxhound.m_5802_(this.f_25602_.above());
            this.hadSlept = true;
        }
    }

    @Override
    protected boolean isValidTarget(@NotNull LevelReader world, @NotNull BlockPos pos) {
        if (!world.isEmptyBlock(pos.above())) {
            return false;
        } else {
            BlockState state = world.m_8055_(pos);
            BlockEntity tileentity = world.m_7702_(pos);
            int light = Quark.ZETA.blockExtensions.get(state).getLightEmissionZeta(state, world, pos);
            return switch(this.target) {
                case LIT_FURNACE ->
                    tileentity instanceof FurnaceBlockEntity && light > 2;
                case FURNACE ->
                    tileentity instanceof FurnaceBlockEntity && light <= 2;
                case GLOWING ->
                    light > 2;
            };
        }
    }

    public static enum Target {

        LIT_FURNACE, FURNACE, GLOWING
    }
}