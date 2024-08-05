package net.minecraft.client.gui.screens.recipebook;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public class OverlayRecipeComponent implements Renderable, GuiEventListener {

    static final ResourceLocation RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");

    private static final int MAX_ROW = 4;

    private static final int MAX_ROW_LARGE = 5;

    private static final float ITEM_RENDER_SCALE = 0.375F;

    public static final int BUTTON_SIZE = 25;

    private final List<OverlayRecipeComponent.OverlayRecipeButton> recipeButtons = Lists.newArrayList();

    private boolean isVisible;

    private int x;

    private int y;

    private Minecraft minecraft;

    private RecipeCollection collection;

    @Nullable
    private Recipe<?> lastRecipeClicked;

    float time;

    boolean isFurnaceMenu;

    public void init(Minecraft minecraft0, RecipeCollection recipeCollection1, int int2, int int3, int int4, int int5, float float6) {
        this.minecraft = minecraft0;
        this.collection = recipeCollection1;
        if (minecraft0.player.f_36096_ instanceof AbstractFurnaceMenu) {
            this.isFurnaceMenu = true;
        }
        boolean $$7 = minecraft0.player.getRecipeBook().m_12689_((RecipeBookMenu) minecraft0.player.f_36096_);
        List<Recipe<?>> $$8 = recipeCollection1.getDisplayRecipes(true);
        List<Recipe<?>> $$9 = $$7 ? Collections.emptyList() : recipeCollection1.getDisplayRecipes(false);
        int $$10 = $$8.size();
        int $$11 = $$10 + $$9.size();
        int $$12 = $$11 <= 16 ? 4 : 5;
        int $$13 = (int) Math.ceil((double) ((float) $$11 / (float) $$12));
        this.x = int2;
        this.y = int3;
        float $$14 = (float) (this.x + Math.min($$11, $$12) * 25);
        float $$15 = (float) (int4 + 50);
        if ($$14 > $$15) {
            this.x = (int) ((float) this.x - float6 * (float) ((int) (($$14 - $$15) / float6)));
        }
        float $$16 = (float) (this.y + $$13 * 25);
        float $$17 = (float) (int5 + 50);
        if ($$16 > $$17) {
            this.y = (int) ((float) this.y - float6 * (float) Mth.ceil(($$16 - $$17) / float6));
        }
        float $$18 = (float) this.y;
        float $$19 = (float) (int5 - 100);
        if ($$18 < $$19) {
            this.y = (int) ((float) this.y - float6 * (float) Mth.ceil(($$18 - $$19) / float6));
        }
        this.isVisible = true;
        this.recipeButtons.clear();
        for (int $$20 = 0; $$20 < $$11; $$20++) {
            boolean $$21 = $$20 < $$10;
            Recipe<?> $$22 = $$21 ? (Recipe) $$8.get($$20) : (Recipe) $$9.get($$20 - $$10);
            int $$23 = this.x + 4 + 25 * ($$20 % $$12);
            int $$24 = this.y + 5 + 25 * ($$20 / $$12);
            if (this.isFurnaceMenu) {
                this.recipeButtons.add(new OverlayRecipeComponent.OverlaySmeltingRecipeButton($$23, $$24, $$22, $$21));
            } else {
                this.recipeButtons.add(new OverlayRecipeComponent.OverlayRecipeButton($$23, $$24, $$22, $$21));
            }
        }
        this.lastRecipeClicked = null;
    }

    public RecipeCollection getRecipeCollection() {
        return this.collection;
    }

    @Nullable
    public Recipe<?> getLastRecipeClicked() {
        return this.lastRecipeClicked;
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (int2 != 0) {
            return false;
        } else {
            for (OverlayRecipeComponent.OverlayRecipeButton $$3 : this.recipeButtons) {
                if ($$3.m_6375_(double0, double1, int2)) {
                    this.lastRecipeClicked = $$3.recipe;
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean isMouseOver(double double0, double double1) {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        if (this.isVisible) {
            this.time += float3;
            RenderSystem.enableBlend();
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().translate(0.0F, 0.0F, 1000.0F);
            int $$4 = this.recipeButtons.size() <= 16 ? 4 : 5;
            int $$5 = Math.min(this.recipeButtons.size(), $$4);
            int $$6 = Mth.ceil((float) this.recipeButtons.size() / (float) $$4);
            int $$7 = 4;
            guiGraphics0.blitNineSliced(RECIPE_BOOK_LOCATION, this.x, this.y, $$5 * 25 + 8, $$6 * 25 + 8, 4, 32, 32, 82, 208);
            RenderSystem.disableBlend();
            for (OverlayRecipeComponent.OverlayRecipeButton $$8 : this.recipeButtons) {
                $$8.m_88315_(guiGraphics0, int1, int2, float3);
            }
            guiGraphics0.pose().popPose();
        }
    }

    public void setVisible(boolean boolean0) {
        this.isVisible = boolean0;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    @Override
    public void setFocused(boolean boolean0) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    class OverlayRecipeButton extends AbstractWidget implements PlaceRecipe<Ingredient> {

        final Recipe<?> recipe;

        private final boolean isCraftable;

        protected final List<OverlayRecipeComponent.OverlayRecipeButton.Pos> ingredientPos = Lists.newArrayList();

        public OverlayRecipeButton(int int0, int int1, Recipe<?> recipe2, boolean boolean3) {
            super(int0, int1, 200, 20, CommonComponents.EMPTY);
            this.f_93618_ = 24;
            this.f_93619_ = 24;
            this.recipe = recipe2;
            this.isCraftable = boolean3;
            this.calculateIngredientsPositions(recipe2);
        }

        protected void calculateIngredientsPositions(Recipe<?> recipe0) {
            this.m_135408_(3, 3, -1, recipe0, recipe0.getIngredients().iterator(), 0);
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
            this.m_168802_(narrationElementOutput0);
        }

        @Override
        public void addItemToSlot(Iterator<Ingredient> iteratorIngredient0, int int1, int int2, int int3, int int4) {
            ItemStack[] $$5 = ((Ingredient) iteratorIngredient0.next()).getItems();
            if ($$5.length != 0) {
                this.ingredientPos.add(new OverlayRecipeComponent.OverlayRecipeButton.Pos(3 + int4 * 7, 3 + int3 * 7, $$5));
            }
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
            int $$4 = 152;
            if (!this.isCraftable) {
                $$4 += 26;
            }
            int $$5 = OverlayRecipeComponent.this.isFurnaceMenu ? 130 : 78;
            if (this.m_198029_()) {
                $$5 += 26;
            }
            guiGraphics0.blit(OverlayRecipeComponent.RECIPE_BOOK_LOCATION, this.m_252754_(), this.m_252907_(), $$4, $$5, this.f_93618_, this.f_93619_);
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().translate((double) (this.m_252754_() + 2), (double) (this.m_252907_() + 2), 150.0);
            for (OverlayRecipeComponent.OverlayRecipeButton.Pos $$6 : this.ingredientPos) {
                guiGraphics0.pose().pushPose();
                guiGraphics0.pose().translate((double) $$6.x, (double) $$6.y, 0.0);
                guiGraphics0.pose().scale(0.375F, 0.375F, 1.0F);
                guiGraphics0.pose().translate(-8.0, -8.0, 0.0);
                if ($$6.ingredients.length > 0) {
                    guiGraphics0.renderItem($$6.ingredients[Mth.floor(OverlayRecipeComponent.this.time / 30.0F) % $$6.ingredients.length], 0, 0);
                }
                guiGraphics0.pose().popPose();
            }
            guiGraphics0.pose().popPose();
        }

        protected class Pos {

            public final ItemStack[] ingredients;

            public final int x;

            public final int y;

            public Pos(int int0, int int1, ItemStack[] itemStack2) {
                this.x = int0;
                this.y = int1;
                this.ingredients = itemStack2;
            }
        }
    }

    class OverlaySmeltingRecipeButton extends OverlayRecipeComponent.OverlayRecipeButton {

        public OverlaySmeltingRecipeButton(int int0, int int1, Recipe<?> recipe2, boolean boolean3) {
            super(int0, int1, recipe2, boolean3);
        }

        @Override
        protected void calculateIngredientsPositions(Recipe<?> recipe0) {
            ItemStack[] $$1 = recipe0.getIngredients().get(0).getItems();
            this.f_100226_.add(new OverlayRecipeComponent.OverlayRecipeButton.Pos(10, 10, $$1));
        }
    }
}