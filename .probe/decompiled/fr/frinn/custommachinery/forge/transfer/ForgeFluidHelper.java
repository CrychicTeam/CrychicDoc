package fr.frinn.custommachinery.forge.transfer;

import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import fr.frinn.custommachinery.common.component.FluidMachineComponent;
import fr.frinn.custommachinery.common.component.ItemMachineComponent;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.common.util.transfer.IFluidHelper;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgeFluidHelper implements IFluidHelper {

    @Override
    public boolean isFluidHandler(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
    }

    @Override
    public void fillTanksFromStack(List<FluidMachineComponent> tanks, ItemMachineComponent slot) {
        ItemStack stack = slot.getItemStack();
        if (!stack.isEmpty()) {
            IFluidHandlerItem handlerItem = (IFluidHandlerItem) stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElseThrow(() -> new IllegalStateException("Can't fill tanks from non fluid handler item: " + ForgeRegistries.ITEMS.getKey(stack.getItem())));
            for (FluidMachineComponent component : tanks) {
                FluidStack maxExtract;
                if (component.getFluidStack().isEmpty()) {
                    maxExtract = handlerItem.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                } else {
                    maxExtract = handlerItem.drain(new FluidStack(component.getFluidStack().getFluid(), Integer.MAX_VALUE, component.getFluidStack().getTag()), IFluidHandler.FluidAction.SIMULATE);
                }
                if (!maxExtract.isEmpty()) {
                    long maxInsert = component.insert(maxExtract.getFluid(), (long) maxExtract.getAmount(), maxExtract.getTag(), true);
                    if (maxInsert > 0L) {
                        FluidStack extracted = handlerItem.drain(new FluidStack(maxExtract.getFluid(), Utils.toInt(maxInsert), maxExtract.getTag()), IFluidHandler.FluidAction.EXECUTE);
                        if (extracted.getAmount() > 0) {
                            component.insert(extracted.getFluid(), (long) extracted.getAmount(), extracted.getTag(), false);
                        }
                    }
                }
            }
            slot.setItemStack(handlerItem.getContainer());
        }
    }

    @Override
    public void fillStackFromTanks(ItemMachineComponent slot, List<FluidMachineComponent> tanks) {
        ItemStack stack = slot.getItemStack();
        if (!stack.isEmpty()) {
            IFluidHandlerItem handlerItem = (IFluidHandlerItem) stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElseThrow(() -> new IllegalStateException("Can't fill tanks from non fluid handler item: " + ForgeRegistries.ITEMS.getKey(stack.getItem())));
            for (FluidMachineComponent component : tanks) {
                for (int i = 0; i < handlerItem.getTanks(); i++) {
                    if (handlerItem.getFluidInTank(i).isEmpty() || handlerItem.getFluidInTank(i).isFluidEqual(FluidStackHooksForge.toForge(component.getFluidStack()))) {
                        FluidStack maxExtract = FluidStackHooksForge.toForge(component.extract(2147483647L, true));
                        if (!maxExtract.isEmpty()) {
                            int maxInsert = handlerItem.fill(maxExtract, IFluidHandler.FluidAction.SIMULATE);
                            if (maxInsert > 0) {
                                FluidStack extracted = FluidStackHooksForge.toForge(component.extract((long) maxInsert, false));
                                if (extracted.getAmount() > 0) {
                                    handlerItem.fill(extracted, IFluidHandler.FluidAction.EXECUTE);
                                }
                            }
                        }
                    }
                }
            }
            slot.setItemStack(handlerItem.getContainer());
        }
    }

    @Override
    public ItemStack transferFluid(ItemStack stack, FluidMachineComponent component) {
        FluidTank tank = new FluidTank(component);
        FluidActionResult result = FluidUtil.tryEmptyContainer(stack, tank, Integer.MAX_VALUE, null, true);
        if (result.isSuccess()) {
            return result.getResult();
        } else {
            result = FluidUtil.tryFillContainer(stack, tank, Integer.MAX_VALUE, null, true);
            return result.isSuccess() ? result.getResult() : stack;
        }
    }
}