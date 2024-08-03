package team.lodestar.lodestone.systems.postprocess;

import java.util.function.BiConsumer;

public abstract class DynamicShaderFxInstance {

    private float time = 0.0F;

    private boolean removed;

    public void update(double deltaTime) {
        this.time = (float) ((double) this.time + deltaTime / 20.0);
    }

    public abstract void writeDataToBuffer(BiConsumer<Integer, Float> var1);

    public final void remove() {
        this.removed = true;
    }

    public final boolean isRemoved() {
        return this.removed;
    }

    public final float getTime() {
        return this.time;
    }
}