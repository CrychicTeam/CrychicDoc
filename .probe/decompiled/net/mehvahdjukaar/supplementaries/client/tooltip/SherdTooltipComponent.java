package net.mehvahdjukaar.supplementaries.client.tooltip;

import net.mehvahdjukaar.moonlight.api.client.util.RenderUtil;
import net.mehvahdjukaar.supplementaries.common.items.tooltip_components.SherdTooltip;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceKey;

public class SherdTooltipComponent implements ClientTooltipComponent {

    private final int size = (Integer) ClientConfigs.Tweaks.TOOLTIP_IMAGE_SIZE.get();

    private final ResourceKey<String> pattern;

    public SherdTooltipComponent(SherdTooltip tooltip) {
        this.pattern = tooltip.sherd();
    }

    @Override
    public int getHeight() {
        return this.size + 2;
    }

    @Override
    public int getWidth(Font pFont) {
        return this.size;
    }

    @Override
    public void renderImage(Font pFont, int x, int y, GuiGraphics graphics) {
        Material decoratedPotMaterial = Sheets.getDecoratedPotMaterial(this.pattern);
        if (decoratedPotMaterial != null) {
            TextureAtlasSprite sprite = decoratedPotMaterial.sprite();
            SpriteContents contents = sprite.contents();
            int width = contents.width();
            int height = contents.height();
            RenderUtil.blitSpriteSection(graphics, x, y, this.size, this.size, 2.0F * (16.0F / (float) width), 2.0F * (16.0F / (float) height), (int) (12.0F * (16.0F / (float) width)), (int) (12.0F * (16.0F / (float) width)), sprite);
        }
    }
}