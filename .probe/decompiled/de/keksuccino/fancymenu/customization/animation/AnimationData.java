package de.keksuccino.fancymenu.customization.animation;

import de.keksuccino.konkrete.rendering.animation.IAnimationRenderer;

public class AnimationData {

    public final String name;

    public final AnimationData.Type type;

    public final IAnimationRenderer animation;

    public AnimationData(IAnimationRenderer animation, String name, AnimationData.Type type) {
        this.name = name;
        this.type = type;
        this.animation = animation;
    }

    public static enum Type {

        INTERNAL, EXTERNAL
    }
}