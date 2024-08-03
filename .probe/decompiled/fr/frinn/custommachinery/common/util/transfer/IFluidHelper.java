package fr.frinn.custommachinery.common.util.transfer;

import fr.frinn.custommachinery.common.component.FluidMachineComponent;
import fr.frinn.custommachinery.common.component.ItemMachineComponent;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public interface IFluidHelper {

    boolean isFluidHandler(ItemStack var1);

    void fillTanksFromStack(List<FluidMachineComponent> var1, ItemMachineComponent var2);

    void fillStackFromTanks(ItemMachineComponent var1, List<FluidMachineComponent> var2);

    ItemStack transferFluid(ItemStack var1, FluidMachineComponent var2);
}