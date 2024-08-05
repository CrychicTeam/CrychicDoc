package net.minecraft.client.gui.screens.recipebook;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeButton extends AbstractWidget {

    private static final ResourceLocation RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");

    private static final float ANIMATION_TIME = 15.0F;

    private static final int BACKGROUND_SIZE = 25;

    public static final int TICKS_TO_SWAP = 30;

    private static final Component MORE_RECIPES_TOOLTIP = Component.translatable("gui.recipebook.moreRecipes");

    private RecipeBookMenu<?> menu;

    private RecipeBook book;

    private RecipeCollection collection;

    private float time;

    private float animationTime;

    private int currentIndex;

    public RecipeButton() {
        super(0, 0, 25, 25, CommonComponents.EMPTY);
    }

    public void init(RecipeCollection recipeCollection0, RecipeBookPage recipeBookPage1) {
        this.collection = recipeCollection0;
        this.menu = (RecipeBookMenu<?>) recipeBookPage1.getMinecraft().player.f_36096_;
        this.book = recipeBookPage1.getRecipeBook();
        List<Recipe<?>> $$2 = recipeCollection0.getRecipes(this.book.isFiltering(this.menu));
        for (Recipe<?> $$3 : $$2) {
            if (this.book.willHighlight($$3)) {
                recipeBookPage1.recipesShown($$2);
                this.animationTime = 15.0F;
                break;
            }
        }
    }

    public RecipeCollection getCollection() {
        return this.collection;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        if (!Screen.hasControlDown()) {
            this.time += float3;
        }
        Minecraft $$4 = Minecraft.getInstance();
        int $$5 = 29;
        if (!this.collection.hasCraftable()) {
            $$5 += 25;
        }
        int $$6 = 206;
        if (this.collection.getRecipes(this.book.isFiltering(this.menu)).size() > 1) {
            $$6 += 25;
        }
        boolean $$7 = this.animationTime > 0.0F;
        if ($$7) {
            float $$8 = 1.0F + 0.1F * (float) Math.sin((double) (this.animationTime / 15.0F * (float) Math.PI));
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().translate((float) (this.m_252754_() + 8), (float) (this.m_252907_() + 12), 0.0F);
            guiGraphics0.pose().scale($$8, $$8, 1.0F);
            guiGraphics0.pose().translate((float) (-(this.m_252754_() + 8)), (float) (-(this.m_252907_() + 12)), 0.0F);
            this.animationTime -= float3;
        }
        guiGraphics0.blit(RECIPE_BOOK_LOCATION, this.m_252754_(), this.m_252907_(), $$5, $$6, this.f_93618_, this.f_93619_);
        List<Recipe<?>> $$9 = this.getOrderedRecipes();
        this.currentIndex = Mth.floor(this.time / 30.0F) % $$9.size();
        ItemStack $$10 = ((Recipe) $$9.get(this.currentIndex)).getResultItem(this.collection.registryAccess());
        int $$11 = 4;
        if (this.collection.hasSingleResultItem() && this.getOrderedRecipes().size() > 1) {
            guiGraphics0.renderItem($$10, this.m_252754_() + $$11 + 1, this.m_252907_() + $$11 + 1, 0, 10);
            $$11--;
        }
        guiGraphics0.renderFakeItem($$10, this.m_252754_() + $$11, this.m_252907_() + $$11);
        if ($$7) {
            guiGraphics0.pose().popPose();
        }
    }

    private List<Recipe<?>> getOrderedRecipes() {
        List<Recipe<?>> $$0 = this.collection.getDisplayRecipes(true);
        if (!this.book.isFiltering(this.menu)) {
            $$0.addAll(this.collection.getDisplayRecipes(false));
        }
        return $$0;
    }

    public boolean isOnlyOption() {
        return this.getOrderedRecipes().size() == 1;
    }

    public Recipe<?> getRecipe() {
        List<Recipe<?>> $$0 = this.getOrderedRecipes();
        return (Recipe<?>) $$0.get(this.currentIndex);
    }

    public List<Component> getTooltipText() {
        ItemStack $$0 = ((Recipe) this.getOrderedRecipes().get(this.currentIndex)).getResultItem(this.collection.registryAccess());
        List<Component> $$1 = Lists.newArrayList(Screen.getTooltipFromItem(Minecraft.getInstance(), $$0));
        if (this.collection.getRecipes(this.book.isFiltering(this.menu)).size() > 1) {
            $$1.add(MORE_RECIPES_TOOLTIP);
        }
        return $$1;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
        ItemStack $$1 = ((Recipe) this.getOrderedRecipes().get(this.currentIndex)).getResultItem(this.collection.registryAccess());
        narrationElementOutput0.add(NarratedElementType.TITLE, Component.translatable("narration.recipe", $$1.getHoverName()));
        if (this.collection.getRecipes(this.book.isFiltering(this.menu)).size() > 1) {
            narrationElementOutput0.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.hovered"), Component.translatable("narration.recipe.usage.more"));
        } else {
            narrationElementOutput0.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.hovered"));
        }
    }

    @Override
    public int getWidth() {
        return 25;
    }

    @Override
    protected boolean isValidClickButton(int int0) {
        return int0 == 0 || int0 == 1;
    }
}