package com.mna.guide.recipe;

import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.api.rituals.IRitualReagent;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.recipes.rituals.RitualRecipe;
import com.mna.recipes.rituals.RitualRecipeHelper;
import com.mna.tools.render.GuiRenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;

public class RecipeRitual extends RecipeRendererBase {

    private RitualRecipe pattern;

    static final int POINT_RENDER_SIZE = 13;

    public RecipeRitual(int x, int y) {
        super(x, y);
    }

    @Override
    public void init_internal(ResourceLocation recipeLocation) {
        this.pattern = RitualRecipeHelper.GetRitualRecipe(this.minecraft.level, recipeLocation);
    }

    @Override
    protected ResourceLocation backgroundTexture() {
        return null;
    }

    @Override
    protected void drawForeground(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (this.pattern != null) {
            int startX = x + this.f_93618_ - 54;
            int startY = y + 60;
            int pointSize = (this.f_93618_ - 80) / this.pattern.getPattern().length;
            int centerOffset = (pointSize - 13) / 2;
            int[][] pData = this.pattern.getPattern();
            IRitualReagent[][] reagents = this.pattern.getReagents();
            for (int i = 0; i < pData.length; i++) {
                for (int j = 0; j < pData[i].length; j++) {
                    if (pData[i][j] != 0) {
                        pGuiGraphics.blit(GuiTextures.Widgets.GUIDE_WIDGETS, startX - j * pointSize - centerOffset, startY + i * pointSize + centerOffset, 13, 0, 13, 13);
                    }
                }
            }
            RenderSystem.enableDepthTest();
            int gridWidth = pData.length * pointSize;
            int gridTop = startY + 1;
            int gridLeft = startX + pointSize - gridWidth - centerOffset - (pointSize - 13) / 2 + 1;
            for (int i = 0; i <= pData.length; i++) {
                GuiRenderUtils.line2d(pGuiGraphics, (float) gridLeft, (float) (gridTop + pointSize * i), (float) (gridLeft + gridWidth), (float) (gridTop + pointSize * i), 0.0F, FastColor.ARGB32.color(255, 255, 255, 255));
                GuiRenderUtils.line2d(pGuiGraphics, (float) (gridLeft + pointSize * i), (float) gridTop, (float) (gridLeft + pointSize * i), (float) (gridTop + gridWidth), 0.0F, FastColor.ARGB32.color(255, 255, 255, 255));
            }
            int stackStartX = (int) ((float) this.m_252754_() / this.scale + (float) this.f_93618_ - 54.0F);
            int stackStartY = (int) ((float) this.m_252907_() / this.scale + 63.0F);
            int stackPointSize = (this.f_93618_ - 80) / this.pattern.getPattern().length;
            int stackCenterOffset = (pointSize - 13) / 2;
            for (int i = 0; i < reagents.length; i++) {
                for (int jx = 0; jx < reagents[i].length; jx++) {
                    if (reagents[i][jx] != null && !reagents[i][jx].isDynamic()) {
                        this.renderItemStack(pGuiGraphics, (List<ItemStack>) MATags.smartLookupItem(reagents[i][jx].getResourceLocation()).stream().map(item -> new ItemStack(item)).collect(Collectors.toList()), stackStartX - (int) Math.floor((double) (1.0F * this.scale)) - jx * stackPointSize - stackCenterOffset, stackStartY - (int) Math.ceil((double) (3.0F * this.scale)) + i * stackPointSize + stackCenterOffset, 1.0F);
                    }
                }
            }
            int tier = this.pattern.getTier();
            MutableInt playerTier = new MutableInt(0);
            this.minecraft.player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> playerTier.setValue(p.getTier()));
            int color = tier <= playerTier.getValue() ? FastColor.ARGB32.color(255, 0, 128, 0) : FastColor.ARGB32.color(255, 255, 0, 0);
            Component name = Component.translatable(this.pattern.m_6423_().toString());
            Component tierPrompt = Component.translatable("gui.mna.item-tier", tier);
            String patternSize = String.format("%dx%d", this.pattern.getPattern().length, this.pattern.getPattern().length);
            int stringWidth = this.minecraft.font.width(name);
            int textX = x + this.f_93618_ / 2 - stringWidth / 2;
            int textY = y + 5;
            pGuiGraphics.drawString(this.minecraft.font, name, textX, textY, FastColor.ARGB32.color(255, 255, 255, 255), false);
            pGuiGraphics.drawString(this.minecraft.font, tierPrompt, x + this.f_93618_ / 2 - this.minecraft.font.width(tierPrompt) / 2, y + 15, color, false);
            pGuiGraphics.drawString(this.minecraft.font, patternSize, x + this.f_93618_ / 2 - this.minecraft.font.width(patternSize) / 2, y + 35, 4210752, false);
            if (this.pattern.getFactionRequirement() != null) {
                int xPadding = 3;
                this.renderFactionIcon(pGuiGraphics, this.pattern.getFactionRequirement(), textX + stringWidth + xPadding, textY);
            }
        }
    }

    @Override
    public int getTier() {
        return this.pattern != null ? this.pattern.getTier() : 1;
    }
}