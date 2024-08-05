package fr.frinn.custommachinery.common.util.transfer;

import fr.frinn.custommachinery.common.component.EnergyMachineComponent;
import fr.frinn.custommachinery.common.component.ItemMachineComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public interface IEnergyHelper {

    Component unit();

    boolean isEnergyHandler(ItemStack var1);

    void fillBufferFromStack(EnergyMachineComponent var1, ItemMachineComponent var2);

    void fillStackFromBuffer(ItemMachineComponent var1, EnergyMachineComponent var2);
}