package dev.latvian.mods.kubejs.gui;

import dev.latvian.mods.kubejs.core.InventoryKJS;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;

public class KubeJSGUI {

    private static final Component DEFAULT_TITLE = Component.literal("KubeJS");

    public static final SimpleContainer EMPTY_CONTAINER = new SimpleContainer(0);

    public int width = 176;

    public int height = 166;

    public Component title = DEFAULT_TITLE;

    public int inventoryLabelX = -1;

    public int inventoryLabelY = -1;

    public InventoryKJS inventory = EMPTY_CONTAINER;

    public int inventoryWidth = 0;

    public int inventoryHeight = 0;

    public int playerSlotsX = -1;

    public int playerSlotsY = -1;

    public KubeJSGUI() {
    }

    public KubeJSGUI(FriendlyByteBuf buf) {
        this.width = buf.readShort();
        this.height = buf.readShort();
        this.inventoryLabelX = buf.readShort();
        this.inventoryLabelY = buf.readShort();
        this.inventory = new SimpleContainer(buf.readUnsignedByte());
        this.inventoryWidth = buf.readUnsignedByte();
        this.inventoryHeight = buf.readUnsignedByte();
        this.playerSlotsX = buf.readShort();
        this.playerSlotsY = buf.readShort();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeShort(this.width);
        buf.writeShort(this.height);
        buf.writeShort(this.inventoryLabelX);
        buf.writeShort(this.inventoryLabelY);
        buf.writeByte(this.inventory.kjs$getSlots());
        buf.writeByte(this.inventoryWidth);
        buf.writeByte(this.inventoryHeight);
        buf.writeShort(this.playerSlotsX);
        buf.writeShort(this.playerSlotsY);
    }

    public void setInventory(InventoryKJS inv) {
        this.inventory = inv;
        this.inventoryWidth = inv.kjs$getWidth();
        this.inventoryHeight = inv.kjs$getHeight();
        this.height = 114 + this.inventoryHeight * 18;
        this.inventoryLabelY = this.height - 94;
        this.playerSlotsX = 8;
        this.playerSlotsY = 103;
    }
}