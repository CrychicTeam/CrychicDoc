package com.mna.gui.containers.block;

import com.mna.blocks.tileentities.wizard_lab.ArcanaAltarTile;
import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.slots.ItemFilterSlot;
import com.mna.gui.containers.slots.SlotNoPlace;
import com.mna.items.filters.SpellItemFilter;
import com.mna.items.filters.SpellRecipeFilter;
import java.util.HashMap;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ContainerArcaneAltar extends SimpleWizardLabContainer<ContainerArcaneAltar, ArcanaAltarTile> {

    private boolean shouldRecalculateMaterials = false;

    public ContainerArcaneAltar(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        super(ContainerInit.ENSORCELLATION_STATION.get(), i, playerInventory, packetBuffer);
    }

    public ContainerArcaneAltar(int id, Inventory playerInventory, ArcanaAltarTile tile) {
        super(ContainerInit.ENSORCELLATION_STATION.get(), id, playerInventory, tile);
    }

    @Override
    protected int addInventorySlots() {
        ItemFilterSlot recipe = new ItemFilterSlot(this.tileItemHandler, 0, 65, 29, new SpellRecipeFilter()).setMaxStackSize(1);
        recipe.addChangeListener(this::slotChanged);
        this.m_38897_(recipe);
        this.m_38897_(new ItemFilterSlot(this.tileItemHandler, 1, 65, 100, new SpellItemFilter()).setMaxStackSize(1));
        this.m_38897_(new SlotNoPlace(this.tileItemHandler, 2, 142, 65));
        return 3;
    }

    @Override
    protected int playerInventoryXStart() {
        return 48;
    }

    @Override
    protected int playerInventoryYStart() {
        return 144;
    }

    public HashMap<ResourceLocation, Integer> getRemainingReagents() {
        return this.tile.getRemainingReagents();
    }

    public List<ItemStack> getRemainingManaweaves() {
        return this.tile.getRemainingManaweaves();
    }

    public void slotChanged(int index) {
        this.tile.reCacheRequirements();
        this.shouldRecalculateMaterials = true;
    }

    public boolean shouldRecalculateMaterials() {
        return this.shouldRecalculateMaterials;
    }

    public void materialsRecalculated() {
        this.shouldRecalculateMaterials = false;
    }
}