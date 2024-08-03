package dev.xkmc.modulargolems.content.entity.goals;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

public class GolemRandomStrollGoal extends RandomStrollGoal {

    private final AbstractGolemEntity<?, ?> golem;

    public GolemRandomStrollGoal(AbstractGolemEntity<?, ?> golem) {
        super(golem, 0.7, 60);
        this.golem = golem;
    }

    @Override
    public boolean canUse() {
        return this.golem.getMode().couldRandomStroll() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.golem.getMode().couldRandomStroll() && super.canContinueToUse();
    }

    @Override
    protected Vec3 getPosition() {
        if (this.f_25725_.m_20072_()) {
            if (this.golem.hasFlag(GolemFlags.SWIM)) {
                return BehaviorUtils.getRandomSwimmablePos(this.f_25725_, 10, 7);
            } else {
                Vec3 vec3 = LandRandomPos.getPos(this.f_25725_, 15, 7);
                return vec3 == null ? super.getPosition() : vec3;
            }
        } else {
            return LandRandomPos.getPos(this.f_25725_, 10, 7);
        }
    }
}