package dev.xkmc.l2library.serial.recipe;

import javax.annotation.ParametersAreNonnullByDefault;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BaseRecipeCategory<T, C extends BaseRecipeCategory<T, C>> implements IRecipeCategory<T> {

    private final RecipeType<T> type;

    protected IDrawable background;

    protected IDrawable icon;

    public static <T extends R, R> Class<T> cast(Class<R> cls) {
        return cls;
    }

    public BaseRecipeCategory(ResourceLocation name, Class<T> cls) {
        this.type = new RecipeType<>(name, cls);
    }

    public final C getThis() {
        return (C) this;
    }

    @Override
    public final RecipeType<T> getRecipeType() {
        return this.type;
    }

    @Override
    public final IDrawable getBackground() {
        return this.background;
    }

    @Override
    public final IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public abstract void setRecipe(IRecipeLayoutBuilder var1, T var2, IFocusGroup var3);
}