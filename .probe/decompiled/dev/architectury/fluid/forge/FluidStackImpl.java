package dev.architectury.fluid.forge;

import dev.architectury.fluid.FluidStack;
import dev.architectury.utils.Amount;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public enum FluidStackImpl implements FluidStack.FluidStackAdapter<net.minecraftforge.fluids.FluidStack> {

    INSTANCE;

    public static Function<FluidStack, Object> toValue;

    public static Function<Object, FluidStack> fromValue;

    public static FluidStack.FluidStackAdapter<Object> adapt(Function<FluidStack, Object> toValue, Function<Object, FluidStack> fromValue) {
        FluidStackImpl.toValue = toValue;
        FluidStackImpl.fromValue = fromValue;
        return INSTANCE;
    }

    public net.minecraftforge.fluids.FluidStack create(Supplier<Fluid> fluid, long amount, CompoundTag tag) {
        return new net.minecraftforge.fluids.FluidStack((Fluid) fluid.get(), Amount.toInt(amount), tag);
    }

    public Supplier<Fluid> getRawFluidSupplier(net.minecraftforge.fluids.FluidStack object) {
        return ForgeRegistries.FLUIDS.getDelegateOrThrow(object.getRawFluid());
    }

    public Fluid getFluid(net.minecraftforge.fluids.FluidStack object) {
        return object.getFluid();
    }

    public long getAmount(net.minecraftforge.fluids.FluidStack object) {
        return (long) object.getAmount();
    }

    public void setAmount(net.minecraftforge.fluids.FluidStack object, long amount) {
        object.setAmount(Amount.toInt(amount));
    }

    public CompoundTag getTag(net.minecraftforge.fluids.FluidStack value) {
        return value.getTag();
    }

    public void setTag(net.minecraftforge.fluids.FluidStack value, CompoundTag tag) {
        value.setTag(tag);
    }

    public net.minecraftforge.fluids.FluidStack copy(net.minecraftforge.fluids.FluidStack value) {
        return value.copy();
    }

    public int hashCode(net.minecraftforge.fluids.FluidStack value) {
        int code = 1;
        code = 31 * code + value.getFluid().hashCode();
        code = 31 * code + value.getAmount();
        CompoundTag tag = value.getTag();
        if (tag != null) {
            code = 31 * code + tag.hashCode();
        }
        return code;
    }

    static {
        FluidStack.init();
    }
}