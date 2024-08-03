package org.violetmoon.zeta.client.event.play;

import com.mojang.blaze3d.platform.Window;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

@Deprecated(forRemoval = true)
public interface ZCustomizeDebugText extends IZetaPlayEvent {

    @Deprecated
    List<String> getLeft();

    @Deprecated
    List<String> getRight();

    @Deprecated
    Window getWindow();

    @Deprecated
    GuiGraphics getGuiGraphics();

    @Deprecated
    float getPartialTick();
}