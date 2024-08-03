package net.mehvahdjukaar.amendments.mixins.forge;

import net.mehvahdjukaar.amendments.common.tile.LiquidCauldronBlockTile;
import net.mehvahdjukaar.moonlight.api.block.ISoftFluidTankProvider;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.moonlight.api.fluids.forge.SoftFluidStackImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ LiquidCauldronBlockTile.class })
public abstract class SelfCauldronMixin extends BlockEntity implements IFluidHandler, ISoftFluidTankProvider {

    @Unique
    public final LazyOptional<IFluidHandler> amendments$cap = LazyOptional.of(() -> this);

    public SelfCauldronMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.FLUID_HANDLER ? ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, this.amendments$cap) : super.getCapability(cap, side);
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int i) {
        return SoftFluidStackImpl.toForgeFluid(this.getSoftFluidTank().getFluid());
    }

    @Override
    public int getTankCapacity(int i) {
        return SoftFluidStackImpl.bottlesToMB(this.getSoftFluidTank().getCapacity());
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return this.getSoftFluidTank().isFluidCompatible(SoftFluidStackImpl.fromForgeFluid(fluidStack));
    }

    @Override
    public int fill(FluidStack fluidStack, IFluidHandler.FluidAction fluidAction) {
        SoftFluidTank tank = this.getSoftFluidTank();
        SoftFluidStack original = SoftFluidStackImpl.fromForgeFluid(fluidStack);
        int filled = tank.addFluid(original, fluidAction.simulate());
        int bottlesRemoved = SoftFluidStackImpl.fromForgeFluid(fluidStack).getCount() - original.getCount();
        fluidStack.shrink(SoftFluidStackImpl.bottlesToMB(bottlesRemoved));
        this.m_6596_();
        return filled;
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack fluidStack, IFluidHandler.FluidAction fluidAction) {
        return this.drain(fluidStack.getAmount(), fluidAction);
    }

    @NotNull
    @Override
    public FluidStack drain(int i, IFluidHandler.FluidAction fluidAction) {
        SoftFluidTank tank = this.getSoftFluidTank();
        SoftFluidStack drained = tank.removeFluid(i, fluidAction.simulate());
        this.m_6596_();
        return SoftFluidStackImpl.toForgeFluid(drained);
    }
}