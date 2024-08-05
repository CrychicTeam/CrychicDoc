package com.mojang.blaze3d.audio;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.lwjgl.openal.AL10;

public class Listener {

    private float gain = 1.0F;

    private Vec3 position = Vec3.ZERO;

    public void setListenerPosition(Vec3 vec0) {
        this.position = vec0;
        AL10.alListener3f(4100, (float) vec0.x, (float) vec0.y, (float) vec0.z);
    }

    public Vec3 getListenerPosition() {
        return this.position;
    }

    public void setListenerOrientation(Vector3f vectorF0, Vector3f vectorF1) {
        AL10.alListenerfv(4111, new float[] { vectorF0.x(), vectorF0.y(), vectorF0.z(), vectorF1.x(), vectorF1.y(), vectorF1.z() });
    }

    public void setGain(float float0) {
        AL10.alListenerf(4106, float0);
        this.gain = float0;
    }

    public float getGain() {
        return this.gain;
    }

    public void reset() {
        this.setListenerPosition(Vec3.ZERO);
        this.setListenerOrientation(new Vector3f(0.0F, 0.0F, -1.0F), new Vector3f(0.0F, 1.0F, 0.0F));
    }
}