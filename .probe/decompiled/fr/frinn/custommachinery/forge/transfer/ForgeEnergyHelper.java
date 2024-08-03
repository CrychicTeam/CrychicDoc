package fr.frinn.custommachinery.forge.transfer;

import fr.frinn.custommachinery.common.component.EnergyMachineComponent;
import fr.frinn.custommachinery.common.component.ItemMachineComponent;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.common.util.transfer.IEnergyHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgeEnergyHelper implements IEnergyHelper {

    private static final Component ENERGY_UNIT = Component.translatable("unit.energy.forge");

    @Override
    public Component unit() {
        return ENERGY_UNIT;
    }

    @Override
    public boolean isEnergyHandler(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
    }

    @Override
    public void fillBufferFromStack(EnergyMachineComponent buffer, ItemMachineComponent slot) {
        ItemStack stack = slot.getItemStack();
        if (!stack.isEmpty()) {
            IEnergyStorage handler = (IEnergyStorage) stack.getCapability(ForgeCapabilities.ENERGY).orElseThrow(() -> new IllegalStateException("Can't fill energy buffer from non energy storage item: " + ForgeRegistries.ITEMS.getKey(stack.getItem())));
            if (handler.canExtract()) {
                int maxExtract = handler.extractEnergy(Integer.MAX_VALUE, true);
                if (maxExtract > 0) {
                    long maxInsert = buffer.receiveEnergy((long) maxExtract, true);
                    if (maxInsert > 0L) {
                        int extracted = handler.extractEnergy(Utils.toInt(maxInsert), false);
                        if (extracted > 0) {
                            buffer.receiveEnergy((long) extracted, false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void fillStackFromBuffer(ItemMachineComponent slot, EnergyMachineComponent buffer) {
        ItemStack stack = slot.getItemStack();
        if (!stack.isEmpty()) {
            IEnergyStorage handler = (IEnergyStorage) stack.getCapability(ForgeCapabilities.ENERGY).orElseThrow(() -> new IllegalStateException("Can't fill energy buffer from non energy storage item: " + ForgeRegistries.ITEMS.getKey(stack.getItem())));
            if (handler.canReceive()) {
                long maxExtract = buffer.extractEnergy(2147483647L, true);
                if (maxExtract > 0L) {
                    int maxInsert = handler.receiveEnergy(Utils.toInt(maxExtract), true);
                    if (maxInsert > 0) {
                        long extracted = buffer.extractEnergy((long) maxInsert, false);
                        if (extracted > 0L) {
                            handler.receiveEnergy(Utils.toInt(extracted), false);
                        }
                    }
                }
            }
        }
    }
}