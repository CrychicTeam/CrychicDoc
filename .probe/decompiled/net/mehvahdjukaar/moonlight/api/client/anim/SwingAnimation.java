package net.mehvahdjukaar.moonlight.api.client.anim;

import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public abstract class SwingAnimation {

    protected final Supplier<Vector3f> rotationAxis;

    protected float angle = 0.0F;

    protected float prevAngle = 0.0F;

    protected SwingAnimation(Supplier<Vector3f> axisGetter) {
        this.rotationAxis = axisGetter;
    }

    public abstract void tick(boolean var1);

    public abstract void addImpulse(float var1);

    public abstract void addPositiveImpulse(float var1);

    public abstract boolean hit(Vec3 var1, double var2);

    public abstract boolean hitByEntity(Entity var1);

    public abstract float getAngle(float var1);

    public abstract void reset();
}