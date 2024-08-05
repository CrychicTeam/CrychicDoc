package net.mehvahdjukaar.supplementaries.common.inventories;

import net.mehvahdjukaar.supplementaries.common.block.tiles.AbstractPresentBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class TrappedPresentContainerMenu extends PresentContainerMenu {

    public TrappedPresentContainerMenu(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(id, playerInventory, (AbstractPresentBlockTile) playerInventory.player.m_9236_().getBlockEntity(packetBuffer.readBlockPos()));
    }

    public TrappedPresentContainerMenu(int id, Inventory playerInventory, AbstractPresentBlockTile inventory) {
        super((MenuType) ModMenuTypes.TRAPPED_PRESENT_BLOCK.get(), id, playerInventory, inventory);
    }

    @Override
    protected int getSlotX() {
        return 62;
    }

    @Override
    protected int getSlotY() {
        return 36;
    }
}