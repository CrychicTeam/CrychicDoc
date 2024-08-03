package com.mna.interop.jei.categories;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.interop.jei.MARecipeTypes;
import com.mna.items.ItemInit;
import com.mna.recipes.manaweaving.TransmutationRecipe;
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

public class TransmutationRecipeCategory implements IRecipeCategory<TransmutationRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    private final String localizedName;

    private int xSize = 152;

    private int ySize = 177;

    public TransmutationRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GuiTextures.Jei.TRANSMUTATION_MULTI, 0, 0, this.xSize, this.ySize).setTextureSize(152, 177).build();
        this.localizedName = I18n.get("gui.mna.jei.transmutation");
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ItemInit.MANAWEAVER_WAND_ADVANCED.get()));
    }

    @Override
    public RecipeType<TransmutationRecipe> getRecipeType() {
        return MARecipeTypes.TRANSMUTATION;
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

    public void setRecipe(IRecipeLayoutBuilder builder, TransmutationRecipe recipe, IFocusGroup focuses) {
        builder.setShapeless();
        builder.addSlot(RecipeIngredientRole.INPUT, 21, 80).addIngredients(Ingredient.of(MATags.smartLookupItem(recipe.getTargetBlock()).stream().map(i -> new ItemStack(i))));
        if (recipe.getReplaceBlock() != null) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 22, 123).addIngredients(Ingredient.of(MATags.smartLookupItem(recipe.getReplaceBlock()).stream().map(i -> new ItemStack(i))));
        } else if (recipe.getLootTable() != null) {
        }
        if (recipe.hasRepresentationItem()) {
            builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(Ingredient.of(MATags.smartLookupItem(recipe.getRepresentationItem()).stream().map(i -> new ItemStack(i))));
        }
    }

    public void draw(TransmutationRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics pGuiGraphics, double mouseX, double mouseY) {
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
            RenderSystem.enableBlend();
        }
    }
}