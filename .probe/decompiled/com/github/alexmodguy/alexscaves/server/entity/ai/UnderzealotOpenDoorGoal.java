package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.phys.Vec3;

public class UnderzealotOpenDoorGoal extends OpenDoorGoal {

    private UnderzealotEntity underzealot;

    public UnderzealotOpenDoorGoal(UnderzealotEntity underzealot) {
        super(underzealot, false);
        this.underzealot = underzealot;
    }

    @Override
    public void start() {
        if (this.underzealot.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.underzealot.setAnimation(UnderzealotEntity.ANIMATION_BREAKTORCH);
        }
    }

    @Override
    public boolean canUse() {
        return super.m_8036_() && !this.m_25200_();
    }

    @Override
    public boolean canContinueToUse() {
        return this.underzealot.getAnimation() == UnderzealotEntity.ANIMATION_BREAKTORCH && !this.m_25200_();
    }

    @Override
    public void tick() {
        this.underzealot.m_7618_(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(this.f_25190_));
        if (this.underzealot.getAnimation() == UnderzealotEntity.ANIMATION_BREAKTORCH && this.underzealot.getAnimationTick() == 8) {
            this.m_25195_(true);
        }
    }

    @Override
    public void stop() {
    }
}