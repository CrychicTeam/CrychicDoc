package noppes.npcs.containers;

import net.minecraft.world.entity.player.Inventory;
import noppes.npcs.CustomContainer;

public class ContainerNPCBankSmall extends ContainerNPCBankInterface {

    public ContainerNPCBankSmall(int containerId, Inventory playerInventory, int slot, int bankid) {
        super(CustomContainer.container_banksmall, containerId, playerInventory, slot, bankid);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getRowNumber() {
        return 3;
    }
}