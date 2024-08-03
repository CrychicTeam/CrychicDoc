package net.mehvahdjukaar.moonlight.api.client.anim;

import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class SwayingAnimation extends SwingAnimation {

    protected float maxSwingAngle = 45.0F;

    protected float minSwingAngle = 2.5F;

    protected float maxPeriod = 25.0F;

    protected float angleDamping = 150.0F;

    protected float periodDamping = 100.0F;

    private int animationCounter = 800 + new Random().nextInt(80);

    private boolean inv = false;

    public SwayingAnimation(Supplier<Vector3f> getRotationAxis) {
        super(getRotationAxis);
    }

    @Override
    public void tick(boolean inWater) {
        this.animationCounter++;
        double timer = (double) this.animationCounter;
        if (inWater) {
            timer /= 2.0;
        }
        this.prevAngle = this.angle;
        float a = this.minSwingAngle;
        float k = 0.01F;
        if (timer < 800.0) {
            a = (float) Math.max((double) this.maxSwingAngle * Math.exp(-(timer / (double) this.angleDamping)), (double) this.minSwingAngle);
            k = (float) Math.max((Math.PI * 2) * (double) ((float) Math.exp(-(timer / (double) this.periodDamping))), 0.01F);
        }
        this.angle = a * Mth.cos((float) (timer / (double) this.maxPeriod - (double) k));
        this.angle = this.angle * (this.inv ? -1.0F : 1.0F);
    }

    @Override
    public void addImpulse(float vel) {
    }

    @Override
    public void addPositiveImpulse(float vel) {
    }

    @Override
    public float getAngle(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevAngle, this.angle);
    }

    @Override
    public void reset() {
        this.animationCounter = 800;
    }

    @Override
    public boolean hit(Vec3 mot, double eMass) {
        if (mot.length() > 0.05) {
            Vec3 norm = new Vec3(mot.x, 0.0, mot.z).normalize();
            Vec3 vec = new Vec3((Vector3f) this.rotationAxis.get());
            double dot = norm.dot(vec);
            if (dot != 0.0) {
                this.inv = dot < 0.0;
            }
            if (Math.abs(dot) > 0.4) {
                if (this.animationCounter > 10) {
                    this.animationCounter = 0;
                    return true;
                }
                this.animationCounter = 0;
            }
        }
        return false;
    }

    @Override
    public boolean hitByEntity(Entity entity) {
        Vec3 mot = entity.getDeltaMovement();
        return this.hit(mot, 1.0);
    }
}