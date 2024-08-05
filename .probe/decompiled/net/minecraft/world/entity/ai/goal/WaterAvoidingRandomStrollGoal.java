package net.minecraft.world.entity.ai.goal;

import javax.annotation.Nullable;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

public class WaterAvoidingRandomStrollGoal extends RandomStrollGoal {

    public static final float PROBABILITY = 0.001F;

    protected final float probability;

    public WaterAvoidingRandomStrollGoal(PathfinderMob pathfinderMob0, double double1) {
        this(pathfinderMob0, double1, 0.001F);
    }

    public WaterAvoidingRandomStrollGoal(PathfinderMob pathfinderMob0, double double1, float float2) {
        super(pathfinderMob0, double1);
        this.probability = float2;
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        if (this.f_25725_.m_20072_()) {
            Vec3 $$0 = LandRandomPos.getPos(this.f_25725_, 15, 7);
            return $$0 == null ? super.getPosition() : $$0;
        } else {
            return this.f_25725_.m_217043_().nextFloat() >= this.probability ? LandRandomPos.getPos(this.f_25725_, 10, 7) : super.getPosition();
        }
    }
}