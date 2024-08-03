package net.mehvahdjukaar.amendments.integration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.amendments.integration.forge.AlexCavesCompatImpl;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AlexCavesCompat {

    @ExpectPlatform
    @Transformed
    public static void acidDamage(SoftFluidStack fluid, Level level, BlockPos pos, BlockState state, Entity entity) {
        AlexCavesCompatImpl.acidDamage(fluid, level, pos, state, entity);
    }

    @ExpectPlatform
    @Transformed
    public static void acidParticles(SoftFluidStack fluid, Level level, BlockPos pos, RandomSource rand, double height) {
        AlexCavesCompatImpl.acidParticles(fluid, level, pos, rand, height);
    }
}