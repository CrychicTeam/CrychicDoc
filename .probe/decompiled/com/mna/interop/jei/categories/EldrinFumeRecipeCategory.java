package com.mna.interop.jei.categories;

import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.tools.MATags;
import com.mna.blocks.BlockInit;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.interop.jei.MARecipeTypes;
import com.mna.recipes.eldrin.FumeFilterRecipe;
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

public class EldrinFumeRecipeCategory implements IRecipeCategory<FumeFilterRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    private final String localizedName;

    private int xSize = 152;

    private int ySize = 177;

    public EldrinFumeRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GuiTextures.Jei.FUME, 0, 0, this.xSize, this.ySize).setTextureSize(152, 177).build();
        this.localizedName = I18n.get("gui.mna.jei.eldrin_fume");
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.ELDRIN_FUME.get()));
    }

    @Override
    public RecipeType<FumeFilterRecipe> getRecipeType() {
        return MARecipeTypes.FUME;
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

    public void setRecipe(IRecipeLayoutBuilder builder, FumeFilterRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 64).addIngredients(Ingredient.of(MATags.smartLookupItem(recipe.getItemOrTagID()).stream().map(i -> new ItemStack(i))));
    }

    public void draw(FumeFilterRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics pGuiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.font != null) {
            int tier = recipe.getTier();
            int playerTier = ((IPlayerProgression) mc.player.getCapability(PlayerProgressionProvider.PROGRESSION).resolve().get()).getTier();
            int color = tier <= playerTier ? FastColor.ARGB32.color(255, 0, 128, 0) : FastColor.ARGB32.color(255, 255, 0, 0);
            Component name = Component.translatable("gui.mna.jei.eldrin_fume.generation");
            Component tierPrompt = Component.translatable("gui.mna.item-tier", tier);
            int stringWidth = mc.font.width(name);
            int textX = this.xSize / 2 - stringWidth / 2;
            int textY = 5;
            pGuiGraphics.drawString(mc.font, name, textX, textY, FastColor.ARGB32.color(255, 255, 255, 255), false);
            pGuiGraphics.drawString(mc.font, tierPrompt, this.xSize / 2 - mc.font.width(tierPrompt) / 2, 15, color, false);
            GuiRenderUtils.renderFactionIcon(pGuiGraphics, recipe.getFactionRequirement(), textX + stringWidth + 3, textY);
            Affinity a = recipe.getAffinity();
            int affX = this.xSize - 40;
            int rY = 63;
            float scale = 0.7F;
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().scale(scale, scale, scale);
            String vText = String.format("%s: %.0f", a.name(), recipe.getTotalGeneration() * 0.01F);
            affX = (int) ((float) affX - (float) (mc.font.width(vText) / 2) * scale);
            if (a == Affinity.WIND) {
                pGuiGraphics.drawString(mc.font, vText, (int) ((float) affX / scale), (int) ((float) rY / scale), FastColor.ARGB32.color(255, 0, 0, 0), false);
            } else {
                pGuiGraphics.drawString(mc.font, vText, (int) ((float) affX / scale), (int) ((float) rY / scale), FastColor.ARGB32.color(255, a.getColor()[0], a.getColor()[1], a.getColor()[2]), false);
            }
            pGuiGraphics.pose().popPose();
        }
    }
}