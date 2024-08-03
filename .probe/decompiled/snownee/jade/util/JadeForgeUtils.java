package snownee.jade.util;

import com.google.common.math.IntMath;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import snownee.jade.addon.universal.ItemIterator;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ViewGroup;

public class JadeForgeUtils {

    private JadeForgeUtils() {
    }

    @Deprecated
    public static ViewGroup<ItemStack> fromItemHandler(IItemHandler itemHandler, int maxSize, int startIndex) {
        return ItemView.compacted(IntStream.range(startIndex, itemHandler.getSlots()).limit((long) (maxSize * 3)).mapToObj(itemHandler::getStackInSlot), maxSize);
    }

    public static ItemIterator<? extends IItemHandler> fromItemHandler(IItemHandler storage, int fromIndex) {
        return fromItemHandler(storage, fromIndex, target -> target instanceof CapabilityProvider<?> capProvider ? capProvider.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null) : null);
    }

    public static ItemIterator<? extends IItemHandler> fromItemHandler(IItemHandler storage, int fromIndex, Function<Object, IItemHandler> containerFinder) {
        return new ItemIterator.SlottedItemIterator<IItemHandler>(containerFinder, fromIndex) {

            protected int getSlotCount(IItemHandler container) {
                return container.getSlots();
            }

            protected ItemStack getItemInSlot(IItemHandler container, int slot) {
                return container.getStackInSlot(slot);
            }
        };
    }

    public static CompoundTag fromFluidStack(FluidStack fluidStack, long capacity) {
        CompoundTag tag = new CompoundTag();
        if (capacity <= 0L) {
            return tag;
        } else {
            tag.putString("fluid", BuiltInRegistries.FLUID.getKey(fluidStack.getFluid()).toString());
            tag.putLong("amount", (long) fluidStack.getAmount());
            tag.putLong("capacity", capacity);
            if (fluidStack.getTag() != null) {
                tag.put("tag", fluidStack.getTag());
            }
            return tag;
        }
    }

    public static List<ViewGroup<CompoundTag>> fromFluidHandler(IFluidHandler fluidHandler) {
        List<CompoundTag> list = new ArrayList(fluidHandler.getTanks());
        int emptyCapacity = 0;
        for (int i = 0; i < fluidHandler.getTanks(); i++) {
            int capacity = fluidHandler.getTankCapacity(i);
            if (capacity > 0) {
                FluidStack fluidStack = fluidHandler.getFluidInTank(i);
                if (fluidStack.isEmpty()) {
                    emptyCapacity = IntMath.saturatedAdd(emptyCapacity, capacity);
                } else {
                    list.add(fromFluidStack(fluidStack, (long) capacity));
                }
            }
        }
        if (list.isEmpty() && emptyCapacity > 0) {
            list.add(fromFluidStack(FluidStack.EMPTY, (long) emptyCapacity));
        }
        return !list.isEmpty() ? List.of(new ViewGroup(list)) : List.of();
    }
}