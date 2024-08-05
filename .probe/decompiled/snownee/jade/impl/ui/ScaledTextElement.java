package snownee.jade.impl.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.overlay.DisplayHelper;

public class ScaledTextElement extends TextElement {

    public final float scale;

    public ScaledTextElement(Component component, float scale) {
        super(component);
        this.scale = scale;
    }

    @Override
    public Vec2 getSize() {
        Font font = Minecraft.getInstance().font;
        return new Vec2((float) font.width(this.text) * this.scale, 9.0F * this.scale + 1.0F);
    }

    @Override
    public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
        PoseStack matrixStack = guiGraphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(x, y + this.scale, 0.0F);
        matrixStack.scale(this.scale, this.scale, 1.0F);
        DisplayHelper.INSTANCE.drawText(guiGraphics, this.text, 0.0F, 0.0F, IThemeHelper.get().getNormalColor());
        matrixStack.popPose();
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.text.getString();
    }
}