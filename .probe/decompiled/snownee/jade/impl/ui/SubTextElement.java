package snownee.jade.impl.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.Element;
import snownee.jade.overlay.DisplayHelper;

public class SubTextElement extends Element {

    private final Component text;

    public SubTextElement(Component text) {
        this.text = text;
    }

    @Override
    public Vec2 getSize() {
        return Vec2.ZERO;
    }

    @Override
    public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
        PoseStack matrixStack = guiGraphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(x, y, 800.0F);
        matrixStack.scale(0.75F, 0.75F, 0.0F);
        DisplayHelper.INSTANCE.drawText(guiGraphics, this.text, 0.0F, 0.0F, IThemeHelper.get().getNormalColor());
        matrixStack.popPose();
    }
}