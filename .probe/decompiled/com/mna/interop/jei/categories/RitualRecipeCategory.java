package com.mna.interop.jei.categories;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.rituals.IRitualReagent;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.interop.jei.MARecipeTypes;
import com.mna.items.ItemInit;
import com.mna.recipes.rituals.RitualRecipe;
import com.mna.tools.render.GuiRenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class RitualRecipeCategory implements IRecipeCategory<RitualRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    private final String localizedName;

    private int xSize = 144;

    private int ySize = 169;

    public RitualRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GuiTextures.Jei.RITUAL, 0, 0, this.xSize, this.ySize).setTextureSize(144, 169).build();
        this.localizedName = I18n.get("gui.mna.jei.ritual");
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ItemInit.RUNE_RITUAL_METAL.get()));
    }

    @Override
    public RecipeType<RitualRecipe> getRecipeType() {
        return MARecipeTypes.RITUAL;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(this.localizedName);
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, RitualRecipe recipe, IFocusGroup focuses) {
        int xc = this.xSize / 2 - 8;
        int yc = this.ySize / 2 + 3;
        int bounds = recipe.getLowerBound();
        IRitualReagent[][] reagents = recipe.getReagents();
        int xPosOrigin = xc + 16 * bounds;
        int yPos = yc - 16 * bounds;
        for (int i = 0; i < reagents.length; i++) {
            int xPos = xPosOrigin;
            for (int j = 0; j < reagents[i].length; j++) {
                if (reagents[i][j] != null && !reagents[i][j].isEmpty() && !reagents[i][j].isDynamic()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, xPos, yPos).addIngredients(Ingredient.of(MATags.smartLookupItem(reagents[i][j].getResourceLocation()).stream().map(item -> new ItemStack(item))));
                }
                xPos -= 16;
            }
            yPos += 16;
        }
        if (!recipe.getResultItem().isEmpty()) {
            builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredient(VanillaTypes.ITEM_STACK, recipe.getResultItem());
        }
    }

    public void draw(RitualRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics pGuiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.font != null) {
            int tier = recipe.getTier();
            int playerTier = ((IPlayerProgression) mc.player.getCapability(PlayerProgressionProvider.PROGRESSION).resolve().get()).getTier();
            int color = tier <= playerTier ? FastColor.ARGB32.color(255, 0, 128, 0) : FastColor.ARGB32.color(255, 255, 0, 0);
            Component name = Component.translatable(recipe.m_6423_().toString());
            Component tierPrompt = Component.translatable("gui.mna.item-tier", tier);
            int stringWidth = mc.font.width(name);
            int textX = this.xSize / 2 - stringWidth / 2;
            int textY = 5;
            pGuiGraphics.drawString(mc.font, name, textX, textY, FastColor.ARGB32.color(255, 255, 255, 255), false);
            pGuiGraphics.drawString(mc.font, tierPrompt, this.xSize / 2 - mc.font.width(tierPrompt) / 2, 15, color, false);
            GuiRenderUtils.renderFactionIcon(pGuiGraphics, recipe.getFactionRequirement(), textX + stringWidth + 3, textY);
        }
        int squareSize = 16;
        int xc = this.xSize / 2 - 9;
        int yc = this.ySize / 2 + 2;
        int bounds = recipe.getLowerBound();
        int xPosOrigin = xc + squareSize * bounds;
        int yPos = yc - squareSize * bounds;
        RenderSystem.enableBlend();
        int[][] pattern = recipe.getPattern();
        for (int i = 0; i < pattern.length; i++) {
            int var24 = xPosOrigin;
            for (int j = 0; j < pattern[i].length; j++) {
                if (pattern[i][j] != 0) {
                    pGuiGraphics.blit(GuiTextures.Widgets.WIDGETS, var24 + 1, yPos + 1, 112.0F, 0.0F, squareSize, squareSize, 128, 128);
                }
                var24 -= squareSize;
            }
            yPos += squareSize;
        }
        RenderSystem.disableBlend();
    }
}