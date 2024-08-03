package com.almostreliable.summoningrituals.compat.viewer.common;

import com.almostreliable.summoningrituals.Registration;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class AltarRenderer extends SizedItemRenderer {

    private final ItemStack altar = new ItemStack(Registration.ALTAR_ITEM.get());

    public AltarRenderer(int size) {
        super(size);
    }

    @Override
    public void render(GuiGraphics guiGraphics, @Nullable ItemStack item) {
        super.render(guiGraphics, this.altar);
    }
}