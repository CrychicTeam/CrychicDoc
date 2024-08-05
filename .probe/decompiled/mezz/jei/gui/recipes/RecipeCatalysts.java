package mezz.jei.gui.recipes;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.common.gui.elements.DrawableNineSliceTexture;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.input.ClickableIngredientInternal;
import mezz.jei.common.input.IClickableIngredientInternal;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.common.util.MathUtil;
import mezz.jei.gui.input.IRecipeFocusSource;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;

public class RecipeCatalysts implements IRecipeFocusSource {

    private static final int ingredientSize = 16;

    private static final int ingredientBorderSize = 1;

    private static final int borderSize = 5;

    private static final int overlapSize = 6;

    private final DrawableNineSliceTexture backgroundTab;

    private final List<IRecipeSlotDrawable> recipeSlots;

    private final DrawableNineSliceTexture slotBackground;

    private final IRecipeManager recipeManager;

    private int left = 0;

    private int top = 0;

    private int width = 0;

    private int height = 0;

    public RecipeCatalysts(Textures textures, IRecipeManager recipeManager) {
        this.recipeManager = recipeManager;
        this.recipeSlots = new ArrayList();
        this.backgroundTab = textures.getCatalystTab();
        this.slotBackground = textures.getRecipeCatalystSlotBackground();
    }

    public boolean isEmpty() {
        return this.recipeSlots.isEmpty();
    }

    @Nonnegative
    public int getWidth() {
        return Math.max(0, this.width - 6);
    }

    public void updateLayout(List<ITypedIngredient<?>> ingredients, ImmutableRect2i recipeArea) {
        this.recipeSlots.clear();
        if (!ingredients.isEmpty()) {
            int availableHeight = recipeArea.getHeight() - 8;
            int borderHeight = 12;
            int maxIngredientsPerColumn = (availableHeight - borderHeight) / 16;
            int columnCount = MathUtil.divideCeil(ingredients.size(), maxIngredientsPerColumn);
            maxIngredientsPerColumn = MathUtil.divideCeil(ingredients.size(), columnCount);
            this.width = 12 + columnCount * 16;
            this.height = 12 + maxIngredientsPerColumn * 16;
            this.top = recipeArea.getY();
            this.left = recipeArea.getX() - this.width + 6;
            for (int i = 0; i < ingredients.size(); i++) {
                ITypedIngredient<?> ingredientForSlot = (ITypedIngredient<?>) ingredients.get(i);
                IRecipeSlotDrawable recipeSlot = this.createSlot(ingredientForSlot, i, maxIngredientsPerColumn);
                this.recipeSlots.add(recipeSlot);
            }
        }
    }

    private <T> IRecipeSlotDrawable createSlot(ITypedIngredient<T> typedIngredient, int index, int maxIngredientsPerColumn) {
        int column = index / maxIngredientsPerColumn;
        int row = index % maxIngredientsPerColumn;
        int xPos = this.left + 5 + column * 16 + 1;
        int yPos = this.top + 5 + row * 16 + 1;
        return this.recipeManager.createRecipeSlotDrawable(RecipeIngredientRole.CATALYST, List.of(Optional.of(typedIngredient)), IntSet.of(0), xPos, yPos, 0);
    }

    public Optional<IRecipeSlotDrawable> draw(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int ingredientCount = this.recipeSlots.size();
        if (ingredientCount > 0) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableDepthTest();
            int slotWidth = this.width - 10;
            int slotHeight = this.height - 10;
            this.backgroundTab.draw(guiGraphics, this.left, this.top, this.width, this.height);
            this.slotBackground.draw(guiGraphics, this.left + 5, this.top + 5, slotWidth, slotHeight);
            RenderSystem.enableDepthTest();
            IRecipeSlotDrawable hovered = null;
            for (IRecipeSlotDrawable recipeSlot : this.recipeSlots) {
                Rect2i rect = recipeSlot.getRect();
                if (MathUtil.contains(rect, (double) mouseX, (double) mouseY)) {
                    hovered = recipeSlot;
                }
                recipeSlot.draw(guiGraphics);
            }
            return Optional.ofNullable(hovered);
        } else {
            return Optional.empty();
        }
    }

    private Stream<IRecipeSlotDrawable> getHovered(double mouseX, double mouseY) {
        return this.recipeSlots.stream().filter(recipeSlot -> {
            Rect2i rect = recipeSlot.getRect();
            return MathUtil.contains(rect, mouseX, mouseY);
        });
    }

    @Override
    public Stream<IClickableIngredientInternal<?>> getIngredientUnderMouse(double mouseX, double mouseY) {
        return this.getHovered(mouseX, mouseY).map(recipeSlot -> recipeSlot.getDisplayedIngredient().map(i -> {
            Rect2i rect = recipeSlot.getRect();
            return new ClickableIngredientInternal(i, new ImmutableRect2i(rect), false, true);
        })).flatMap(Optional::stream);
    }
}