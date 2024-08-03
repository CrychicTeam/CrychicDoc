package com.simibubi.create.foundation.ponder.element;

import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.ui.PonderUI;
import net.minecraft.client.gui.GuiGraphics;

public abstract class PonderOverlayElement extends PonderElement {

    @Override
    public void tick(PonderScene scene) {
    }

    public abstract void render(PonderScene var1, PonderUI var2, GuiGraphics var3, float var4);
}