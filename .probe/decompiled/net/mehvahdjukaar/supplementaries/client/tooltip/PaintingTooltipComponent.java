package net.mehvahdjukaar.supplementaries.client.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.mehvahdjukaar.supplementaries.common.items.tooltip_components.PaintingTooltip;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.PaintingTextureManager;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class PaintingTooltipComponent implements ClientTooltipComponent {

    private final int size = (Integer) ClientConfigs.Tweaks.TOOLTIP_IMAGE_SIZE.get();

    private final PaintingVariant pattern;

    private final int height;

    private final int width;

    public PaintingTooltipComponent(PaintingTooltip tooltip) {
        this.pattern = tooltip.pattern();
        float h = (float) this.pattern.getHeight();
        float w = (float) this.pattern.getWidth();
        if (h > w) {
            this.height = this.size;
            this.width = (int) ((float) this.size / h * w);
        } else {
            this.width = this.size;
            this.height = (int) ((float) this.size / w * h);
        }
    }

    @Override
    public int getHeight() {
        return this.height + 2;
    }

    @Override
    public int getWidth(Font pFont) {
        return this.width;
    }

    @Override
    public void renderImage(Font pFont, int x, int y, GuiGraphics graphics) {
        graphics.pose().pushPose();
        PaintingTextureManager paintingTextureManager = Minecraft.getInstance().getPaintingTextures();
        TextureAtlasSprite sprite = paintingTextureManager.get(this.pattern);
        RenderSystem.enableBlend();
        graphics.blit(x, y, 0, this.width, this.height, sprite);
        graphics.pose().popPose();
    }
}