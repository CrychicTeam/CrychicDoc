package com.mna.gui.base;

import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class GuiJEIDisable<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public GuiJEIDisable(T p_i51105_1_, Inventory p_i51105_2_, Component p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    public List<Rect2i> getSideWindowBounds() {
        Minecraft mc = Minecraft.getInstance();
        return Arrays.asList(new Rect2i(0, 0, mc.screen.width, mc.screen.height));
    }
}