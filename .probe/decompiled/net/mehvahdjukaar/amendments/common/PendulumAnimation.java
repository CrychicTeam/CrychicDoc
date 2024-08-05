package net.mehvahdjukaar.amendments.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import java.util.function.Supplier;
import net.mehvahdjukaar.amendments.configs.ClientConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class PendulumAnimation extends SwingAnimation {

    private static final RandomSource RAND = RandomSource.create();

    private final Supplier<PendulumAnimation.Config> config;

    private float angularVel;

    private boolean hasDrag = true;

    private float lastImpulse;

    private int immunity = 0;

    public PendulumAnimation(Supplier<PendulumAnimation.Config> config, Function<BlockState, Vector3f> axisGetter) {
        super(axisGetter);
        this.config = config;
        PendulumAnimation.Config c = (PendulumAnimation.Config) config.get();
        if (c != null) {
            this.angle = (RAND.nextFloat() - 0.5F) * c.minAngle * 2.0F;
            this.angularVel = capVelocity(c.k, 1000.0F, this.angle, c.minAngleEnergy);
        }
    }

    @Override
    public float getAngle(float partialTicks) {
        return (180.0F / (float) Math.PI) * Mth.lerp(partialTicks, this.prevAngle, this.angle);
    }

    @Override
    public void reset() {
        this.angle = 0.0F;
        this.prevAngle = 0.0F;
        PendulumAnimation.Config c = (PendulumAnimation.Config) this.config.get();
        this.angularVel = capVelocity(c.k, 1000.0F, this.angle, c.minAngleEnergy);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        this.prevAngle = this.angle;
        if (this.immunity > 0) {
            this.immunity--;
        }
        float dt = 0.05F;
        float energy = 0.0F;
        PendulumAnimation.Config config = (PendulumAnimation.Config) this.config.get();
        float k = config.k;
        boolean hasAcc = this.lastImpulse != 0.0F;
        if (hasAcc) {
            this.hasDrag = true;
        }
        if (this.hasDrag) {
            energy = calculateEnergy(k, this.angularVel, this.angle);
        }
        if (hasAcc && energy < config.maxAngleEnergy || this.lastImpulse * this.angularVel < 0.0F) {
            this.angularVel = this.angularVel + this.lastImpulse;
            if (calculateEnergy(k, this.angularVel, this.angle) > config.maxAngleEnergy) {
                this.angularVel = 0.1F * this.angularVel + 0.9F * capVelocity(k, this.angularVel, this.angle, config.maxAngleEnergy);
            }
        }
        this.lastImpulse = 0.0F;
        float acc = -k * Mth.sin(this.angle);
        if (this.hasDrag && !hasAcc) {
            if (energy > config.minAngleEnergy) {
                double damping = (double) config.damping;
                float drag = (float) (damping * (double) this.angularVel);
                acc -= drag;
            } else {
                this.hasDrag = false;
            }
        }
        this.angularVel += dt * acc;
        this.angle = this.angle + this.angularVel * dt;
    }

    public void addImpulse(double vel) {
        this.angularVel = (float) ((double) this.angularVel + vel);
        this.hasDrag = true;
    }

    private static float capVelocity(float k, float currentVel, float angle, float targetEnergy) {
        float newVel = (float) Math.sqrt((double) Math.max(0.0F, 2.0F * (targetEnergy - k * (1.0F - Mth.cos(angle)))));
        if (currentVel < 0.0F) {
            newVel *= -1.0F;
        }
        return newVel;
    }

    private static float calculateEnergy(float k, float vel, float radAngle) {
        return angleToEnergy(k, radAngle) + 0.5F * vel * vel;
    }

    private static float angleToEnergy(float k, float radAngle) {
        return k * (1.0F - Mth.cos(radAngle));
    }

    @Override
    public boolean hitByEntity(Entity entity, BlockState state, BlockPos pos) {
        if (this.immunity != 0) {
            return true;
        } else {
            Vec3 eVel = entity.getDeltaMovement();
            if (eVel.length() < 0.01) {
                return false;
            } else {
                PendulumAnimation.Config config = (PendulumAnimation.Config) ClientConfigs.HANGING_SIGN_CONFIG.get();
                double eMass;
                if (config.considerEntityHitbox) {
                    AABB boundingBox = entity.getBoundingBox();
                    eMass = boundingBox.getXsize() * boundingBox.getYsize() * boundingBox.getZsize();
                } else {
                    eMass = 1.0;
                }
                eMass *= (double) config.collisionInertia;
                eVel = eVel.scale((double) config.collisionForce);
                Vec3 rotationAxis = new Vec3(this.getRotationAxis(state));
                Vec3 normalVec = rotationAxis.cross(new Vec3(0.0, 1.0, 0.0));
                Vec3 entityPlaneVector = eVel.subtract(eVel.multiply(rotationAxis.multiply(rotationAxis)));
                float radius = 1.0F;
                double magnitude = (double) (this.angularVel * radius);
                if (magnitude == 0.0) {
                    magnitude = 1.0E-5;
                }
                Vec3 signVel = new Vec3(0.0, (double) Mth.sin(this.angle), 0.0).add(normalVec.scale((double) Mth.cos(this.angle)));
                double eRelVel = eVel.dot(signVel.scale(1000000.0).normalize());
                if (eRelVel * eRelVel < 1.0E-4) {
                    return false;
                } else {
                    double entityForwardMotion;
                    if (normalVec.z != 0.0) {
                        entityForwardMotion = entityPlaneVector.z;
                    } else {
                        entityForwardMotion = entityPlaneVector.x;
                    }
                    double f = eMass * eMass + eMass;
                    double g = 2.0 * eMass * (-magnitude - eMass * eRelVel);
                    double h = eMass * eMass * eRelVel * eRelVel - eMass * eRelVel * eRelVel + 2.0 * magnitude * eMass * eRelVel;
                    float delta = Mth.sqrt((float) (g * g - 4.0 * f * h));
                    double y1 = (-g + (double) delta) / (2.0 * f);
                    double y2 = (-g - (double) delta) / (2.0 * f);
                    double x1 = magnitude + eMass * eRelVel - eMass * y1;
                    double x2 = magnitude + eMass * eRelVel - eMass * y2;
                    double x;
                    if ((double) Mth.abs((float) (x2 - magnitude)) < 1.0E-4) {
                        x = x1;
                    } else {
                        x = x2;
                    }
                    float dW = (float) (x / (double) radius) - this.angularVel;
                    if (eRelVel < 0.0 ^ entityForwardMotion < 0.0) {
                        dW *= -1.0F;
                    }
                    boolean invertedAxis = normalVec.z < 0.0 || normalVec.x < 0.0;
                    if (invertedAxis) {
                        dW *= -1.0F;
                    }
                    this.lastImpulse = dW;
                    this.immunity = 10;
                    entity.level().playLocalSound(pos, state.m_60827_().getPlaceSound(), SoundSource.BLOCKS, 0.75F, 1.5F, false);
                    return true;
                }
            }
        }
    }

    public static class Config {

        public static final Codec<PendulumAnimation.Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.floatRange(0.0F, 360.0F).fieldOf("min_angle").forGetter(c -> (180.0F / (float) Math.PI) * c.minAngle), Codec.floatRange(0.0F, 360.0F).fieldOf("max_angle").forGetter(c -> (180.0F / (float) Math.PI) * c.maxAngle), Codec.FLOAT.fieldOf("damping").forGetter(c -> c.damping), Codec.FLOAT.fieldOf("frequency").forGetter(c -> c.frequency), Codec.BOOL.fieldOf("collision_considers_entity_hitbox").forGetter(c -> c.considerEntityHitbox), ExtraCodecs.POSITIVE_FLOAT.fieldOf("collision_inertia").forGetter(c -> c.collisionInertia), ExtraCodecs.POSITIVE_FLOAT.fieldOf("collision_force").forGetter(c -> c.collisionForce)).apply(instance, PendulumAnimation.Config::new));

        protected final float minAngle;

        protected final float maxAngle;

        protected final float damping;

        protected final float frequency;

        protected final float maxAngleEnergy;

        protected final float minAngleEnergy;

        protected final float k;

        protected final boolean considerEntityHitbox;

        protected final float collisionInertia;

        protected final float collisionForce;

        public Config(float minAngle, float maxAngle, float damping, float frequency, boolean hitbox, float mass, float force) {
            this.minAngle = minAngle * (float) (Math.PI / 180.0);
            this.maxAngle = maxAngle * (float) (Math.PI / 180.0);
            this.damping = damping;
            this.frequency = frequency;
            this.k = (float) Math.pow((Math.PI * 2) * (double) frequency, 2.0);
            this.maxAngleEnergy = PendulumAnimation.angleToEnergy(this.k, this.maxAngle);
            this.minAngleEnergy = PendulumAnimation.angleToEnergy(this.k, this.minAngle);
            this.considerEntityHitbox = hitbox;
            this.collisionInertia = mass;
            this.collisionForce = force;
        }

        public Config() {
            this(0.8F, 60.0F, 0.525F, 0.6F, true, 1.0F, 15.0F);
        }
    }
}