package fuzs.overflowingbars.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.config.ClientConfig;
import java.util.stream.IntStream;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RowCountRenderer {

    private static final ResourceLocation TINY_NUMBERS_LOCATION = new ResourceLocation("overflowingbars", "textures/font/tiny_numbers.png");

    public static void drawBarRowCount(GuiGraphics guiGraphics, int posX, int posY, int barValue, boolean left, Font font) {
        drawBarRowCount(guiGraphics, posX, posY, barValue, left, 20, font);
    }

    public static void drawBarRowCount(GuiGraphics guiGraphics, int posX, int posY, int barValue, boolean left, int maxRowCount, Font font) {
        if (barValue > 0 && maxRowCount > 0) {
            float rowCount = (float) barValue / (float) maxRowCount;
            ClientConfig config = OverflowingBars.CONFIG.get(ClientConfig.class);
            if (config.rowCount.alwaysRenderRowCount || !(rowCount <= 1.0F)) {
                int numberValue;
                if (config.rowCount.countFullRowsOnly) {
                    numberValue = Mth.floor(rowCount);
                } else {
                    numberValue = Mth.ceil(rowCount);
                }
                int textColor = config.rowCount.rowCountColor.getColor();
                if (config.rowCount.forceFontRenderer) {
                    String text = String.valueOf(numberValue);
                    if (config.rowCount.rowCountX) {
                        text = text + "x";
                    }
                    if (left) {
                        posX -= font.width(text);
                    }
                    drawBorderedText(guiGraphics, posX, posY + 1, text, textColor, 255, font);
                } else {
                    int[] numberDigitis = IntStream.iterate(numberValue, ix -> ix > 0, ix -> ix / 10).map(ix -> ix % 10).toArray();
                    float red = (float) (textColor >> 16 & 0xFF) / 255.0F;
                    float green = (float) (textColor >> 8 & 0xFF) / 255.0F;
                    float blue = (float) (textColor >> 0 & 0xFF) / 255.0F;
                    if (left) {
                        posX -= config.rowCount.rowCountX ? 7 : 3;
                    } else {
                        posX += 4 * numberDigitis.length;
                    }
                    for (int i = 0; i < numberDigitis.length; i++) {
                        drawBorderedSprite(guiGraphics, 3, 5, posX - 4 * i, posY + 2, 5 * numberDigitis[i], 0, red, green, blue, 1.0F);
                    }
                    if (OverflowingBars.CONFIG.get(ClientConfig.class).rowCount.rowCountX) {
                        drawBorderedSprite(guiGraphics, 3, 5, posX + 4, posY + 2, 0, 7, red, green, blue, 1.0F);
                    }
                }
            }
        }
    }

    private static void drawBorderedSprite(GuiGraphics guiGraphics, int width, int height, int posX, int posY, int textureX, int textureY, float red, float green, float blue, float alpha) {
        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, alpha);
        guiGraphics.blit(TINY_NUMBERS_LOCATION, posX - 1, posY, (float) textureX, (float) textureY, width, height, 256, 256);
        guiGraphics.blit(TINY_NUMBERS_LOCATION, posX + 1, posY, (float) textureX, (float) textureY, width, height, 256, 256);
        guiGraphics.blit(TINY_NUMBERS_LOCATION, posX, posY - 1, (float) textureX, (float) textureY, width, height, 256, 256);
        guiGraphics.blit(TINY_NUMBERS_LOCATION, posX, posY + 1, (float) textureX, (float) textureY, width, height, 256, 256);
        RenderSystem.setShaderColor(red, green, blue, alpha);
        guiGraphics.blit(TINY_NUMBERS_LOCATION, posX, posY, (float) textureX, (float) textureY, width, height, 256, 256);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void drawBorderedText(GuiGraphics guiGraphics, int posX, int posY, String text, int color, int alpha, Font font) {
        guiGraphics.drawString(font, text, posX - 1, posY, alpha << 24, false);
        guiGraphics.drawString(font, text, posX + 1, posY, alpha << 24, false);
        guiGraphics.drawString(font, text, posX, posY - 1, alpha << 24, false);
        guiGraphics.drawString(font, text, posX, posY + 1, alpha << 24, false);
        guiGraphics.drawString(font, text, posX, posY, color & 16777215 | alpha << 24, false);
    }
}