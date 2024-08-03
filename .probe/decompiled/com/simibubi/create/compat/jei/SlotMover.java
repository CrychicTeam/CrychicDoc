package com.simibubi.create.compat.jei;

import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import java.util.List;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;

public class SlotMover implements IGuiContainerHandler<AbstractSimiContainerScreen<?>> {

    public List<Rect2i> getGuiExtraAreas(AbstractSimiContainerScreen<?> containerScreen) {
        return containerScreen.getExtraAreas();
    }
}