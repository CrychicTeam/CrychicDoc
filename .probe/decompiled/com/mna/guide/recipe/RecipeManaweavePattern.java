package com.mna.guide.recipe;

import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.gui.GuiTextures;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.manaweaving.ManaweavingPatternHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;

public class RecipeManaweavePattern extends RecipeRendererBase {

    private ManaweavingPattern pattern;

    static final int POINT_RENDER_SIZE = 13;

    public RecipeManaweavePattern(int x, int y) {
        super(x, y);
    }

    @Override
    public void init_internal(ResourceLocation recipeLocation) {
        this.pattern = ManaweavingPatternHelper.GetManaweavingRecipe(this.minecraft.level, recipeLocation);
    }

    @Override
    protected ResourceLocation backgroundTexture() {
        return GuiTextures.Recipe.BLANK;
    }

    @Override
    protected void drawForeground(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (this.pattern != null) {
            int startX = x + 40;
            int startY = y + 60;
            int pointSize = (this.f_93618_ - 80) / this.pattern.get().length;
            byte[][] pData = this.pattern.get();
            for (int i = 0; i < pData.length; i++) {
                for (int j = 0; j < pData[i].length; j++) {
                    if (pData[i][j] != 0) {
                        pGuiGraphics.blit(GuiTextures.Widgets.GUIDE_WIDGETS, startX + j * pointSize, startY + i * pointSize, 0, 0, 13, 13);
                    }
                }
            }
            Font fr = this.minecraft.font;
            String patternName = I18n.get(this.pattern.m_6423_().toString());
            String prefix = I18n.get("mechanic.mna.manaweaving_pattern");
            pGuiGraphics.drawString(fr, prefix, x + this.f_93618_ / 2 - fr.width(prefix) / 2, y + 10, 4210752, false);
            pGuiGraphics.drawString(fr, patternName, x + this.f_93618_ / 2 - fr.width(patternName) / 2, y + 20, 4210752, false);
        }
    }

    @Override
    public int getTier() {
        return this.pattern != null ? this.pattern.getTier() : 1;
    }
}