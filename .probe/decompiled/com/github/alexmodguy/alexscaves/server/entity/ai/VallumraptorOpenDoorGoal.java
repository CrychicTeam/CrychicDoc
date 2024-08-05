package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.phys.Vec3;

public class VallumraptorOpenDoorGoal extends DoorInteractGoal {

    private VallumraptorEntity raptor;

    private int timeSincePassing = 0;

    public VallumraptorOpenDoorGoal(VallumraptorEntity vallumraptor) {
        super(vallumraptor);
        this.raptor = vallumraptor;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.m_25200_();
    }

    @Override
    public boolean canContinueToUse() {
        return this.raptor.m_21824_() ? this.timeSincePassing < 15 : !this.m_25200_();
    }

    public boolean hasNotPassed() {
        return super.canContinueToUse();
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 vec3 = Vec3.atCenterOf(this.f_25190_);
        if (!this.m_25200_() && this.raptor.m_20238_(vec3) < 4.0) {
            this.raptor.m_7618_(EntityAnchorArgument.Anchor.EYES, vec3);
            if (this.raptor.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                this.raptor.setAnimation(VallumraptorEntity.ANIMATION_GRAB);
            }
        }
        if (this.raptor.getAnimation() == VallumraptorEntity.ANIMATION_GRAB && this.raptor.getAnimationTick() == 16) {
            this.m_25195_(true);
        }
        if (!this.hasNotPassed()) {
            this.timeSincePassing++;
        }
    }

    @Override
    public void stop() {
        super.m_8041_();
        if (this.raptor.m_21824_()) {
            this.m_25195_(false);
        }
        this.timeSincePassing = 0;
    }
}