package net.mehvahdjukaar.amendments.integration.forge;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import java.lang.reflect.Method;
import net.mehvahdjukaar.amendments.common.block.LiquidCauldronBlock;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.misc.DataObjectReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class AlexCavesCompatImpl {

    public static DataObjectReference<SoftFluid> ACID = new DataObjectReference<>(new ResourceLocation("alexscaves:acid"), SoftFluidRegistry.KEY);

    public static final Method SET_H = ObfuscationReflectionHelper.findMethod(Entity.class, "setFluidTypeHeight", new Class[] { FluidType.class, double.class });

    public static void acidDamage(SoftFluidStack fluid, Level level, BlockPos pos, BlockState state, Entity entity) {
        if (fluid.is(ACID.get())) {
            try {
                FluidType acidFluid = (FluidType) ACFluidRegistry.ACID_FLUID_TYPE.get();
                double oldH = entity.getFluidTypeHeight(acidFluid);
                double stateH = (double) ((Integer) state.m_61143_(LiquidCauldronBlock.LEVEL)).intValue() * 0.25;
                SET_H.invoke(entity, acidFluid, stateH);
                LiquidBlock acid = (LiquidBlock) ACBlockRegistry.ACID.get();
                acid.m_49966_().m_60682_(level, pos, entity);
                SET_H.invoke(entity, acidFluid, oldH);
            } catch (Exception var11) {
            }
        }
    }

    public static void acidParticles(SoftFluidStack fluid, Level level, BlockPos pos, RandomSource rand, double height) {
        if (fluid.is(ACID.get())) {
            if (rand.nextInt(400) == 0) {
                level.playLocalSound((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, (SoundEvent) ACSoundRegistry.ACID_IDLE.get(), SoundSource.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
            }
            LiquidCauldronBlock.addSurfaceParticles((ParticleOptions) ACParticleRegistry.ACID_BUBBLE.get(), level, pos, 1, height, rand, (rand.nextFloat() - 0.5F) * 0.1F, 0.05F + rand.nextFloat() * 0.1F, (rand.nextFloat() - 0.5F) * 0.1F);
        }
    }

    static {
        SET_H.setAccessible(true);
    }
}