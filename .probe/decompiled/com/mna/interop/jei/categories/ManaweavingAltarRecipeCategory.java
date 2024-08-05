package com.mna.interop.jei.categories;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.tools.MATags;
import com.mna.blocks.BlockInit;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.interop.jei.JEIInterop;
import com.mna.interop.jei.MARecipeTypes;
import com.mna.interop.jei.ingredients.ManaweavePatternIngredient;
import com.mna.recipes.manaweaving.ManaweavingRecipe;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

public class ManaweavingAltarRecipeCategory implements IRecipeCategory<ManaweavingRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    private final String localizedName;

    private int xSize = 152;

    private int ySize = 177;

    public ManaweavingAltarRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GuiTextures.Jei.MANAWEAVING_ALTAR, 0, 0, this.xSize, this.ySize).setTextureSize(152, 177).build();
        this.localizedName = I18n.get("gui.mna.jei.manaweaving_altar");
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.MANAWEAVING_ALTAR.get()));
    }

    @Override
    public RecipeType<ManaweavingRecipe> getRecipeType() {
        return MARecipeTypes.MANAWEAVING;
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

    public void setRecipe(IRecipeLayoutBuilder builder, ManaweavingRecipe recipe, IFocusGroup focuses) {
        int x = 29;
        int y = 30;
        int reagentIdx = 0;
        builder.setShapeless();
        for (ResourceLocation rLoc : recipe.getRequiredItems()) {
            builder.addSlot(RecipeIngredientRole.INPUT, x + reagentIdx % 3 * 22, y + (int) Math.floor((double) (reagentIdx / 3)) * 22).addIngredients(this.resolveIngredient(rLoc));
            reagentIdx++;
        }
        int xStep = 24;
        x = 8;
        int var13 = 150;
        for (ResourceLocation rLoc : recipe.getRequiredPatterns()) {
            builder.addSlot(RecipeIngredientRole.INPUT, x, var13).addIngredient(JEIInterop.MANAWEAVE_PATTERN, new ManaweavePatternIngredient(rLoc));
            reagentIdx++;
            x += xStep;
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 52).addIngredient(VanillaTypes.ITEM_STACK, recipe.getResultItem());
    }

    private Ingredient resolveIngredient(ResourceLocation rLoc) {
        return Ingredient.of(MATags.smartLookupItem(rLoc).stream().map(item -> {
            ItemStack stack = new ItemStack(item);
            if (stack.getItem() == Items.POTION && stack.getTag() == null) {
                stack = PotionUtils.setPotion(stack, Potions.WATER);
            }
            return stack;
        }));
    }

    public void draw(ManaweavingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics pGuiGraphics, double mouseX, double mouseY) {
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
            GuiRenderUtils.renderByproducts(pGuiGraphics, this.xSize / 2 + 16, 78, recipe, false);
        }
    }
}