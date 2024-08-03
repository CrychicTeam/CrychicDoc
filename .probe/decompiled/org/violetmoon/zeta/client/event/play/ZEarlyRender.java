package org.violetmoon.zeta.client.event.play;

import net.minecraft.client.gui.GuiGraphics;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZEarlyRender extends IZetaPlayEvent {

    GuiGraphics guiGraphics();
}