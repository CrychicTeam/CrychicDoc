package mezz.jei.common.util;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;

public final class RectDebugger {

    public static final RectDebugger INSTANCE = new RectDebugger();

    private final Map<String, RectDebugger.Rect> rects = new HashMap();

    private RectDebugger() {
    }

    public void add(ImmutableRect2i rect, int color, String id) {
        this.rects.put(id, new RectDebugger.Rect(rect, color));
    }

    public void draw(GuiGraphics guiGraphics) {
        RenderSystem.disableDepthTest();
        for (RectDebugger.Rect rect : this.rects.values()) {
            ImmutableRect2i rect1 = rect.rect;
            guiGraphics.fill(RenderType.guiOverlay(), rect1.getX(), rect1.getY(), rect1.getX() + rect1.getWidth(), rect1.getY() + rect1.getHeight(), rect.color);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static record Rect(ImmutableRect2i rect, int color) {
    }
}