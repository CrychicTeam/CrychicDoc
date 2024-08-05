package com.simibubi.create.foundation.ponder.element;

import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.ui.PonderUI;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.gui.GuiGraphics;

public abstract class AnimatedOverlayElement extends PonderOverlayElement {

    protected LerpedFloat fade = LerpedFloat.linear().startWithValue(0.0);

    public void setFade(float fade) {
        this.fade.setValue((double) fade);
    }

    @Override
    public final void render(PonderScene scene, PonderUI screen, GuiGraphics graphics, float partialTicks) {
        float currentFade = this.fade.getValue(partialTicks);
        this.render(scene, screen, graphics, partialTicks, currentFade);
    }

    protected abstract void render(PonderScene var1, PonderUI var2, GuiGraphics var3, float var4, float var5);
}