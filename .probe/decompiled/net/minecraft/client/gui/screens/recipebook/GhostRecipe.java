package net.minecraft.client.gui.screens.recipebook;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public class GhostRecipe {

    @Nullable
    private Recipe<?> recipe;

    private final List<GhostRecipe.GhostIngredient> ingredients = Lists.newArrayList();

    float time;

    public void clear() {
        this.recipe = null;
        this.ingredients.clear();
        this.time = 0.0F;
    }

    public void addIngredient(Ingredient ingredient0, int int1, int int2) {
        this.ingredients.add(new GhostRecipe.GhostIngredient(ingredient0, int1, int2));
    }

    public GhostRecipe.GhostIngredient get(int int0) {
        return (GhostRecipe.GhostIngredient) this.ingredients.get(int0);
    }

    public int size() {
        return this.ingredients.size();
    }

    @Nullable
    public Recipe<?> getRecipe() {
        return this.recipe;
    }

    public void setRecipe(Recipe<?> recipe0) {
        this.recipe = recipe0;
    }

    public void render(GuiGraphics guiGraphics0, Minecraft minecraft1, int int2, int int3, boolean boolean4, float float5) {
        if (!Screen.hasControlDown()) {
            this.time += float5;
        }
        for (int $$6 = 0; $$6 < this.ingredients.size(); $$6++) {
            GhostRecipe.GhostIngredient $$7 = (GhostRecipe.GhostIngredient) this.ingredients.get($$6);
            int $$8 = $$7.getX() + int2;
            int $$9 = $$7.getY() + int3;
            if ($$6 == 0 && boolean4) {
                guiGraphics0.fill($$8 - 4, $$9 - 4, $$8 + 20, $$9 + 20, 822018048);
            } else {
                guiGraphics0.fill($$8, $$9, $$8 + 16, $$9 + 16, 822018048);
            }
            ItemStack $$10 = $$7.getItem();
            guiGraphics0.renderFakeItem($$10, $$8, $$9);
            guiGraphics0.fill(RenderType.guiGhostRecipeOverlay(), $$8, $$9, $$8 + 16, $$9 + 16, 822083583);
            if ($$6 == 0) {
                guiGraphics0.renderItemDecorations(minecraft1.font, $$10, $$8, $$9);
            }
        }
    }

    public class GhostIngredient {

        private final Ingredient ingredient;

        private final int x;

        private final int y;

        public GhostIngredient(Ingredient ingredient0, int int1, int int2) {
            this.ingredient = ingredient0;
            this.x = int1;
            this.y = int2;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public ItemStack getItem() {
            ItemStack[] $$0 = this.ingredient.getItems();
            return $$0.length == 0 ? ItemStack.EMPTY : $$0[Mth.floor(GhostRecipe.this.time / 30.0F) % $$0.length];
        }
    }
}