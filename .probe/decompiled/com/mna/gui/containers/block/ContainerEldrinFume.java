package com.mna.gui.containers.block;

import com.mna.api.affinity.Affinity;
import com.mna.blocks.tileentities.wizard_lab.EldrinFumeTile;
import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.slots.NonStupidFurnaceFuelSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerEldrinFume extends SimpleWizardLabContainer<ContainerEldrinFume, EldrinFumeTile> {

    public ContainerEldrinFume(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        super(ContainerInit.ELDRIN_FUME.get(), i, playerInventory, packetBuffer);
    }

    public ContainerEldrinFume(int id, Inventory playerInventory, EldrinFumeTile tile) {
        super(ContainerInit.ELDRIN_FUME.get(), id, playerInventory, tile);
    }

    @Override
    protected int addInventorySlots() {
        InvWrapper wrapper = new InvWrapper(this.tile);
        this.m_38897_(new NonStupidFurnaceFuelSlot(wrapper, 0, 80, 52));
        this.m_38897_(new Slot(this.tile, 1, 80, 14));
        return 2;
    }

    @Override
    protected int playerInventoryXStart() {
        return 8;
    }

    @Override
    protected int playerInventoryYStart() {
        return 76;
    }

    public float getMotePctRemaining() {
        return this.tile.getMotePctRemaining();
    }

    public float getFuelPctRemaining() {
        return this.tile.getFuelPctRemaining();
    }

    public Affinity getGeneratingAffinity() {
        return this.tile.getGeneratingAffinity();
    }
}