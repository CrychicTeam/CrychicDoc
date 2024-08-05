package net.mehvahdjukaar.supplementaries.common.utils.forge;

import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.fluids.forge.SoftFluidStackImpl;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Contract;

public class FluidsUtilImpl {

    public static boolean extractFluidFromTank(BlockEntity tileBack, Direction dir, int amount) {
        IFluidHandler handlerBack = (IFluidHandler) tileBack.getCapability(ForgeCapabilities.FLUID_HANDLER, dir).orElse(null);
        if (handlerBack != null) {
            if (handlerBack.drain(250 * amount, IFluidHandler.FluidAction.SIMULATE).getAmount() != 250 * amount) {
                return false;
            } else {
                handlerBack.drain(250 * amount, IFluidHandler.FluidAction.EXECUTE);
                tileBack.setChanged();
                return true;
            }
        } else {
            return false;
        }
    }

    public static Integer fillFluidTank(BlockEntity tileBelow, SoftFluidStack fluid, int minAmount) {
        IFluidHandler handlerDown = (IFluidHandler) tileBelow.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.UP).orElse(null);
        if (handlerDown != null && fluid instanceof SoftFluidStackImpl impl) {
            FluidStack stack = impl.toForgeFluid();
            if (!stack.isEmpty()) {
                stack.setAmount(250 * minAmount);
                if (stack.isEmpty()) {
                    return null;
                }
                int filled = handlerDown.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                tileBelow.setChanged();
                return Mth.ceil((float) filled / 250.0F);
            }
        }
        return null;
    }

    public static boolean hasFluidHandler(Level level, BlockPos pos, Direction dir) {
        return FluidUtil.getFluidHandler(level, pos, dir).isPresent();
    }

    @Contract
    public static SoftFluidStack getFluidInTank(Level level, BlockPos pos, Direction dir, BlockEntity source) {
        LazyOptional<IFluidHandler> opt = FluidUtil.getFluidHandler(level, pos, dir);
        if (opt.isPresent()) {
            FluidStack fluidInTank = ((IFluidHandler) opt.resolve().get()).drain(1000, IFluidHandler.FluidAction.SIMULATE);
            if (!fluidInTank.isEmpty() && !Utils.getID(source.getBlockState().m_60734_()).getPath().equals("fluid_interface")) {
                return SoftFluidStackImpl.fromForgeFluid(fluidInTank);
            }
        }
        return SoftFluidStack.empty();
    }
}