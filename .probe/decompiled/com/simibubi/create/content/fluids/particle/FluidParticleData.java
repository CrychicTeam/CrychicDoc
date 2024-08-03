package com.simibubi.create.content.fluids.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.foundation.particle.ICustomParticleData;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

public class FluidParticleData implements ParticleOptions, ICustomParticleData<FluidParticleData> {

    private ParticleType<FluidParticleData> type;

    private FluidStack fluid;

    public static final Codec<FluidParticleData> CODEC = RecordCodecBuilder.create(i -> i.group(FluidStack.CODEC.fieldOf("fluid").forGetter(p -> p.fluid)).apply(i, fs -> new FluidParticleData(AllParticleTypes.FLUID_PARTICLE.get(), fs)));

    public static final Codec<FluidParticleData> BASIN_CODEC = RecordCodecBuilder.create(i -> i.group(FluidStack.CODEC.fieldOf("fluid").forGetter(p -> p.fluid)).apply(i, fs -> new FluidParticleData(AllParticleTypes.BASIN_FLUID.get(), fs)));

    public static final Codec<FluidParticleData> DRIP_CODEC = RecordCodecBuilder.create(i -> i.group(FluidStack.CODEC.fieldOf("fluid").forGetter(p -> p.fluid)).apply(i, fs -> new FluidParticleData(AllParticleTypes.FLUID_DRIP.get(), fs)));

    public static final ParticleOptions.Deserializer<FluidParticleData> DESERIALIZER = new ParticleOptions.Deserializer<FluidParticleData>() {

        public FluidParticleData fromCommand(ParticleType<FluidParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            return new FluidParticleData(particleTypeIn, new FluidStack(Fluids.WATER, 1));
        }

        public FluidParticleData fromNetwork(ParticleType<FluidParticleData> particleTypeIn, FriendlyByteBuf buffer) {
            return new FluidParticleData(particleTypeIn, buffer.readFluidStack());
        }
    };

    public FluidParticleData() {
    }

    public FluidParticleData(ParticleType<?> type, FluidStack fluid) {
        this.type = (ParticleType<FluidParticleData>) type;
        this.fluid = fluid;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ParticleProvider<FluidParticleData> getFactory() {
        return (data, world, x, y, z, vx, vy, vz) -> FluidStackParticle.create(data.type, world, data.fluid, x, y, z, vx, vy, vz);
    }

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFluidStack(this.fluid);
    }

    @Override
    public String writeToString() {
        return RegisteredObjects.getKeyOrThrow(this.type) + " " + RegisteredObjects.getKeyOrThrow(this.fluid.getFluid());
    }

    @Override
    public ParticleOptions.Deserializer<FluidParticleData> getDeserializer() {
        return DESERIALIZER;
    }

    @Override
    public Codec<FluidParticleData> getCodec(ParticleType<FluidParticleData> type) {
        if (type == AllParticleTypes.BASIN_FLUID.get()) {
            return BASIN_CODEC;
        } else {
            return type == AllParticleTypes.FLUID_DRIP.get() ? DRIP_CODEC : CODEC;
        }
    }
}