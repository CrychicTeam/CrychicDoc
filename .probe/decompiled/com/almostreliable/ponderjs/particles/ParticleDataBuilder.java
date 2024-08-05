package com.almostreliable.ponderjs.particles;

import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.base.RotationIndicatorParticleData;
import dev.latvian.mods.rhino.mod.util.color.Color;
import dev.latvian.mods.rhino.mod.wrapper.ColorWrapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public abstract class ParticleDataBuilder<O extends ParticleDataBuilder<O, PO>, PO extends ParticleOptions> {

    final List<ParticleTransformation> transformations = new ArrayList();

    int density = 1;

    @Nullable
    Float gravity = null;

    @Nullable
    Boolean physics = null;

    @Nullable
    Boolean collision = null;

    @Nullable
    Color color = null;

    @Nullable
    Float roll = null;

    @Nullable
    Float friction = null;

    @Nullable
    Float scale = null;

    @Nullable
    Integer lifetime = null;

    public O density(int density) {
        this.density = density;
        return this.getSelf();
    }

    public O gravity(float gravity) {
        this.gravity = gravity;
        return this.getSelf();
    }

    public O physics(boolean physics) {
        this.physics = physics;
        return this.getSelf();
    }

    public O collision(boolean collision) {
        this.collision = collision;
        return this.getSelf();
    }

    public O color(Color color) {
        this.color = color;
        return this.getSelf();
    }

    public O roll(float roll) {
        this.roll = roll;
        return this.getSelf();
    }

    public O friction(float friction) {
        this.friction = friction;
        return this.getSelf();
    }

    public O scale(float scale) {
        this.scale = scale;
        return this.getSelf();
    }

    public O lifetime(int lifetime) {
        this.lifetime = lifetime;
        return this.getSelf();
    }

    public O motion(Vec3 motion) {
        return this.transformMotion((partialTicks, m) -> motion);
    }

    public O speed(Vec3 speed) {
        return this.transformMotion((partialTick, motion) -> new Vec3(Create.RANDOM.nextGaussian() * speed.x, Create.RANDOM.nextGaussian() * speed.y, Create.RANDOM.nextGaussian() * speed.z));
    }

    public O withinBlockSpace() {
        return this.transformPosition((partialTicks, position) -> new Vec3(Math.floor(position.x) + (double) Create.RANDOM.nextFloat(), Math.floor(position.y) + (double) Create.RANDOM.nextFloat(), Math.floor(position.z) + (double) Create.RANDOM.nextFloat()));
    }

    public O area(Vec3 area) {
        return this.transformPosition((partialTicks, position) -> new Vec3(position.x + (double) Create.RANDOM.nextFloat() * (area.x - position.x), position.y + (double) Create.RANDOM.nextFloat() * (area.y - position.y), position.z + (double) Create.RANDOM.nextFloat() * (area.z - position.z)));
    }

    public O delta(Vec3 delta) {
        return this.transformPosition((partialTicks, position) -> new Vec3(position.x + Create.RANDOM.nextGaussian() * delta.x, position.y + Create.RANDOM.nextGaussian() * delta.y, position.z + Create.RANDOM.nextGaussian() * delta.z));
    }

    public O transform(ParticleTransformation transformer) {
        this.transformations.add(transformer);
        return this.getSelf();
    }

    public O transformPosition(ParticleTransformation.Simple transformer) {
        return this.transform(ParticleTransformation.onlyPosition(transformer));
    }

    public O transformMotion(ParticleTransformation.Simple transformer) {
        return this.transform(ParticleTransformation.onlyMotion(transformer));
    }

    abstract PO createOptions();

    protected O getSelf() {
        return (O) this;
    }

    public static class DustParticleDataBuilder extends ParticleDataBuilder<ParticleDataBuilder.DustParticleDataBuilder, DustParticleOptionsBase> {

        final Color fromColor;

        @Nullable
        final Color toColor;

        public DustParticleDataBuilder(Color fromColor, @Nullable Color toColor) {
            this.fromColor = fromColor;
            this.toColor = toColor;
        }

        public ParticleDataBuilder.DustParticleDataBuilder color(Color color) {
            return this;
        }

        DustParticleOptionsBase createOptions() {
            float s = this.scale == null ? 1.0F : this.scale;
            Vector3f fC = new com.simibubi.create.foundation.utility.Color(this.fromColor.getRgbJS()).asVectorF();
            if (this.toColor == null) {
                return new DustParticleOptions(fC, s);
            } else {
                Vector3f toC = new com.simibubi.create.foundation.utility.Color(this.toColor.getRgbJS()).asVectorF();
                return new DustColorTransitionOptions(fC, toC, s);
            }
        }
    }

    public static class RotationIndicatorParticleDataBuilder extends ParticleDataBuilder<ParticleDataBuilder.RotationIndicatorParticleDataBuilder, RotationIndicatorParticleData> {

        private final float radius1;

        private final float radius2;

        private final Direction.Axis axis;

        private float rotationSpeed = 1.0F;

        public RotationIndicatorParticleDataBuilder(float radius1, float radius2, Direction.Axis axis) {
            this.radius1 = radius1;
            this.radius2 = radius2;
            this.axis = axis;
        }

        public ParticleDataBuilder.RotationIndicatorParticleDataBuilder rotationSpeed(float rotationSpeed) {
            this.rotationSpeed = rotationSpeed;
            return (ParticleDataBuilder.RotationIndicatorParticleDataBuilder) this.getSelf();
        }

        RotationIndicatorParticleData createOptions() {
            Color c = this.color == null ? ColorWrapper.BLACK : this.color;
            char axisChar = this.axis.name().charAt(0);
            int lTime = this.lifetime == null ? 40 : this.lifetime;
            return new RotationIndicatorParticleData(c.getRgbJS(), this.rotationSpeed, this.radius1, this.radius2, lTime, axisChar);
        }
    }

    public static class Static extends ParticleDataBuilder<ParticleDataBuilder.Static, ParticleOptions> {

        private final ParticleOptions type;

        public Static(ParticleOptions type) {
            this.type = type;
        }

        @Override
        ParticleOptions createOptions() {
            return this.type;
        }
    }
}