package noppes.npcs.containers;

import net.minecraft.world.entity.player.Inventory;
import noppes.npcs.CustomContainer;

public class ContainerNPCBankUpgrade extends ContainerNPCBankInterface {

    public ContainerNPCBankUpgrade(int containerId, Inventory playerInventory, int slot, int bankid) {
        super(CustomContainer.container_bankupgrade, containerId, playerInventory, slot, bankid);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean canBeUpgraded() {
        return true;
    }

    @Override
    public int xOffset() {
        return 54;
    }

    @Override
    public int getRowNumber() {
        return 3;
    }
}