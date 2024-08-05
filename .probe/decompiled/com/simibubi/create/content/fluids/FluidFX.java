package com.simibubi.create.content.fluids;

import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.content.fluids.particle.FluidParticleData;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

public class FluidFX {

    static RandomSource r = RandomSource.create();

    public static void splash(BlockPos pos, FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        if (fluid != Fluids.EMPTY) {
            FluidState defaultState = fluid.defaultFluidState();
            if (defaultState != null && !defaultState.isEmpty()) {
                BlockParticleOption blockParticleData = new BlockParticleOption(ParticleTypes.BLOCK, defaultState.createLegacyBlock());
                Vec3 center = VecHelper.getCenterOf(pos);
                for (int i = 0; i < 20; i++) {
                    Vec3 v = VecHelper.offsetRandomly(Vec3.ZERO, r, 0.25F);
                    particle(blockParticleData, center.add(v), v);
                }
            }
        }
    }

    public static ParticleOptions getFluidParticle(FluidStack fluid) {
        return new FluidParticleData(AllParticleTypes.FLUID_PARTICLE.get(), fluid);
    }

    public static ParticleOptions getDrippingParticle(FluidStack fluid) {
        ParticleOptions particle = null;
        if (FluidHelper.isWater(fluid.getFluid())) {
            particle = ParticleTypes.DRIPPING_WATER;
        }
        if (FluidHelper.isLava(fluid.getFluid())) {
            particle = ParticleTypes.DRIPPING_LAVA;
        }
        if (particle == null) {
            particle = new FluidParticleData(AllParticleTypes.FLUID_DRIP.get(), fluid);
        }
        return particle;
    }

    public static void spawnRimParticles(Level world, BlockPos pos, Direction side, int amount, ParticleOptions particle, float rimRadius) {
        Vec3 directionVec = Vec3.atLowerCornerOf(side.getNormal());
        for (int i = 0; i < amount; i++) {
            Vec3 vec = VecHelper.offsetRandomly(Vec3.ZERO, r, 1.0F).normalize();
            vec = VecHelper.clampComponentWise(vec, rimRadius).multiply(VecHelper.axisAlingedPlaneOf(directionVec)).add(directionVec.scale(0.45 + (double) (r.nextFloat() / 16.0F)));
            Vec3 m = vec.scale(0.05F);
            vec = vec.add(VecHelper.getCenterOf(pos));
            world.addAlwaysVisibleParticle(particle, vec.x, vec.y - 0.0625, vec.z, m.x, m.y, m.z);
        }
    }

    public static void spawnPouringLiquid(Level world, BlockPos pos, int amount, ParticleOptions particle, float rimRadius, Vec3 directionVec, boolean inbound) {
        for (int i = 0; i < amount; i++) {
            Vec3 vec = VecHelper.offsetRandomly(Vec3.ZERO, r, rimRadius * 0.75F);
            vec = vec.multiply(VecHelper.axisAlingedPlaneOf(directionVec)).add(directionVec.scale(0.5 + (double) (r.nextFloat() / 4.0F)));
            Vec3 m = vec.scale(0.25);
            Vec3 centerOf = VecHelper.getCenterOf(pos);
            vec = vec.add(centerOf);
            if (inbound) {
                vec = vec.add(m);
                m = centerOf.add(directionVec.scale(0.5)).subtract(vec).scale(0.0625);
            }
            world.addAlwaysVisibleParticle(particle, vec.x, vec.y - 0.0625, vec.z, m.x, m.y, m.z);
        }
    }

    private static void particle(ParticleOptions data, Vec3 pos, Vec3 motion) {
        world().addParticle(data, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
    }

    private static Level world() {
        return Minecraft.getInstance().level;
    }
}