package noppes.npcs.containers;

import net.minecraft.world.entity.player.Inventory;
import noppes.npcs.CustomContainer;

public class ContainerNPCBankUnlock extends ContainerNPCBankInterface {

    public ContainerNPCBankUnlock(int containerId, Inventory playerInventory, int slot, int bankid) {
        super(CustomContainer.container_bankunlock, containerId, playerInventory, slot, bankid);
    }
}