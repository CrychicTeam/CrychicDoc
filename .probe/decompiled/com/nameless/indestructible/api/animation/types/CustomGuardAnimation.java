package com.nameless.indestructible.api.animation.types;

import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.model.Armature;

public class CustomGuardAnimation extends StaticAnimation {

    public final String successAnimation;

    public final Boolean isShield;

    public CustomGuardAnimation(String path, String successanimation, Armature armature, boolean isShield) {
        super(0.05F, true, path, armature);
        this.successAnimation = successanimation;
        this.isShield = isShield;
    }

    public CustomGuardAnimation(String path, String successanimation, Armature armature) {
        this(path, successanimation, armature, false);
    }
}