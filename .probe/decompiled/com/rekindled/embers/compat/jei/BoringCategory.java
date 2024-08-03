package com.rekindled.embers.compat.jei;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.recipe.IBoringRecipe;
import com.rekindled.embers.util.Misc;
import java.util.ArrayList;
import java.util.List;
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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;

public class BoringCategory implements IRecipeCategory<IBoringRecipe> {

    private final IDrawable background;

    public IDrawable icon;

    public static Component title = Component.translatable("embers.jei.recipe.boring");

    public static ResourceLocation texture = new ResourceLocation("embers", "textures/gui/jei_boring.png");

    public BoringCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(texture, 0, 0, 126, 98);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryManager.EMBER_BORE_ITEM.get()));
    }

    @Override
    public RecipeType<IBoringRecipe> getRecipeType() {
        return JEIPlugin.BORING;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IBoringRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 6, 6).addItemStack(recipe.getDisplayOutput().getStack());
        builder.addSlot(RecipeIngredientRole.CATALYST, 6, 26).addItemStacks(recipe.getDisplayInput());
    }

    public List<Component> getTooltipStrings(IBoringRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        int height = 48;
        List<Component> text = new ArrayList();
        if (recipe.getMinHeight() != Integer.MIN_VALUE) {
            height += 11;
        }
        if (recipe.getMaxHeight() != Integer.MAX_VALUE) {
            height += 11;
        }
        if (!recipe.getDimensions().isEmpty()) {
            if (mouseY > (double) height && mouseY < (double) (height + 11) && mouseX > 7.0 && mouseX < 119.0) {
                for (ResourceLocation dimension : recipe.getDimensions()) {
                    text.add(Component.translatableWithFallback("dimension." + dimension.toLanguageKey(), WordUtils.capitalize(dimension.getPath().replace("_", " "))));
                }
            }
            height += 11;
        }
        if (!recipe.getBiomes().isEmpty()) {
            if (mouseY > (double) height && mouseY < (double) (height + 11) && mouseX > 7.0 && mouseX < 119.0) {
                for (ResourceLocation biome : recipe.getBiomes()) {
                    text.add(Component.literal(biome.toString()));
                }
            }
            height += 11;
        }
        return text;
    }

    public void draw(IBoringRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font fontRenderer = Minecraft.getInstance().font;
        Misc.drawComponents(fontRenderer, guiGraphics, 28, 10, Component.translatable("embers.jei.recipe.boring.weight", recipe.getDisplayOutput().m_142631_()));
        Misc.drawComponents(fontRenderer, guiGraphics, 28, 30, Component.translatable("embers.jei.recipe.boring.required_blocks"));
        List<Component> text = new ArrayList();
        if (recipe.getMinHeight() != Integer.MIN_VALUE) {
            text.add(Component.translatable("embers.jei.recipe.boring.min_height", recipe.getMinHeight()));
        }
        if (recipe.getMaxHeight() != Integer.MAX_VALUE) {
            text.add(Component.translatable("embers.jei.recipe.boring.max_height", recipe.getMaxHeight()));
        }
        if (!recipe.getDimensions().isEmpty()) {
            text.add(Component.translatable("embers.jei.recipe.boring.dimensions").withStyle(style -> style.withColor(16758093)));
        }
        if (!recipe.getBiomes().isEmpty()) {
            text.add(Component.translatable("embers.jei.recipe.boring.biomes").withStyle(style -> style.withColor(16758093)));
        }
        Component[] components = new Component[text.size()];
        for (int i = 0; i < text.size(); i++) {
            components[i] = (Component) text.get(i);
        }
        Misc.drawComponents(fontRenderer, guiGraphics, 10, 48, components);
    }
}