package noppes.npcs.containers;

import net.minecraft.world.entity.player.Inventory;
import noppes.npcs.CustomContainer;

public class ContainerNPCBankLarge extends ContainerNPCBankInterface {

    public ContainerNPCBankLarge(int containerId, Inventory playerInventory, int slot, int bankid) {
        super(CustomContainer.container_banklarge, containerId, playerInventory, slot, bankid);
    }

    @Override
    public boolean isUpgraded() {
        return true;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getRowNumber() {
        return 6;
    }
}