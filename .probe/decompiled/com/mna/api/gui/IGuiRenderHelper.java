package com.mna.api.gui;

import com.mna.api.faction.IFaction;
import net.minecraft.client.gui.GuiGraphics;

public interface IGuiRenderHelper {

    void renderFactionIcon(GuiGraphics var1, IFaction var2, int var3, int var4);
}