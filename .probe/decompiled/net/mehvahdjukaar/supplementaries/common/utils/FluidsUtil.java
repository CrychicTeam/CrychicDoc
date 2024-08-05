package net.mehvahdjukaar.supplementaries.common.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.supplementaries.common.utils.forge.FluidsUtilImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Contract;

public class FluidsUtil {

    @Contract
    @ExpectPlatform
    @Transformed
    public static boolean extractFluidFromTank(BlockEntity tileBack, Direction dir, int amount) {
        return FluidsUtilImpl.extractFluidFromTank(tileBack, dir, amount);
    }

    @ExpectPlatform
    @Transformed
    public static Integer fillFluidTank(BlockEntity tileBelow, SoftFluidStack fluidStack, int minAmount) {
        return FluidsUtilImpl.fillFluidTank(tileBelow, fluidStack, minAmount);
    }

    @Contract
    @ExpectPlatform
    @Transformed
    public static boolean hasFluidHandler(Level level, BlockPos pos, Direction dir) {
        return FluidsUtilImpl.hasFluidHandler(level, pos, dir);
    }

    @Contract
    @ExpectPlatform
    @Transformed
    public static SoftFluidStack getFluidInTank(Level level, BlockPos pos, Direction dir, BlockEntity source) {
        return FluidsUtilImpl.getFluidInTank(level, pos, dir, source);
    }
}