package net.mehvahdjukaar.supplementaries.common.inventories;

import net.mehvahdjukaar.moonlight.api.misc.IContainerProvider;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SafeBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModMenuTypes;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SafeContainerMenu extends ShulkerBoxMenu implements IContainerProvider {

    private final SafeBlockTile tile;

    public SafeContainerMenu(int id, Inventory inventory, SafeBlockTile container) {
        super(id, inventory, container);
        this.tile = container;
    }

    public SafeContainerMenu(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(id, playerInventory, (SafeBlockTile) ((BlockEntityType) ModRegistry.SAFE_TILE.get()).getBlockEntity(playerInventory.player.m_9236_(), packetBuffer.readBlockPos()));
    }

    @Override
    public MenuType<?> getType() {
        return (MenuType<?>) ModMenuTypes.SAFE.get();
    }

    @Override
    protected Slot addSlot(Slot slot) {
        return slot instanceof ShulkerBoxSlot ? super.m_38897_(new DelegatingSlot(this.f_40186_, slot.getContainerSlot(), slot.x, slot.y, this)) : super.m_38897_(slot);
    }

    @Override
    public Container getContainer() {
        return this.tile;
    }
}