package com.mna.guide.recipe;

import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.recipes.arcanefurnace.ArcaneFurnaceRecipe;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.mutable.MutableInt;

public class RecipeArcaneFurnace extends RecipeRendererBase {

    private ArcaneFurnaceRecipe recipe;

    public RecipeArcaneFurnace(int xIn, int yIn) {
        super(xIn, yIn);
    }

    @Override
    protected void drawForeground(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (this.recipe != null) {
            Item input = ForgeRegistries.ITEMS.getValue(this.recipe.getInputItem());
            Item output = ForgeRegistries.ITEMS.getValue(this.recipe.getOutputItem());
            if (input != null) {
                this.renderItemStack(pGuiGraphics, new ItemStack(input), (int) ((float) this.m_252754_() / this.scale + 27.0F), (int) ((float) this.m_252907_() / this.scale + 69.0F), 2.0F);
            }
            if (output != null) {
                this.renderItemStack(pGuiGraphics, new ItemStack(output), (int) ((float) this.m_252754_() / this.scale + 159.0F), (int) ((float) this.m_252907_() / this.scale + 69.0F), 2.0F);
            }
            this.renderByproducts(pGuiGraphics, this.m_5711_() / 2 - 25, 50, this.recipe);
            int tier = this.recipe.getTier();
            MutableInt playerTier = new MutableInt(0);
            this.minecraft.player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> playerTier.setValue(p.getTier()));
            int color = tier <= playerTier.getValue() ? FastColor.ARGB32.color(255, 0, 128, 0) : FastColor.ARGB32.color(255, 255, 0, 0);
            Component name = Component.translatable(this.recipe.getResultItem().getDescriptionId().toString());
            Component tierPrompt = Component.translatable("gui.mna.item-tier", tier);
            int stringWidth = this.minecraft.font.width(name);
            int textX = x + this.f_93618_ / 2 - stringWidth / 2;
            int textY = y + 5;
            pGuiGraphics.drawString(this.minecraft.font, name, textX, textY, FastColor.ARGB32.color(255, 255, 255, 255), false);
            pGuiGraphics.drawString(this.minecraft.font, tierPrompt, x + this.f_93618_ / 2 - this.minecraft.font.width(tierPrompt) / 2, y + 15, color, false);
            if (this.recipe.getFactionRequirement() != null) {
                int xPadding = 3;
                this.renderFactionIcon(pGuiGraphics, this.recipe.getFactionRequirement(), textX + stringWidth + xPadding, textY);
            }
        }
    }

    @Override
    protected ResourceLocation backgroundTexture() {
        return GuiTextures.Recipe.RUNEFORGE;
    }

    @Override
    public void init_internal(ResourceLocation recipeLocation) {
        Optional<? extends Recipe<?>> pattern = this.minecraft.level.getRecipeManager().byKey(recipeLocation);
        if (pattern.isPresent() && pattern.get() instanceof ArcaneFurnaceRecipe) {
            this.recipe = (ArcaneFurnaceRecipe) pattern.get();
        }
    }

    @Override
    public int getTier() {
        return this.recipe != null ? this.recipe.getTier() : 1;
    }
}