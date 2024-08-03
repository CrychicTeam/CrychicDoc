package com.almostreliable.ponderjs.particles;

import com.almostreliable.ponderjs.mixin.ParticleAccessor;
import com.almostreliable.ponderjs.mixin.PonderWorldAccessor;
import com.almostreliable.ponderjs.util.PonderErrorHelper;
import com.almostreliable.ponderjs.util.PonderPlatform;
import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.content.fluids.particle.FluidParticleData;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.instruction.TickingInstruction;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.rhino.mod.util.color.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ParticleInstructions {

    private final SceneBuilder scene;

    public ParticleInstructions(SceneBuilder scene) {
        this.scene = scene;
    }

    public ParticleDataBuilder<?, ?> simple(int ticks, ParticleType<?> type, Vec3 pos) {
        if (type instanceof SimpleParticleType simple) {
            return this.create(ticks, pos, new ParticleDataBuilder.Static(simple));
        } else {
            throw new IllegalArgumentException("Particle type " + (type == null ? "INVALID" : PonderPlatform.getParticleTypeName(type)) + " is null or not simple.");
        }
    }

    public ParticleDataBuilder.DustParticleDataBuilder dust(int ticks, Color color, Vec3 pos) {
        return this.create(ticks, pos, new ParticleDataBuilder.DustParticleDataBuilder(color, null).color(color));
    }

    public ParticleDataBuilder.DustParticleDataBuilder dust(int ticks, Color fromColor, Color toColor, Vec3 pos) {
        return this.create(ticks, pos, new ParticleDataBuilder.DustParticleDataBuilder(fromColor, toColor).color(fromColor));
    }

    public ParticleDataBuilder.Static item(int ticks, ItemStack item, Vec3 pos) {
        ItemParticleOption options = new ItemParticleOption(ParticleTypes.ITEM, item);
        return this.create(ticks, pos, new ParticleDataBuilder.Static(options));
    }

    public ParticleDataBuilder.Static block(int ticks, BlockState blockState, Vec3 pos) {
        BlockParticleOption options = new BlockParticleOption(ParticleTypes.BLOCK, blockState);
        return this.create(ticks, pos, new ParticleDataBuilder.Static(options));
    }

    public ParticleDataBuilder<?, ?> fluid(int ticks, FluidStackJS fluid, Vec3 pos) {
        FluidParticleData data = PonderPlatform.createFluidParticleData(fluid, AllParticleTypes.FLUID_PARTICLE.get());
        return this.create(ticks, pos, new ParticleDataBuilder.Static(data));
    }

    public ParticleDataBuilder<?, ?> drip(int ticks, FluidStackJS fluid, Vec3 pos) {
        FluidParticleData data = PonderPlatform.createFluidParticleData(fluid, AllParticleTypes.FLUID_DRIP.get());
        return this.create(ticks, pos, new ParticleDataBuilder.Static(data));
    }

    public ParticleDataBuilder<?, ?> basin(int ticks, FluidStackJS fluid, Vec3 pos) {
        FluidParticleData data = PonderPlatform.createFluidParticleData(fluid, AllParticleTypes.BASIN_FLUID.get());
        return this.create(ticks, pos, new ParticleDataBuilder.Static(data));
    }

    public ParticleDataBuilder<?, ?> rotationIndicator(int ticks, Vec3 pos, float radius1, float radius2, Direction.Axis axis) {
        return this.create(ticks, pos, new ParticleDataBuilder.RotationIndicatorParticleDataBuilder(radius1, radius2, axis));
    }

    private <O extends ParticleDataBuilder<O, ?>> O create(int ticks, Vec3 origin, O options) {
        this.scene.addInstruction(new ParticleInstructions.ParticleInstruction(ticks, origin, options));
        return options;
    }

    public static class ParticleInstruction extends TickingInstruction {

        private final ParticleDataBuilder<?, ?> builder;

        private final Vec3 origin;

        private final List<ParticleTransformation> transformations = new ArrayList();

        private ParticleOptions cachedOptions;

        public ParticleInstruction(int ticks, Vec3 origin, ParticleDataBuilder<?, ?> builder) {
            super(false, ticks);
            this.origin = origin;
            this.builder = builder;
        }

        @Override
        protected void firstTick(PonderScene scene) {
            this.cachedOptions = this.builder.createOptions();
            this.transformations.clear();
            this.transformations.addAll(this.builder.transformations);
        }

        @Override
        public void tick(PonderScene scene) {
            try {
                super.tick(scene);
                this.doTick(scene);
            } catch (Exception var3) {
                PonderErrorHelper.yeet(var3);
                this.remainingTicks = 0;
            }
        }

        private void doTick(PonderScene scene) {
            int currentTick = this.totalTicks - this.remainingTicks;
            for (int i = 0; i < this.builder.density; i++) {
                ParticleTransformation.Data data = new ParticleTransformation.Data(this.origin, Vec3.ZERO);
                for (ParticleTransformation transformation : this.transformations) {
                    float partialTicks = (float) currentTick + (float) i / (float) this.builder.density;
                    data = transformation.apply(partialTicks, data.position(), data.motion());
                }
                Vec3 pos = data.position();
                Vec3 motion = data.motion();
                Particle particle = ((PonderWorldAccessor) scene.getWorld()).ponderjs$makeParticle(this.cachedOptions, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
                if (particle != null) {
                    this.applyParticleData(particle);
                    scene.getWorld().addParticle(particle);
                }
            }
        }

        private void applyParticleData(Particle particle) {
            if (particle instanceof ParticleAccessor accessor) {
                if (this.builder.color != null) {
                    long argb = (long) this.builder.color.getArgbJS();
                    float a = (float) (argb >> 24 & 255L) / 255.0F;
                    float r = (float) (argb >> 16 & 255L) / 255.0F;
                    float g = (float) (argb >> 8 & 255L) / 255.0F;
                    float b = (float) (argb & 255L) / 255.0F;
                    particle.setColor(r, g, b);
                    accessor.ponderjs$setAlpha(a);
                }
                if (this.builder.scale != null) {
                    particle.scale(this.builder.scale);
                }
                if (this.builder.roll != null) {
                    accessor.ponderjs$setRoll(this.builder.roll);
                }
                if (this.builder.friction != null) {
                    accessor.ponderjs$setFriction(this.builder.friction);
                }
                if (this.builder.gravity != null) {
                    accessor.ponderjs$setGravity(this.builder.gravity);
                }
                if (this.builder.physics != null) {
                    accessor.ponderjs$setHasPhysics(this.builder.physics);
                }
                if (this.builder.collision != null) {
                    accessor.ponderjs$setStoppedByCollision(this.builder.collision);
                }
                if (this.builder.lifetime != null) {
                    accessor.ponderjs$setLifetime(this.builder.lifetime);
                }
            }
        }
    }
}