package dev.xkmc.l2library.base.overlay;

import dev.xkmc.l2library.init.L2LibraryConfig;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public class OverlayUtil implements ClientTooltipPositioner {

    public int bg = getBGColor();

    public int bs = 1347420415;

    public int be = 1344798847;

    public int tc = -1;

    protected final GuiGraphics g;

    protected final int x0;

    protected final int y0;

    protected final int maxW;

    private static int getBGColor() {
        return (int) Math.round(L2LibraryConfig.CLIENT.infoAlpha.get() * 255.0) << 24 | 1048592;
    }

    public OverlayUtil(GuiGraphics g, int x0, int y0, int maxW) {
        this.g = g;
        this.x0 = x0;
        this.y0 = y0;
        this.maxW = maxW < 0 ? this.getMaxWidth() : maxW;
    }

    public int getMaxWidth() {
        return this.g.guiWidth() / 4;
    }

    public void renderLongText(Font font, List<Component> list) {
        List<ClientTooltipComponent> ans = list.stream().flatMap(text -> font.split(text, this.maxW).stream()).map(ClientTooltipComponent::m_169948_).toList();
        this.renderTooltipInternal(font, ans);
    }

    public void renderTooltipInternal(Font font, List<ClientTooltipComponent> list) {
        if (!list.isEmpty()) {
            int w = 0;
            int h = list.size() == 1 ? -2 : 0;
            for (ClientTooltipComponent c : list) {
                int wi = c.getWidth(font);
                if (wi > w) {
                    w = wi;
                }
                h += c.getHeight();
            }
            int wf = w;
            int hf = h;
            Vector2ic pos = this.positionTooltip(this.g.guiWidth(), this.g.guiHeight(), this.x0, this.y0, wf, hf);
            int xf = pos.x();
            int yf = pos.y();
            this.g.pose().pushPose();
            int z = 400;
            this.g.drawManaged(() -> TooltipRenderUtil.renderTooltipBackground(this.g, xf, yf, wf, hf, z, this.bg, this.bg, this.bs, this.be));
            this.g.pose().translate(0.0F, 0.0F, (float) z);
            int yi = yf;
            for (int i = 0; i < list.size(); i++) {
                ClientTooltipComponent c = (ClientTooltipComponent) list.get(i);
                c.renderText(font, xf, yi, this.g.pose().last().pose(), this.g.bufferSource());
                yi += c.getHeight() + (i == 0 ? 2 : 0);
            }
            yi = yf;
            for (int i = 0; i < list.size(); i++) {
                ClientTooltipComponent c = (ClientTooltipComponent) list.get(i);
                c.renderImage(font, xf, yi, this.g);
                yi += c.getHeight() + (i == 0 ? 2 : 0);
            }
            this.g.pose().popPose();
        }
    }

    @Override
    public Vector2ic positionTooltip(int gw, int gh, int x, int y, int tw, int th) {
        if (x < 0) {
            x = Math.round((float) gw / 8.0F);
        }
        if (y < 0) {
            y = Math.round((float) (gh - th) / 2.0F);
        }
        return new Vector2i(x, y);
    }

    public static void fillRect(GuiGraphics g, int x, int y, int w, int h, int col) {
        g.fill(x, y, x + w, y + h, col);
    }

    public static void drawRect(GuiGraphics g, int x, int y, int w, int h, int col) {
        fillRect(g, x - 1, y - 1, w + 2, 1, col);
        fillRect(g, x - 1, y - 1, 1, h + 2, col);
        fillRect(g, x - 1, y + h, w + 2, 1, col);
        fillRect(g, x + w, y - 1, 1, h + 2, col);
    }
}