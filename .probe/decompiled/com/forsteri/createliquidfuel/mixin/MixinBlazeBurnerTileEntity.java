package com.forsteri.createliquidfuel.mixin;

import com.forsteri.createliquidfuel.core.BurnerStomachHandler;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { BlazeBurnerBlockEntity.class }, remap = false)
public abstract class MixinBlazeBurnerTileEntity extends SmartBlockEntity {

    @Unique
    public SmartFluidTank createliquidfuel$stomach;

    public MixinBlazeBurnerTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return this.isFluidHandlerCap(cap) ? LazyOptional.<SmartFluidTank>of(() -> this.createliquidfuel$stomach).cast() : super.getCapability(cap, side);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.createliquidfuel$stomach = new SmartFluidTank(1000, contents -> {
        }) {

            @Override
            public boolean isFluidValid(@NotNull FluidStack stack) {
                return BurnerStomachHandler.LIQUID_BURNER_FUEL_MAP.containsKey(stack.getFluid());
            }
        };
    }

    @Inject(method = { "tick" }, at = { @At("TAIL") })
    public void tick(CallbackInfo info) {
        BurnerStomachHandler.tick(this);
    }

    @Inject(method = { "read" }, at = { @At("TAIL") })
    public void read(CompoundTag compound, boolean clientPacket, CallbackInfo ci) {
        if (this.createliquidfuel$stomach != null) {
            this.createliquidfuel$stomach.readFromNBT(compound.getCompound("Stomach"));
        }
    }

    @Inject(method = { "write" }, at = { @At("TAIL") })
    public void write(CompoundTag compound, boolean clientPacket, CallbackInfo ci) {
        if (this.createliquidfuel$stomach != null) {
            compound.put("Stomach", this.createliquidfuel$stomach.writeToNBT(new CompoundTag()));
        }
    }

    @Inject(method = { "tryUpdateFuel" }, at = { @At("HEAD") }, cancellable = true)
    public void tryUpdateFuel(ItemStack itemStack, boolean forceOverflow, boolean simulate, CallbackInfoReturnable<Boolean> cir) {
        BurnerStomachHandler.tryUpdateFuel(this, itemStack, forceOverflow, simulate, cir);
    }
}