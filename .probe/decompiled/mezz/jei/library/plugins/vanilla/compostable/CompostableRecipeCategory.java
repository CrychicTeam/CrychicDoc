package mezz.jei.library.plugins.vanilla.compostable;

import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.vanilla.IJeiCompostingRecipe;
import mezz.jei.common.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class CompostableRecipeCategory implements IRecipeCategory<IJeiCompostingRecipe> {

    public static final int width = 120;

    public static final int height = 18;

    private final IDrawable background;

    private final IDrawable slot;

    private final IDrawable icon;

    private final Component localizedName;

    public CompostableRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(120, 18);
        this.slot = guiHelper.getSlotDrawable();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(Blocks.COMPOSTER));
        this.localizedName = Component.translatable("gui.jei.category.compostable");
    }

    @Override
    public RecipeType<IJeiCompostingRecipe> getRecipeType() {
        return RecipeTypes.COMPOSTING;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IJeiCompostingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStacks(recipe.getInputs());
    }

    public void draw(IJeiCompostingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.slot.draw(guiGraphics);
        float chance = recipe.getChance();
        int chancePercent = (int) Math.floor((double) (chance * 100.0F));
        String text = Translator.translateToLocalFormatted("gui.jei.category.compostable.chance", chancePercent);
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        guiGraphics.drawString(font, text, 24, 5, -8355712, false);
    }
}