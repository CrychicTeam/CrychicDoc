package mezz.jei.library.plugins.vanilla.brewing;

import java.util.List;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.common.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class BrewingRecipeCategory implements IRecipeCategory<IJeiBrewingRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    private final IDrawable slotDrawable;

    private final Component localizedName;

    private final IDrawableAnimated arrow;

    private final IDrawableAnimated bubbles;

    private final IDrawableStatic blazeHeat;

    public BrewingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = Constants.RECIPE_GUI_VANILLA;
        this.background = guiHelper.drawableBuilder(location, 0, 0, 64, 60).addPadding(1, 0, 0, 50).build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(Blocks.BREWING_STAND));
        this.localizedName = Component.translatable("gui.jei.category.brewing");
        this.arrow = guiHelper.drawableBuilder(location, 64, 0, 9, 28).buildAnimated(400, IDrawableAnimated.StartDirection.TOP, false);
        ITickTimer bubblesTickTimer = new BrewingRecipeCategory.BrewingBubblesTickTimer(guiHelper);
        this.bubbles = guiHelper.drawableBuilder(location, 73, 0, 12, 29).buildAnimated(bubblesTickTimer, IDrawableAnimated.StartDirection.BOTTOM);
        this.blazeHeat = guiHelper.createDrawable(location, 64, 29, 18, 4);
        this.slotDrawable = guiHelper.getSlotDrawable();
    }

    @Override
    public RecipeType<IJeiBrewingRecipe> getRecipeType() {
        return RecipeTypes.BREWING;
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

    public void draw(IJeiBrewingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.blazeHeat.draw(guiGraphics, 5, 30);
        this.bubbles.draw(guiGraphics, 8, 0);
        this.arrow.draw(guiGraphics, 42, 2);
        int brewingSteps = recipe.getBrewingSteps();
        String brewingStepsString = brewingSteps < Integer.MAX_VALUE ? Integer.toString(brewingSteps) : "?";
        Component steps = Component.translatable("gui.jei.category.brewing.steps", brewingStepsString);
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.drawString(minecraft.font, steps, 70, 28, -8355712, false);
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IJeiBrewingRecipe recipe, IFocusGroup focuses) {
        List<ItemStack> potionInputs = recipe.getPotionInputs();
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 37).addItemStacks(potionInputs);
        builder.addSlot(RecipeIngredientRole.INPUT, 24, 44).addItemStacks(potionInputs);
        builder.addSlot(RecipeIngredientRole.INPUT, 47, 37).addItemStacks(potionInputs);
        builder.addSlot(RecipeIngredientRole.INPUT, 24, 3).addItemStacks(recipe.getIngredients());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 3).addItemStack(recipe.getPotionOutput()).setBackground(this.slotDrawable, -1, -1);
    }

    private static class BrewingBubblesTickTimer implements ITickTimer {

        private static final int[] BUBBLE_LENGTHS = new int[] { 29, 23, 18, 13, 9, 5, 0 };

        private final ITickTimer internalTimer;

        public BrewingBubblesTickTimer(IGuiHelper guiHelper) {
            this.internalTimer = guiHelper.createTickTimer(14, BUBBLE_LENGTHS.length - 1, false);
        }

        @Override
        public int getValue() {
            int timerValue = this.internalTimer.getValue();
            return BUBBLE_LENGTHS[timerValue];
        }

        @Override
        public int getMaxValue() {
            return BUBBLE_LENGTHS[0];
        }
    }
}