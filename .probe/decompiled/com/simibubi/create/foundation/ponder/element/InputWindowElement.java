package com.simibubi.create.foundation.ponder.element;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.ui.PonderUI;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class InputWindowElement extends AnimatedOverlayElement {

    private Pointing direction;

    ResourceLocation key;

    AllIcons icon;

    ItemStack item = ItemStack.EMPTY;

    private Vec3 sceneSpace;

    public InputWindowElement clone() {
        InputWindowElement inputWindowElement = new InputWindowElement(this.sceneSpace, this.direction);
        inputWindowElement.key = this.key;
        inputWindowElement.icon = this.icon;
        inputWindowElement.item = this.item.copy();
        return inputWindowElement;
    }

    public InputWindowElement(Vec3 sceneSpace, Pointing direction) {
        this.sceneSpace = sceneSpace;
        this.direction = direction;
    }

    public InputWindowElement withItem(ItemStack stack) {
        this.item = stack;
        return this;
    }

    public InputWindowElement withWrench() {
        this.item = AllItems.WRENCH.asStack();
        return this;
    }

    public InputWindowElement scroll() {
        this.icon = AllIcons.I_SCROLL;
        return this;
    }

    public InputWindowElement rightClick() {
        this.icon = AllIcons.I_RMB;
        return this;
    }

    public InputWindowElement showing(AllIcons icon) {
        this.icon = icon;
        return this;
    }

    public InputWindowElement leftClick() {
        this.icon = AllIcons.I_LMB;
        return this;
    }

    public InputWindowElement whileSneaking() {
        this.key = Create.asResource("sneak_and");
        return this;
    }

    public InputWindowElement whileCTRL() {
        this.key = Create.asResource("ctrl_and");
        return this;
    }

    @Override
    protected void render(PonderScene scene, PonderUI screen, GuiGraphics graphics, float partialTicks, float fade) {
        Font font = screen.getFontRenderer();
        int width = 0;
        int height = 0;
        float xFade = this.direction == Pointing.RIGHT ? -1.0F : (this.direction == Pointing.LEFT ? 1.0F : 0.0F);
        float yFade = this.direction == Pointing.DOWN ? -1.0F : (this.direction == Pointing.UP ? 1.0F : 0.0F);
        xFade *= 10.0F * (1.0F - fade);
        yFade *= 10.0F * (1.0F - fade);
        boolean hasItem = !this.item.isEmpty();
        boolean hasText = this.key != null;
        boolean hasIcon = this.icon != null;
        int keyWidth = 0;
        String text = hasText ? PonderLocalization.getShared(this.key) : "";
        if (!(fade < 0.0625F)) {
            Vec2 sceneToScreen = scene.getTransform().sceneToScreen(this.sceneSpace, partialTicks);
            if (hasIcon) {
                width += 24;
                height = 24;
            }
            if (hasText) {
                keyWidth = font.width(text);
                width += keyWidth;
            }
            if (hasItem) {
                width += 24;
                height = 24;
            }
            PoseStack ms = graphics.pose();
            ms.pushPose();
            ms.translate(sceneToScreen.x + xFade, sceneToScreen.y + yFade, 400.0F);
            PonderUI.renderSpeechBox(graphics, 0, 0, width, height, false, this.direction, true);
            ms.translate(0.0F, 0.0F, 100.0F);
            if (hasText) {
                graphics.drawString(font, text, 2.0F, (float) (height - 9) / 2.0F + 2.0F, PonderPalette.WHITE.getColorObject().scaleAlpha(fade).getRGB(), false);
            }
            if (hasIcon) {
                ms.pushPose();
                ms.translate((float) keyWidth, 0.0F, 0.0F);
                ms.scale(1.5F, 1.5F, 1.5F);
                this.icon.render(graphics, 0, 0);
                ms.popPose();
            }
            if (hasItem) {
                GuiGameElement.of(this.item).<GuiGameElement.GuiRenderBuilder>at((float) (keyWidth + (hasIcon ? 24 : 0)), 0.0F).scale(1.5).render(graphics);
                RenderSystem.disableDepthTest();
            }
            ms.popPose();
        }
    }
}