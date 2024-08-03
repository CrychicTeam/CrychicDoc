package net.minecraft.client.animation;

import org.joml.Vector3f;

public record Keyframe(float f_232283_, Vector3f f_232284_, AnimationChannel.Interpolation f_232285_) {

    private final float timestamp;

    private final Vector3f target;

    private final AnimationChannel.Interpolation interpolation;

    public Keyframe(float f_232283_, Vector3f f_232284_, AnimationChannel.Interpolation f_232285_) {
        this.timestamp = f_232283_;
        this.target = f_232284_;
        this.interpolation = f_232285_;
    }
}