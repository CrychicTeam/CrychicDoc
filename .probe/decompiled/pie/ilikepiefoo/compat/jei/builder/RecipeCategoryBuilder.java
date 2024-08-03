package pie.ilikepiefoo.compat.jei.builder;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecipeCategoryBuilder<T> {

    @NotNull
    private final RecipeType<T> recipeType;

    @NotNull
    private final IJeiHelpers jeiHelpers;

    @NotNull
    private Component title;

    @NotNull
    private IDrawable background;

    @NotNull
    private IDrawable icon;

    private int width;

    private int height;

    private RecipeCategoryBuilder.SetRecipeHandler<T> setRecipeHandler;

    private RecipeCategoryBuilder.DrawHandler<T> drawHandler;

    private RecipeCategoryBuilder.TooltipHandler<T> tooltipHandler;

    private RecipeCategoryBuilder.InputHandler<T> inputHandler;

    private RecipeCategoryBuilder.IsRecipeHandledByCategory<T> isRecipeHandledByCategory;

    private RecipeCategoryBuilder.GetRegisterName<T> getRegisterName;

    public RecipeCategoryBuilder(@NotNull RecipeType<T> recipeType, @NotNull IJeiHelpers jeiHelpers) {
        this.recipeType = recipeType;
        this.jeiHelpers = jeiHelpers;
        this.title = Component.literal("KubeJS Additions Custom Category");
        this.background = this.jeiHelpers.getGuiHelper().createDrawableItemStack(new ItemStack(Items.CREEPER_HEAD));
        this.icon = this.jeiHelpers.getGuiHelper().createDrawableItemStack(new ItemStack(Items.TNT));
        this.width = this.background.getWidth();
        this.height = this.background.getHeight();
    }

    @NotNull
    public IJeiHelpers getJeiHelpers() {
        return this.jeiHelpers;
    }

    @NotNull
    public RecipeType<T> getRecipeType() {
        return this.recipeType;
    }

    @NotNull
    public Component getCategoryTitle() {
        return this.title;
    }

    public RecipeCategoryBuilder<T> title(@NotNull Component title) {
        this.title = title;
        return this;
    }

    @NotNull
    public IDrawable getCategoryBackground() {
        return this.background;
    }

    public RecipeCategoryBuilder<T> background(@NotNull IDrawable background) {
        this.background = background;
        this.width = background.getWidth();
        this.height = background.getHeight();
        return this;
    }

    @NotNull
    public IDrawable getCategoryIcon() {
        return this.icon;
    }

    public RecipeCategoryBuilder<T> icon(@NotNull IDrawable icon) {
        this.icon = icon;
        return this;
    }

    public int getWidth() {
        return this.width;
    }

    public RecipeCategoryBuilder<T> setWidth(int width) {
        if (width < 0) {
            throw new IllegalArgumentException("width must be greater than or equal to zero");
        } else if (width == 0) {
            throw new IllegalArgumentException("width must be greater than zero");
        } else {
            this.width = width;
            return this;
        }
    }

    public int getHeight() {
        return this.height;
    }

    public RecipeCategoryBuilder<T> setHeight(int height) {
        if (height < 0) {
            throw new IllegalArgumentException("height must be greater than or equal to zero");
        } else if (height == 0) {
            throw new IllegalArgumentException("height must be greater than zero");
        } else {
            this.height = height;
            return this;
        }
    }

    public RecipeCategoryBuilder.SetRecipeHandler<T> getSetRecipeHandler() {
        return this.setRecipeHandler;
    }

    public RecipeCategoryBuilder<T> setSetRecipeHandler(RecipeCategoryBuilder.SetRecipeHandler<T> setRecipeHandler) {
        this.setRecipeHandler = setRecipeHandler;
        return this;
    }

    public RecipeCategoryBuilder.DrawHandler<T> getDrawHandler() {
        return this.drawHandler;
    }

    public RecipeCategoryBuilder<T> setDrawHandler(RecipeCategoryBuilder.DrawHandler<T> drawHandler) {
        this.drawHandler = drawHandler;
        return this;
    }

    public RecipeCategoryBuilder.TooltipHandler<T> getTooltipHandler() {
        return this.tooltipHandler;
    }

    public RecipeCategoryBuilder<T> setTooltipHandler(RecipeCategoryBuilder.TooltipHandler<T> tooltipHandler) {
        this.tooltipHandler = tooltipHandler;
        return this;
    }

    public RecipeCategoryBuilder.InputHandler<T> getInputHandler() {
        return this.inputHandler;
    }

    public RecipeCategoryBuilder<T> setInputHandler(RecipeCategoryBuilder.InputHandler<T> inputHandler) {
        this.inputHandler = inputHandler;
        return this;
    }

    public RecipeCategoryBuilder.IsRecipeHandledByCategory<T> getIsRecipeHandledByCategory() {
        return this.isRecipeHandledByCategory;
    }

    public RecipeCategoryBuilder<T> setIsRecipeHandledByCategory(RecipeCategoryBuilder.IsRecipeHandledByCategory<T> isRecipeHandledByCategory) {
        this.isRecipeHandledByCategory = isRecipeHandledByCategory;
        return this;
    }

    public RecipeCategoryBuilder.GetRegisterName<T> getGetRegisterName() {
        return this.getRegisterName;
    }

    public RecipeCategoryBuilder<T> setGetRegisterName(RecipeCategoryBuilder.GetRegisterName<T> getRegisterName) {
        this.getRegisterName = getRegisterName;
        return this;
    }

    public RecipeCategoryBuilder<T> registryName(RecipeCategoryBuilder.GetRegisterName<T> getRegisterName) {
        return this.setGetRegisterName(getRegisterName);
    }

    public RecipeCategoryBuilder<T> isRecipeHandled(RecipeCategoryBuilder.IsRecipeHandledByCategory<T> isRecipeHandledByCategory) {
        return this.setIsRecipeHandledByCategory(isRecipeHandledByCategory);
    }

    public RecipeCategoryBuilder<T> onInput(RecipeCategoryBuilder.InputHandler<T> inputHandler) {
        return this.setInputHandler(inputHandler);
    }

    public RecipeCategoryBuilder<T> withTooltip(RecipeCategoryBuilder.TooltipHandler<T> tooltipHandler) {
        return this.setTooltipHandler(tooltipHandler);
    }

    public RecipeCategoryBuilder<T> handleLookup(RecipeCategoryBuilder.SetRecipeHandler<T> recipeHandler) {
        return this.setSetRecipeHandler(recipeHandler);
    }

    @FunctionalInterface
    public interface DrawHandler<T> {

        void draw(T var1, IRecipeSlotsView var2, GuiGraphics var3, double var4, double var6);
    }

    @FunctionalInterface
    public interface GetRegisterName<T> {

        @Nullable
        ResourceLocation getRegistryName(T var1);
    }

    @FunctionalInterface
    public interface InputHandler<T> {

        boolean handleInput(T var1, double var2, double var4, InputConstants.Key var6);
    }

    @FunctionalInterface
    public interface IsRecipeHandledByCategory<T> {

        boolean isHandled(T var1);
    }

    @FunctionalInterface
    public interface SetRecipeHandler<T> {

        void setRecipe(IRecipeLayoutBuilder var1, T var2, IFocusGroup var3);
    }

    @FunctionalInterface
    public interface TooltipHandler<T> {

        @NotNull
        List<Component> getTooltipStrings(T var1, IRecipeSlotsView var2, double var3, double var5);
    }
}