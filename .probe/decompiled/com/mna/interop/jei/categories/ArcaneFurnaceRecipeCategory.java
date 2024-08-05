package com.mna.interop.jei.categories;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.tools.MATags;
import com.mna.blocks.BlockInit;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.interop.jei.MARecipeTypes;
import com.mna.recipes.arcanefurnace.ArcaneFurnaceRecipe;
import com.mna.tools.render.GuiRenderUtils;
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

public class ArcaneFurnaceRecipeCategory implements IRecipeCategory<ArcaneFurnaceRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    private final String localizedName;

    private int xSize = 152;

    private int ySize = 177;

    public ArcaneFurnaceRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GuiTextures.Jei.RUNEFORGE, 0, 0, this.xSize, this.ySize).setTextureSize(152, 177).build();
        this.localizedName = I18n.get("gui.mna.jei.arcane_furnace");
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.RUNEFORGE.get()));
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

    public void setRecipe(IRecipeLayoutBuilder builder, ArcaneFurnaceRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 22, 51).addIngredients(Ingredient.of(MATags.smartLookupItem(recipe.getInputItem()).stream().map(i -> new ItemStack(i))));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 114, 51).addIngredient(VanillaTypes.ITEM_STACK, recipe.getResultItem());
    }

    public void draw(ArcaneFurnaceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics pGuiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.font != null) {
            int tier = recipe.getTier();
            int playerTier = ((IPlayerProgression) mc.player.getCapability(PlayerProgressionProvider.PROGRESSION).resolve().get()).getTier();
            int color = tier <= playerTier ? FastColor.ARGB32.color(255, 0, 128, 0) : FastColor.ARGB32.color(255, 255, 0, 0);
            Component name = Component.translatable(recipe.getResultItem().getDescriptionId().toString());
            Component tierPrompt = Component.translatable("gui.mna.item-tier", tier);
            int stringWidth = mc.font.width(name);
            int textX = this.xSize / 2 - stringWidth / 2;
            int textY = 5;
            pGuiGraphics.drawString(mc.font, name, textX, textY, FastColor.ARGB32.color(255, 255, 255, 255), false);
            pGuiGraphics.drawString(mc.font, tierPrompt, this.xSize / 2 - mc.font.width(tierPrompt) / 2, 15, color, false);
            GuiRenderUtils.renderFactionIcon(pGuiGraphics, recipe.getFactionRequirement(), textX + stringWidth + 3, textY);
            GuiRenderUtils.renderByproducts(pGuiGraphics, this.xSize / 2 - 24, 34, recipe, false);
        }
    }

    @Override
    public RecipeType<ArcaneFurnaceRecipe> getRecipeType() {
        return MARecipeTypes.ARCANE_FURNACE;
    }
}