package net.mehvahdjukaar.supplementaries.client.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.client.util.RenderUtil;
import net.mehvahdjukaar.supplementaries.common.items.tooltip_components.BannerPatternTooltip;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;

public class BannerPatternTooltipComponent implements ClientTooltipComponent {

    private final int size = (Integer) ClientConfigs.Tweaks.TOOLTIP_IMAGE_SIZE.get();

    private final BannerPatternTooltip tooltip;

    public BannerPatternTooltipComponent(BannerPatternTooltip tooltip) {
        this.tooltip = tooltip;
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
        graphics.pose().pushPose();
        Optional<Material> mat = BuiltInRegistries.BANNER_PATTERN.getTag(this.tooltip.pattern()).flatMap(n -> n.m_203614_().findAny()).flatMap(Holder::m_203543_).map(Sheets::m_234347_);
        if (mat.isPresent()) {
            TextureAtlasSprite sprite = ((Material) mat.get()).sprite();
            RenderSystem.enableBlend();
            SpriteContents contents = sprite.contents();
            int width = contents.width();
            int height = contents.height();
            RenderUtil.blitSpriteSection(graphics, x, y, this.size, this.size, 16.0F / (float) width, 16.0F / (float) height * 12.0F, (int) (0.3125F * (float) width), (int) (0.3125F * (float) height), sprite);
        }
        graphics.pose().popPose();
    }
}