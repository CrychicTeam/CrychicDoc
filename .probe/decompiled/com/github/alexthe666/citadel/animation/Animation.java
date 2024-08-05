package com.github.alexthe666.citadel.animation;

public class Animation {

    @Deprecated
    private int id;

    private int duration;

    private Animation(int duration) {
        this.duration = duration;
    }

    @Deprecated
    public static Animation create(int id, int duration) {
        Animation animation = create(duration);
        animation.id = id;
        return animation;
    }

    public static Animation create(int duration) {
        return new Animation(duration);
    }

    @Deprecated
    public int getID() {
        return this.id;
    }

    public int getDuration() {
        return this.duration;
    }
}