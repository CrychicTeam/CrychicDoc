package com.mna.interop.jei.categories;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.blocks.BlockInit;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.interop.jei.MARecipeTypes;
import com.mna.recipes.runeforging.RunescribingRecipe;
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

public class RunescribingRecipeCategory implements IRecipeCategory<RunescribingRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    private final String localizedName;

    private int xSize = 144;

    private int ySize = 169;

    public RunescribingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GuiTextures.Jei.RUNESCRIBING, 0, 0, this.xSize, this.ySize).setTextureSize(144, 169).build();
        this.localizedName = I18n.get("gui.mna.jei.runescribing");
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.RUNESCRIBING_TABLE.get()));
    }

    @Override
    public RecipeType<RunescribingRecipe> getRecipeType() {
        return MARecipeTypes.RUNESCRIBING;
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

    public void setRecipe(IRecipeLayoutBuilder builder, RunescribingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, this.xSize / 2 - 7, 36).addIngredient(VanillaTypes.ITEM_STACK, recipe.getResultItem());
    }

    public void draw(RunescribingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics pGuiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        mc.player.getCapability(PlayerProgressionProvider.PROGRESSION).resolve().get();
        int startX = 24;
        int startY = 72;
        int grid = 12;
        int count = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                if ((recipe.getHMutex() & 1L << count) != 0L) {
                    pGuiGraphics.blit(GuiTextures.Widgets.GUIDE_WIDGETS, startX + j * grid, startY + i * grid, 26.0F, 0.0F, grid, 1, 256, 256);
                }
                if ((recipe.getVMutex() & 1L << count) != 0L) {
                    pGuiGraphics.blit(GuiTextures.Widgets.GUIDE_WIDGETS, startX + (i + 1) * grid, startY + (j - 1) * grid, 26.0F, 0.0F, 1, grid, 256, 256);
                }
                count++;
            }
        }
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
        }
    }
}