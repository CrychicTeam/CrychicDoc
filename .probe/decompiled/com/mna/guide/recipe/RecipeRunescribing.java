package com.mna.guide.recipe;

import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.recipes.runeforging.RunescribingRecipe;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.commons.lang3.mutable.MutableInt;

public class RecipeRunescribing extends RecipeRendererBase {

    private RunescribingRecipe pattern;

    static final int POINT_RENDER_SIZE = 13;

    private boolean disableLabels = false;

    private boolean disableOutputRender = false;

    public RecipeRunescribing(int x, int y) {
        super(x, y);
    }

    @Override
    public void init_internal(ResourceLocation recipeLocation) {
        Optional<? extends Recipe<?>> pattern = this.minecraft.level.getRecipeManager().byKey(recipeLocation);
        if (pattern.isPresent() && pattern.get() instanceof RunescribingRecipe) {
            this.pattern = (RunescribingRecipe) pattern.get();
        } else {
            this.pattern = null;
        }
    }

    @Override
    protected ResourceLocation backgroundTexture() {
        return GuiTextures.Recipe.RUNESCRIBING;
    }

    @Override
    protected void drawForeground(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (this.pattern != null) {
            float scaleX = 1.5F;
            float scaleY = 1.5F;
            int startX = (int) ((float) (x + 37) / scaleX);
            int startY = (int) ((float) (y + 110) / scaleY);
            int grid = 12;
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().scale(scaleX, scaleY, 1.0F);
            int count = 0;
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((this.pattern.getHMutex() & 1L << count) != 0L) {
                        pGuiGraphics.blit(GuiTextures.Widgets.GUIDE_WIDGETS, startX + j * grid, startY + i * grid, 26, 0, grid, 1);
                    }
                    if ((this.pattern.getVMutex() & 1L << count) != 0L) {
                        pGuiGraphics.blit(GuiTextures.Widgets.GUIDE_WIDGETS, startX + (i + 1) * grid, startY + (j - 1) * grid, 26, 0, 1, grid);
                    }
                    count++;
                }
            }
            pGuiGraphics.pose().popPose();
            if (!this.disableOutputRender) {
                ItemStack output = this.pattern.getResultItem();
                this.renderItemStack(pGuiGraphics, output, (int) ((float) this.m_252754_() / this.scale + (float) (this.f_93618_ / 2 - 8)), (int) ((float) this.m_252907_() / this.scale + 57.0F), 1.0F);
            }
            if (!this.disableLabels) {
                int tier = this.pattern.getTier();
                MutableInt playerTier = new MutableInt(0);
                this.minecraft.player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> playerTier.setValue(p.getTier()));
                int color = tier <= playerTier.getValue() ? FastColor.ARGB32.color(255, 0, 128, 0) : FastColor.ARGB32.color(255, 255, 0, 0);
                Component name = Component.translatable(this.pattern.getResultItem().getDescriptionId().toString());
                Component tierPrompt = Component.translatable("gui.mna.item-tier", tier);
                int stringWidth = this.minecraft.font.width(name);
                int textX = x + this.f_93618_ / 2 - stringWidth / 2;
                int textY = y + 5;
                pGuiGraphics.drawString(this.minecraft.font, name, textX, textY, FastColor.ARGB32.color(255, 255, 255, 255), false);
                pGuiGraphics.drawString(this.minecraft.font, tierPrompt, x + this.f_93618_ / 2 - this.minecraft.font.width(tierPrompt) / 2, y + 15, color, false);
                if (this.pattern.getFactionRequirement() != null) {
                    int xPadding = 3;
                    this.renderFactionIcon(pGuiGraphics, this.pattern.getFactionRequirement(), textX + stringWidth + xPadding, textY);
                }
            }
        }
    }

    @Override
    public int getTier() {
        return this.pattern != null ? this.pattern.getTier() : 1;
    }

    public String getOutputTranslationKey() {
        return this.pattern == null ? "" : this.pattern.getResultItem().getDescriptionId().toString();
    }

    public void disableLabels() {
        this.disableLabels = true;
    }

    public void disableOutputRender() {
        this.disableOutputRender = true;
    }
}