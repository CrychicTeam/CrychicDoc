package dev.shadowsoffire.attributeslib.client;

import dev.shadowsoffire.attributeslib.AttributesLib;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class AttributeModifierComponent implements ClientTooltipComponent {

    public static final ResourceLocation TEXTURE = AttributesLib.loc("textures/gui/attribute_component.png");

    @Nullable
    private final ModifierSource<?> source;

    private final List<FormattedCharSequence> text;

    public AttributeModifierComponent(@Nullable ModifierSource<?> source, FormattedText text, Font font, int maxWidth) {
        this.source = source;
        this.text = font.split(text, maxWidth);
    }

    @Override
    public int getHeight() {
        return this.text.size() * 10;
    }

    @Override
    public int getWidth(Font font) {
        return (Integer) this.text.stream().map(font::m_92724_).map(w -> w + 12).max(Integer::compareTo).get();
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics gfx) {
        gfx.blit(TEXTURE, x, y, 0, this.source == null ? 9.0F : 0.0F, 0.0F, 9, 9, 18, 9);
        if (this.source != null) {
            this.source.render(gfx, font, x, y);
        }
    }

    @Override
    public void renderText(Font font, int pX, int pY, Matrix4f pMatrix4f, MultiBufferSource.BufferSource pBufferSource) {
        FormattedCharSequence line = (FormattedCharSequence) this.text.get(0);
        font.drawInBatch(line, (float) (pX + 12), (float) pY, -1, true, pMatrix4f, pBufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        for (int i = 1; i < this.text.size(); i++) {
            line = (FormattedCharSequence) this.text.get(i);
            font.drawInBatch(line, (float) pX, (float) (pY + i * (9 + 1)), -1, true, pMatrix4f, pBufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        }
    }
}