package com.sihenzhang.crockpot.integration.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.integration.jei.gui.requirement.AbstractDrawableRequirement;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class CrockPotCookingRecipeCategory implements IRecipeCategory<CrockPotCookingRecipe> {

    public static final RecipeType<CrockPotCookingRecipe> RECIPE_TYPE = RecipeType.create("crockpot", "crock_pot_cooking", CrockPotCookingRecipe.class);

    private final IDrawable background;

    private final IDrawable icon;

    private final IDrawable priority;

    private final IDrawable time;

    private final LoadingCache<CrockPotCookingRecipe, List<AbstractDrawableRequirement<? extends IRequirement>>> cachedDrawables;

    public CrockPotCookingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation recipeGui = RLUtils.createRL("textures/gui/jei/crock_pot_cooking.png");
        this.background = guiHelper.createDrawable(recipeGui, 0, 0, 176, 133);
        this.icon = guiHelper.createDrawable(ModIntegrationJei.ICONS, 80, 0, 16, 16);
        this.priority = guiHelper.createDrawable(recipeGui, 176, 0, 16, 16);
        this.time = guiHelper.createDrawable(recipeGui, 176, 16, 16, 16);
        this.cachedDrawables = CacheBuilder.newBuilder().maximumSize(32L).build(new CacheLoader<CrockPotCookingRecipe, List<AbstractDrawableRequirement<? extends IRequirement>>>() {

            public List<AbstractDrawableRequirement<? extends IRequirement>> load(CrockPotCookingRecipe key) {
                return AbstractDrawableRequirement.getDrawables(key.getRequirements());
            }
        });
    }

    @Override
    public RecipeType<CrockPotCookingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return I18nUtils.createIntegrationComponent("jei", "crock_pot_cooking");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, CrockPotCookingRecipe recipe, IFocusGroup focuses) {
        int xOffset = 2;
        int yOffset = 2;
        int maxWidth = 0;
        for (AbstractDrawableRequirement<? extends IRequirement> drawable : (List) this.cachedDrawables.getUnchecked(recipe)) {
            if (!drawable.getInvisibleInputs().isEmpty()) {
                builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(drawable.getInvisibleInputs());
            }
            if (yOffset != 2 && yOffset + drawable.getHeight() > 96) {
                xOffset += maxWidth + 2;
                yOffset = 2;
                maxWidth = 0;
            }
            List<AbstractDrawableRequirement.GuiItemStacksInfo> guiItemStacksInfos = drawable.getGuiItemStacksInfos(xOffset, yOffset);
            guiItemStacksInfos.forEach(guiItemStacksInfo -> builder.addSlot(guiItemStacksInfo.role, guiItemStacksInfo.x, guiItemStacksInfo.y).addItemStacks(guiItemStacksInfo.stacks));
            maxWidth = Math.max(drawable.getWidth(), maxWidth);
            yOffset += drawable.getHeight() + 2;
        }
        List<ItemStack> pots = ForgeRegistries.BLOCKS.tags().getTag(CrockPotBlockTags.CROCK_POTS).stream().filter(CrockPotBlock.class::isInstance).map(CrockPotBlock.class::cast).filter(pot -> pot.getPotLevel() >= recipe.getPotLevel()).map(block -> block.m_5456_().getDefaultInstance()).toList();
        builder.addSlot(RecipeIngredientRole.CATALYST, 62, 104).addItemStacks(pots);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 110).addItemStack(recipe.getResult());
    }

    public void draw(CrockPotCookingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        int cookingTime = recipe.getCookingTime();
        if (cookingTime > 0) {
            this.time.draw(guiGraphics, 0, 117);
            guiGraphics.drawString(font, I18nUtils.createIntegrationComponent("jei", "crock_pot_cooking.cooking_time.second", cookingTime / 20), 17, 121, -8355712, false);
        }
        String priorityString = String.valueOf(recipe.getPriority());
        int priorityWidth = font.width(priorityString);
        this.priority.draw(guiGraphics, 159 - priorityWidth, 117);
        guiGraphics.drawString(font, priorityString, 175 - priorityWidth, 121, -8355712, false);
        int xOffset = 2;
        int yOffset = 2;
        int maxWidth = 0;
        for (AbstractDrawableRequirement<? extends IRequirement> drawable : (List) this.cachedDrawables.getUnchecked(recipe)) {
            if (yOffset != 2 && yOffset + drawable.getHeight() > 96) {
                xOffset += maxWidth + 2;
                yOffset = 2;
                maxWidth = 0;
            }
            drawable.draw(guiGraphics, xOffset, yOffset);
            maxWidth = Math.max(drawable.getWidth(), maxWidth);
            yOffset += drawable.getHeight() + 2;
        }
    }

    public List<Component> getTooltipStrings(CrockPotCookingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 0.0 && mouseX <= 16.0 && mouseY >= 117.0 && mouseY <= 133.0) {
            return List.of(I18nUtils.createIntegrationComponent("jei", "crock_pot_cooking.cooking_time"));
        } else {
            String priorityString = String.valueOf(recipe.getPriority());
            int priorityWidth = Minecraft.getInstance().font.width(priorityString);
            return mouseX >= 159.0 - (double) priorityWidth && mouseX <= 175.0 - (double) priorityWidth && mouseY >= 117.0 && mouseY <= 133.0 ? List.of(I18nUtils.createIntegrationComponent("jei", "crock_pot_cooking.priority")) : IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
        }
    }
}