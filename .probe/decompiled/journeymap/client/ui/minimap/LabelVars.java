package journeymap.client.ui.minimap;

import com.mojang.blaze3d.vertex.PoseStack;
import journeymap.client.JourneymapClient;
import journeymap.client.render.RenderWrapper;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.ui.theme.Theme;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

class LabelVars {

    final double x;

    final double y;

    final double fontScale;

    final DrawUtil.HAlign hAlign;

    final DrawUtil.VAlign vAlign;

    final DisplayVars displayVars;

    final Theme.LabelSpec labelSpec;

    LabelVars(DisplayVars displayVars, double x, double y, DrawUtil.HAlign hAlign, DrawUtil.VAlign vAlign, double fontScale, Theme.LabelSpec labelSpec) {
        this.displayVars = displayVars;
        this.x = x;
        this.y = y;
        this.hAlign = hAlign;
        this.vAlign = vAlign;
        this.fontScale = fontScale;
        this.labelSpec = labelSpec;
    }

    void draw(PoseStack poseStack, MultiBufferSource buffers, String text) {
        RenderWrapper.enableBlend();
        DrawUtil.drawBatchLabel(poseStack, Component.literal(text), buffers, (double) ((int) this.x), (double) ((int) this.y), this.hAlign, this.vAlign, this.labelSpec.background.getColor(), JourneymapClient.getInstance().getActiveMiniMapProperties().infoSlotAlpha.get(), this.labelSpec.foreground.getColor(), this.labelSpec.foreground.alpha, this.fontScale, this.labelSpec.shadow, 0.0);
        RenderWrapper.disableBlend();
    }
}