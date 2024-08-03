package com.mna.guide.recipe;

import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.recipes.multiblock.MultiblockDefinition;
import com.mna.tools.render.MultiblockRenderer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.commons.lang3.mutable.MutableInt;

public class RecipeMaterialsList extends RecipeRendererBase {

    private MultiblockDefinition _recipe;

    private List<List<ItemStack>> _ingredients;

    private static final int ITEMSTACK_WIDTH = 16;

    private boolean visualizeHovered = false;

    public RecipeMaterialsList(int xIn, int yIn) {
        super(xIn, yIn);
    }

    @Override
    protected void drawForeground(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (this._recipe != null) {
            if (this._ingredients == null) {
                this._ingredients = this._recipe.getItemsList(this.minecraft.level);
            }
            if (this._ingredients != null) {
                int rX = (int) (16.0F * this.scale);
                int rY = (int) ((float) this.m_252907_() / this.scale + 35.0F + 9.0F);
                int column_width = (int) (24.0F * this.scale);
                for (List<ItemStack> stacks : this._ingredients) {
                    this.renderItemStack(pGuiGraphics, stacks, x + rX, rY, 1.0F);
                    rX = (int) ((float) rX + 16.0F / this.scale + (float) column_width);
                    if ((float) rX >= (float) this.f_93618_ - 16.0F * this.scale - (float) column_width) {
                        rY += 20;
                        rX = (int) (16.0F * this.scale);
                    }
                }
                int tier = this._recipe.getTier();
                MutableInt playerTier = new MutableInt(0);
                this.minecraft.player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> playerTier.setValue(p.getTier()));
                int color = tier <= playerTier.getValue() ? FastColor.ARGB32.color(255, 0, 128, 0) : FastColor.ARGB32.color(255, 255, 0, 0);
                Component name = Component.translatable("mna:multiblock_recipe").append(Component.translatable(this._recipe.m_6423_().toString()));
                Component tierPrompt = Component.translatable("gui.mna.item-tier", tier);
                int tierWidth = this.minecraft.font.width(tierPrompt);
                int tierX = x + this.f_93618_ / 2 - tierWidth / 2;
                pGuiGraphics.drawString(this.minecraft.font, name, x + this.f_93618_ / 2 - this.minecraft.font.width(name) / 2, y + 10, FastColor.ARGB32.color(255, 255, 255, 255), false);
                pGuiGraphics.drawString(this.minecraft.font, tierPrompt, tierX, y + 20, color, false);
                this.drawVisualizeButton(pGuiGraphics, x + this.f_93618_ / 2, y + 20 + 9, mouseX, mouseY);
            }
        }
    }

    private void drawVisualizeButton(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY) {
        int width = 9;
        int height = 9;
        x -= width / 2;
        pGuiGraphics.blit(GuiTextures.Widgets.VISUALIZE_MULTIBLOCK, x, y, 0.0F, 0.0F, width, height, width, height);
        this.visualizeHovered = false;
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            this.visualizeHovered = true;
            List<Component> toolTip = Arrays.asList(Component.translatable("gui.mna:visualize_multiblock"));
            this.getTooltipFunction().accept(toolTip);
        }
    }

    @Override
    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        if (this.visualizeHovered) {
            this.m_7435_(Minecraft.getInstance().getSoundManager());
            MultiblockRenderer.setMultiblock(this._recipe, Component.translatable(this._recipe.m_6423_().toString()), false);
            this.minecraft.setScreen(null);
            return true;
        } else {
            return super.m_6375_(p_231044_1_, p_231044_3_, p_231044_5_);
        }
    }

    @Override
    protected ResourceLocation backgroundTexture() {
        return null;
    }

    @Override
    protected void init_internal(ResourceLocation recipeLocation) {
        Optional<? extends Recipe<?>> pattern = this.minecraft.level.getRecipeManager().byKey(recipeLocation);
        if (pattern.isPresent() && pattern.get() instanceof MultiblockDefinition) {
            this._recipe = (MultiblockDefinition) pattern.get();
        }
    }

    @Override
    public int getTier() {
        return 0;
    }
}