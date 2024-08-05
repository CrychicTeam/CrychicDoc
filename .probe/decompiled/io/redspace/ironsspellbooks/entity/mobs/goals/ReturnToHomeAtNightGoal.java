package io.redspace.ironsspellbooks.entity.mobs.goals;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ReturnToHomeAtNightGoal<T extends PathfinderMob & HomeOwner> extends WaterAvoidingRandomStrollGoal {

    T homeOwnerMob;

    public ReturnToHomeAtNightGoal(T pMob, double pSpeedModifier) {
        super(pMob, pSpeedModifier);
        this.homeOwnerMob = pMob;
    }

    @Override
    public boolean canUse() {
        return this.homeOwnerMob.getHome() != null && !this.f_25725_.f_19853_.isDay() && super.m_8036_();
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        return this.homeOwnerMob.getHome() == null ? super.getPosition() : Vec3.atBottomCenterOf(this.homeOwnerMob.getHome());
    }
}