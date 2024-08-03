package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

public class AnimalAISwimBottom extends RandomStrollGoal {

    public AnimalAISwimBottom(PathfinderMob p_i48937_1_, double p_i48937_2_, int p_i48937_4_) {
        super(p_i48937_1_, p_i48937_2_, p_i48937_4_);
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        Vec3 vec = DefaultRandomPos.getPos(this.f_25725_, 10, 7);
        int var2 = 0;
        while (vec != null && !this.f_25725_.m_9236_().getBlockState(AMBlockPos.fromVec3(vec)).m_60647_(this.f_25725_.m_9236_(), AMBlockPos.fromVec3(vec), PathComputationType.WATER) && var2++ < 10) {
            vec = DefaultRandomPos.getPos(this.f_25725_, 10, 7);
        }
        var2 = 1 + this.f_25725_.m_217043_().nextInt(3);
        if (vec == null) {
            return vec;
        } else {
            BlockPos pos = AMBlockPos.fromVec3(vec);
            while (this.f_25725_.m_9236_().getFluidState(pos).is(FluidTags.WATER) && this.f_25725_.m_9236_().getBlockState(pos).m_60647_(this.f_25725_.m_9236_(), AMBlockPos.fromVec3(vec), PathComputationType.WATER) && pos.m_123342_() > 1) {
                pos = pos.below();
            }
            pos = pos.above();
            for (int yUp = 0; this.f_25725_.m_9236_().getFluidState(pos).is(FluidTags.WATER) && this.f_25725_.m_9236_().getBlockState(pos).m_60647_(this.f_25725_.m_9236_(), AMBlockPos.fromVec3(vec), PathComputationType.WATER) && yUp < var2; yUp++) {
                pos = pos.above();
            }
            return Vec3.atCenterOf(pos);
        }
    }
}