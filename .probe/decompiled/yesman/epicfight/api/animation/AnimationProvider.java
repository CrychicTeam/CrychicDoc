package yesman.epicfight.api.animation;

import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;

@FunctionalInterface
public interface AnimationProvider {

    StaticAnimation get();

    @FunctionalInterface
    public interface AttackAnimationProvider {

        AttackAnimation get();
    }
}