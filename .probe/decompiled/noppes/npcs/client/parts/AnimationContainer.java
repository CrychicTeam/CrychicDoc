package noppes.npcs.client.parts;

import noppes.npcs.shared.common.util.NopVector3f;

public class AnimationContainer {

    public final int animation;

    public final String part;

    public final int length;

    public final int actualLength;

    public final float speed;

    public final boolean additional;

    public final boolean loop;

    public boolean hasRotation = false;

    public boolean hasTranslate = false;

    public final NopVector3f[] rotations;

    public final NopVector3f[] translates;

    public int startupTicks = Integer.MAX_VALUE;

    public AnimationContainer(int animation, String part, int length, float speed, boolean additional, boolean loop) {
        this.animation = animation;
        this.part = part;
        this.length = length;
        this.speed = speed;
        this.additional = additional;
        this.loop = loop;
        if (loop) {
            this.actualLength = length;
        } else {
            this.actualLength = length > 2 ? length * 2 - 2 : length;
        }
        this.rotations = new NopVector3f[this.actualLength];
        this.translates = new NopVector3f[this.actualLength];
    }

    public void start() {
        this.startupTicks = 0;
    }

    public void animation(ModelPartWrapper part, int step, float partialTick) {
        float f = (float) step / 20.0F * this.speed;
        int i = (int) f;
        float pf = (float) (step - 1) / 20.0F * this.speed;
        int pi = (int) pf;
        if (pi != i) {
            this.step(part, i, (f - (float) i) * partialTick);
        } else {
            this.step(part, i, pf - (float) pi + (f - pf) * partialTick);
        }
        if (this.startupTicks < this.actualLength && i != pi) {
            this.startupTicks++;
        }
    }

    public void animation(ModelPartWrapper part, float step) {
        float f = step * this.speed * (float) (this.length - 1);
        int i = (int) f;
        this.step(part, i, f - (float) i);
    }

    public void step(ModelPartWrapper part, int step, float progress) {
        int i = step % this.actualLength;
        int j = (step + 1) % this.actualLength;
        if (this.startupTicks < 60) {
            if (this.hasRotation) {
                part.setRot(part.getRot().lerp(this.rotations[i].lerp(this.rotations[j], progress), 0.15F));
            } else {
                part.setRot(part.getRot().lerp(part.oriRot, 0.15F));
            }
            if (this.hasTranslate) {
                part.setPos(part.getPos().lerp(this.translates[i].lerp(this.translates[j], progress), 0.15F));
            } else {
                part.setPos(part.getPos().lerp(part.oriPos, 0.15F));
            }
        } else {
            if (this.hasRotation) {
                if (this.loop && j < i) {
                    part.setRot(this.rotations[i].subtract(NopVector3f.ROTATION).modulo(NopVector3f.ROTATION).lerp(this.rotations[j], progress));
                } else {
                    part.setRot(this.rotations[i].lerp(this.rotations[j], progress));
                }
            }
            if (this.hasTranslate) {
                part.setPos(this.translates[i].lerp(this.translates[j], progress));
            }
        }
    }

    public AnimationContainer copy() {
        AnimationContainer container = new AnimationContainer(this.animation, this.part, this.length, this.speed, this.additional, this.loop);
        container.hasRotation = this.hasRotation;
        container.hasTranslate = this.hasTranslate;
        for (int i = 0; i < this.actualLength; i++) {
            if (i < this.length) {
                if (this.hasTranslate) {
                    container.translates[i] = this.translates[i];
                }
                if (this.hasRotation) {
                    container.rotations[i] = this.rotations[i];
                }
            } else {
                if (this.hasTranslate) {
                    container.translates[i] = container.translates[this.length - i % this.length - 2];
                }
                if (this.hasRotation) {
                    container.rotations[i] = container.rotations[this.length - i % this.length - 2];
                }
            }
        }
        return container;
    }
}