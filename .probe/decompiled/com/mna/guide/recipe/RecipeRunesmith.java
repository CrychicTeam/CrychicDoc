package com.mna.guide.recipe;

import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.recipes.runeforging.RuneforgingRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.mutable.MutableInt;

public class RecipeRunesmith extends RecipeRendererBase {

    private RuneforgingRecipe pattern;

    static final int POINT_RENDER_SIZE = 13;

    private ItemStack _metal;

    private ItemStack _pattern;

    private ItemStack _output;

    public RecipeRunesmith(int x, int y) {
        super(x, y);
    }

    @Override
    public void init_internal(ResourceLocation recipeLocation) {
        Optional<? extends Recipe<?>> pattern = this.minecraft.level.getRecipeManager().byKey(recipeLocation);
        if (pattern.isPresent() && pattern.get() instanceof RuneforgingRecipe) {
            this.pattern = (RuneforgingRecipe) pattern.get();
            this._metal = new ItemStack(ForgeRegistries.ITEMS.getValue(this.pattern.getMaterial()));
            this._pattern = new ItemStack(ForgeRegistries.ITEMS.getValue(this.pattern.getPatternResource()));
            this._output = this.pattern.getResultItem();
        } else {
            this.pattern = null;
        }
    }

    @Override
    protected ResourceLocation backgroundTexture() {
        return GuiTextures.Recipe.RUNESMITHING;
    }

    @Override
    protected void drawForeground(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (this.pattern != null) {
            RenderSystem.setShaderTexture(0, GuiTextures.Widgets.GUIDE_WIDGETS);
            this.renderItemStack(pGuiGraphics, this._pattern, (int) ((float) this.m_252754_() / this.scale + 51.0F), (int) ((float) this.m_252907_() / this.scale + 108.0F), 1.0F);
            this.renderItemStack(pGuiGraphics, this._metal, (int) ((float) this.m_252754_() / this.scale + 51.0F), (int) ((float) this.m_252907_() / this.scale + 64.0F), 1.0F);
            this.renderItemStack(pGuiGraphics, this._output, (int) ((float) this.m_252754_() / this.scale) + 180, (int) ((float) this.m_252907_() / this.scale + 90.0F), 1.0F);
            String hits = String.format("x %d", this.pattern.getHits());
            pGuiGraphics.drawString(this.minecraft.font, hits, x + 123 - this.minecraft.font.width(hits) / 2, y + 100, -12105913, false);
            if (this._output.getCount() > 1) {
                String count = String.format("x %d", this._output.getCount());
                pGuiGraphics.drawString(this.minecraft.font, count, x + 186 - this.minecraft.font.width(count) / 2, y + 110, -12105913, false);
            }
            this.renderByproducts(pGuiGraphics, this.m_5711_() - 55, this.m_93694_() / 2 - 5, this.pattern);
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

    @Override
    public int getTier() {
        return this.pattern != null ? this.pattern.getTier() : 1;
    }
}