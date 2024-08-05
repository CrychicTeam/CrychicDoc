package net.minecraft.world.entity.ai.goal;

import javax.annotation.Nullable;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.phys.Vec3;

public class RandomSwimmingGoal extends RandomStrollGoal {

    public RandomSwimmingGoal(PathfinderMob pathfinderMob0, double double1, int int2) {
        super(pathfinderMob0, double1, int2);
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        return BehaviorUtils.getRandomSwimmablePos(this.f_25725_, 10, 7);
    }
}