package com.blamejared.controlling.api.events;

import com.blamejared.controlling.client.NewKeyBindsList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.eventbus.api.Event;

public class KeyEntryRenderEvent extends Event implements IKeyEntryRenderEvent {

    private final NewKeyBindsList.KeyEntry entry;

    private final GuiGraphics guiGraphics;

    private final int slotIndex;

    private final int y;

    private final int x;

    private final int rowLeft;

    private final int rowWidth;

    private final int mouseX;

    private final int mouseY;

    private final boolean hovered;

    private final float partialTicks;

    public KeyEntryRenderEvent(NewKeyBindsList.KeyEntry entry, GuiGraphics guiGraphics, int slotIndex, int y, int x, int rowLeft, int rowWidth, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        this.entry = entry;
        this.guiGraphics = guiGraphics;
        this.slotIndex = slotIndex;
        this.y = y;
        this.x = x;
        this.rowLeft = rowLeft;
        this.rowWidth = rowWidth;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.hovered = hovered;
        this.partialTicks = partialTicks;
    }

    @Override
    public NewKeyBindsList.KeyEntry getEntry() {
        return this.entry;
    }

    @Override
    public GuiGraphics getGuiGraphics() {
        return this.guiGraphics;
    }

    @Override
    public int getSlotIndex() {
        return this.slotIndex;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getRowLeft() {
        return this.rowLeft;
    }

    @Override
    public int getRowWidth() {
        return this.rowWidth;
    }

    @Override
    public int getMouseX() {
        return this.mouseX;
    }

    @Override
    public int getMouseY() {
        return this.mouseY;
    }

    @Override
    public boolean isHovered() {
        return this.hovered;
    }

    @Override
    public float getPartialTicks() {
        return this.partialTicks;
    }
}