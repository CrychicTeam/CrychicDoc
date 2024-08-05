package com.github.alexthe666.alexsmobs.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.Vec3;

public class LaviathanAIRandomSwimming extends LavaAndWaterAIRandomSwimming {

    public LaviathanAIRandomSwimming(PathfinderMob creature, double speed, int chance) {
        super(creature, speed, chance);
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        BlockPos pos = this.f_25725_.m_20183_().offset(RandomPos.generateRandomDirection(this.f_25725_.m_217043_(), 16, 5));
        int i = 0;
        while (pos != null && this.f_25725_.m_9236_().getBlockState(new BlockPos(pos)).m_60819_().isEmpty() && i++ < 10) {
            pos = this.f_25725_.m_20183_().offset(RandomPos.generateRandomDirection(this.f_25725_.m_217043_(), 16, 5));
        }
        if (this.f_25725_.m_9236_().getBlockState(new BlockPos(pos)).m_60819_().isEmpty()) {
            return null;
        } else {
            if (this.f_25725_.m_217043_().nextInt(3) == 0) {
                while (!this.f_25725_.m_9236_().getBlockState(pos).m_60819_().isEmpty() && pos.m_123342_() < 255) {
                    pos = pos.above();
                }
                pos = pos.below();
            }
            return new Vec3((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F));
        }
    }
}