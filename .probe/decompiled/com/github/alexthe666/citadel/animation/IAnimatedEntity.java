package com.github.alexthe666.citadel.animation;

public interface IAnimatedEntity {

    Animation NO_ANIMATION = Animation.create(0);

    int getAnimationTick();

    void setAnimationTick(int var1);

    Animation getAnimation();

    void setAnimation(Animation var1);

    Animation[] getAnimations();
}