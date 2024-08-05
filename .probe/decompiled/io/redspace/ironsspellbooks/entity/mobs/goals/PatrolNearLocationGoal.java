package io.redspace.ironsspellbooks.entity.mobs.goals;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class PatrolNearLocationGoal extends WaterAvoidingRandomStrollGoal {

    LazyOptional<Vec3> origin;

    float radiusSqr;

    public PatrolNearLocationGoal(PathfinderMob pMob, float radius, double pSpeedModifier) {
        super(pMob, pSpeedModifier);
        this.origin = LazyOptional.of(pMob::m_20182_);
        this.radiusSqr = radius * radius;
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        Vec3 f = super.getPosition();
        if (this.f_25725_.m_20182_().horizontalDistanceSqr() > (double) this.radiusSqr) {
            f = LandRandomPos.getPosTowards(this.f_25725_, 8, 4, (Vec3) this.origin.resolve().get());
        }
        return f;
    }
}