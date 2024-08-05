package com.simibubi.create.foundation.ponder.element;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.element.BoxElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.ui.PonderUI;
import com.simibubi.create.foundation.utility.Color;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class TextWindowElement extends AnimatedOverlayElement {

    Supplier<String> textGetter = () -> "(?) No text was provided";

    String bakedText;

    int y;

    Vec3 vec;

    boolean nearScene = false;

    int color = PonderPalette.WHITE.getColor();

    @Override
    protected void render(PonderScene scene, PonderUI screen, GuiGraphics graphics, float partialTicks, float fade) {
        if (this.bakedText == null) {
            this.bakedText = (String) this.textGetter.get();
        }
        if (!(fade < 0.0625F)) {
            PonderScene.SceneTransform transform = scene.getTransform();
            Vec2 sceneToScreen = this.vec != null ? transform.sceneToScreen(this.vec, partialTicks) : new Vec2((float) (screen.f_96543_ / 2), (float) ((screen.f_96544_ - 200) / 2 + this.y - 8));
            boolean settled = transform.xRotation.settled() && transform.yRotation.settled();
            float pY = settled ? (float) ((int) sceneToScreen.y) : sceneToScreen.y;
            float yDiff = ((float) screen.f_96544_ / 2.0F - sceneToScreen.y - 10.0F) / 100.0F;
            float targetX = (float) screen.f_96543_ * Mth.lerp(yDiff * yDiff, 0.75F, 0.625F);
            if (this.nearScene) {
                targetX = Math.min(targetX, sceneToScreen.x + 50.0F);
            }
            if (settled) {
                targetX = (float) ((int) targetX);
            }
            int textWidth = (int) Math.min((float) screen.f_96543_ - targetX, 180.0F);
            List<FormattedText> lines = screen.getFontRenderer().getSplitter().splitLines(this.bakedText, textWidth, Style.EMPTY);
            int boxWidth = 0;
            for (FormattedText line : lines) {
                boxWidth = Math.max(boxWidth, screen.getFontRenderer().width(line));
            }
            int boxHeight = screen.getFontRenderer().wordWrapHeight(this.bakedText, boxWidth);
            PoseStack ms = graphics.pose();
            ms.pushPose();
            ms.translate(0.0F, pY, 400.0F);
            new BoxElement().<BoxElement>withBackground(Theme.c(Theme.Key.PONDER_BACKGROUND_FLAT)).<BoxElement>gradientBorder(Theme.p(Theme.Key.TEXT_WINDOW_BORDER)).<RenderElement>at(targetX - 10.0F, 3.0F, 100.0F).<RenderElement>withBounds(boxWidth, boxHeight - 1).render(graphics);
            int brighterColor = Color.mixColors(this.color, -35, 0.5F);
            brighterColor = 16777215 & brighterColor | 0xFF000000;
            if (this.vec != null) {
                ms.pushPose();
                ms.translate(sceneToScreen.x, 0.0F, 0.0F);
                double lineTarget = (double) ((targetX - sceneToScreen.x) * fade);
                ms.scale((float) lineTarget, 1.0F, 1.0F);
                graphics.fillGradient(0, 0, 1, 1, -100, brighterColor, brighterColor);
                graphics.fillGradient(0, 1, 1, 2, -100, -11974327, -13027015);
                ms.popPose();
            }
            ms.translate(0.0F, 0.0F, 400.0F);
            for (int i = 0; i < lines.size(); i++) {
                graphics.drawString(screen.getFontRenderer(), ((FormattedText) lines.get(i)).getString(), targetX - 10.0F, (float) (3 + 9 * i), new Color(brighterColor).scaleAlpha(fade).getRGB(), false);
            }
            ms.popPose();
        }
    }

    public int getColor() {
        return this.color;
    }

    public class Builder {

        private PonderScene scene;

        public Builder(PonderScene scene) {
            this.scene = scene;
        }

        public TextWindowElement.Builder colored(PonderPalette color) {
            TextWindowElement.this.color = color.getColor();
            return this;
        }

        public TextWindowElement.Builder pointAt(Vec3 vec) {
            TextWindowElement.this.vec = vec;
            return this;
        }

        public TextWindowElement.Builder independent(int y) {
            TextWindowElement.this.y = y;
            return this;
        }

        public TextWindowElement.Builder independent() {
            return this.independent(0);
        }

        public TextWindowElement.Builder text(String defaultText) {
            TextWindowElement.this.textGetter = this.scene.registerText(defaultText);
            return this;
        }

        public TextWindowElement.Builder sharedText(ResourceLocation key) {
            TextWindowElement.this.textGetter = () -> PonderLocalization.getShared(key);
            return this;
        }

        public TextWindowElement.Builder sharedText(String key) {
            return this.sharedText(new ResourceLocation(this.scene.getNamespace(), key));
        }

        public TextWindowElement.Builder placeNearTarget() {
            TextWindowElement.this.nearScene = true;
            return this;
        }

        public TextWindowElement.Builder attachKeyFrame() {
            this.scene.builder().addLazyKeyframe();
            return this;
        }
    }
}