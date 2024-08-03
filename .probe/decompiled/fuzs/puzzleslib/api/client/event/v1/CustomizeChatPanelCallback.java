package fuzs.puzzleslib.api.client.event.v1;

import com.mojang.blaze3d.platform.Window;
import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface CustomizeChatPanelCallback {

    EventInvoker<CustomizeChatPanelCallback> EVENT = EventInvoker.lookup(CustomizeChatPanelCallback.class);

    void onRenderChatPanel(Window var1, GuiGraphics var2, float var3, MutableInt var4, MutableInt var5);
}