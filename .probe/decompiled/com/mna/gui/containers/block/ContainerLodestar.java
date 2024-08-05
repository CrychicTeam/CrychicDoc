package com.mna.gui.containers.block;

import com.mna.blocks.artifice.LodestarBlock;
import com.mna.blocks.tileentities.LodestarTile;
import com.mna.gui.containers.ContainerInit;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerLodestar extends AbstractContainerMenu {

    private LodestarTile lodestar;

    public ContainerLodestar(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(i, playerInventory, ((LodestarTile) playerInventory.player.m_9236_().getBlockEntity(packetBuffer.readBlockPos())).readFrom(packetBuffer));
    }

    public ContainerLodestar(int id, Inventory inv, LodestarTile lodestar) {
        super(ContainerInit.LODESTAR.get(), id);
        this.lodestar = lodestar;
        for (int xpos = 0; xpos < 3; xpos++) {
            for (int ypos = 0; ypos < 9; ypos++) {
                this.m_38897_(new Slot(inv, ypos + xpos * 9 + 9, 48 + ypos * 18, 173 + xpos * 18));
            }
        }
        for (int var6 = 0; var6 < 9; var6++) {
            this.m_38897_(new Slot(inv, var6, 48 + var6 * 18, 231));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        return false;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        return false;
    }

    @Override
    public boolean canDragTo(Slot slotIn) {
        return false;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    public void updateTileLogic(CompoundTag logic, boolean sync) {
        this.lodestar.setLogic(logic, sync);
    }

    public CompoundTag getLogic() {
        return this.lodestar.getLogic();
    }

    public boolean isLowTier() {
        return (Boolean) this.lodestar.m_58900_().m_61143_(LodestarBlock.LOW_TIER);
    }

    public List<Component> getWarnings() {
        return this.lodestar.getWarnings();
    }

    public List<Component> getErrors() {
        return this.lodestar.getErrors();
    }

    public List<String> getMisconfiguredNodeIDs() {
        return this.lodestar.getMisconfiguredNodeIDs();
    }
}