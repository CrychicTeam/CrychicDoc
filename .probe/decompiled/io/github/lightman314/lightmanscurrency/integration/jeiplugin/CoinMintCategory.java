package io.github.lightman314.lightmanscurrency.integration.jeiplugin;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.MintScreen;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.crafting.CoinMintRecipe;
import javax.annotation.Nonnull;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class CoinMintCategory implements IRecipeCategory<CoinMintRecipe> {

    private final IDrawableStatic background;

    private final IDrawable icon;

    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public CoinMintCategory(final IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(MintScreen.GUI_TEXTURE, 47, 8, 98, 42);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.COIN_MINT.get()));
        this.cachedArrows = CacheBuilder.newBuilder().maximumSize(25L).build(new CacheLoader<Integer, IDrawableAnimated>() {

            @Nonnull
            public IDrawableAnimated load(@Nonnull Integer mintTime) {
                return guiHelper.drawableBuilder(MintScreen.GUI_TEXTURE, 176, 0, 22, 16).buildAnimated(mintTime, IDrawableAnimated.StartDirection.LEFT, false);
            }
        });
    }

    protected IDrawableAnimated getArrow(CoinMintRecipe recipe) {
        int mintTime = recipe.getDuration();
        if (mintTime <= 0) {
            mintTime = 100;
        }
        return (IDrawableAnimated) this.cachedArrows.getUnchecked(mintTime);
    }

    @Nonnull
    @Override
    public RecipeType<CoinMintRecipe> getRecipeType() {
        return LCJeiPlugin.COIN_MINT_TYPE;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    public void draw(@Nonnull CoinMintRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        IDrawableAnimated arrow = this.getArrow(recipe);
        arrow.draw(guiGraphics, 33, 13);
        if (!recipe.isValid()) {
            EasyGuiGraphics gui = EasyGuiGraphics.create(guiGraphics, (int) mouseX, (int) mouseY, 0.0F);
            TextRenderUtil.drawCenteredText(gui, LCText.TOOLTIP_COIN_MINT_DISABLED_TOP.get().withStyle(ChatFormatting.BOLD), 52, 0, 16711680);
            TextRenderUtil.drawCenteredText(gui, LCText.TOOLTIP_COIN_MINT_DISABLED_BOTTOM.get().withStyle(ChatFormatting.BOLD), 52, 35, 16711680);
        }
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return LCText.GUI_COIN_MINT_TITLE.get();
    }

    public void setRecipe(IRecipeLayoutBuilder builder, CoinMintRecipe recipe, @Nonnull IFocusGroup focus) {
        IRecipeSlotBuilder inputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 9, 13);
        inputSlot.addIngredients(VanillaTypes.ITEM_STACK, Lists.newArrayList(SetStackCount(recipe.getIngredient().getItems(), recipe.ingredientCount)));
        IRecipeSlotBuilder outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 69, 13);
        outputSlot.addIngredient(VanillaTypes.ITEM_STACK, recipe.getOutputItem());
    }

    private static ItemStack[] SetStackCount(ItemStack[] results, int count) {
        for (ItemStack stack : results) {
            stack.setCount(count);
        }
        return results;
    }
}