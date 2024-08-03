package net.mehvahdjukaar.moonlight.api.fluids.forge;

import java.util.List;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class SoftFluidStackImpl extends SoftFluidStack {

    public SoftFluidStackImpl(Holder<SoftFluid> fluid, int count, CompoundTag tag) {
        super(fluid, count, tag);
    }

    public static SoftFluidStack of(Holder<SoftFluid> fluid, int count, @Nullable CompoundTag tag) {
        return new SoftFluidStackImpl(fluid, count, tag);
    }

    public boolean isFluidEqual(FluidStack fluidStack) {
        return this.isFluidEqual(fromForgeFluid(fluidStack));
    }

    public static FluidStack toForgeFluid(SoftFluidStack softFluid) {
        FluidStack stack = new FluidStack(softFluid.fluid().getVanillaFluid(), bottlesToMB(softFluid.getCount()));
        List<String> nbtKey = softFluid.fluid().getNbtKeyFromItem();
        CompoundTag tag = softFluid.getTag();
        if (tag != null && !tag.isEmpty() && !stack.isEmpty() && nbtKey != null) {
            CompoundTag newCom = new CompoundTag();
            for (String k : nbtKey) {
                if (!k.equals("Bottle") || !Utils.getID(stack.getFluid()).getNamespace().equals("immersiveengineering")) {
                    Tag c = tag.get(k);
                    if (c != null) {
                        newCom.put(k, c);
                    }
                }
            }
            if (!newCom.isEmpty()) {
                stack.setTag(newCom);
            }
        }
        return stack;
    }

    public FluidStack toForgeFluid() {
        return toForgeFluid(this);
    }

    public static SoftFluidStack fromForgeFluid(FluidStack fluidStack) {
        int amount = MBtoBottles(fluidStack.getAmount());
        return SoftFluidStack.fromFluid(fluidStack.getFluid(), amount, fluidStack.hasTag() ? fluidStack.getTag().copy() : null);
    }

    public static int bottlesToMB(int bottles) {
        return bottles * 250;
    }

    public static int MBtoBottles(int milliBuckets) {
        return (int) ((float) milliBuckets / 250.0F);
    }
}