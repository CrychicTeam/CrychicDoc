package mezz.jei.library.recipes;

import com.google.common.collect.ImmutableTable;
import java.util.Optional;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferManager;
import mezz.jei.common.Constants;
import mezz.jei.common.util.ErrorUtil;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class RecipeTransferManager implements IRecipeTransferManager {

    private final ImmutableTable<Class<? extends AbstractContainerMenu>, RecipeType<?>, IRecipeTransferHandler<?, ?>> recipeTransferHandlers;

    public RecipeTransferManager(ImmutableTable<Class<? extends AbstractContainerMenu>, RecipeType<?>, IRecipeTransferHandler<?, ?>> recipeTransferHandlers) {
        this.recipeTransferHandlers = recipeTransferHandlers;
    }

    @Override
    public <C extends AbstractContainerMenu, R> Optional<IRecipeTransferHandler<C, R>> getRecipeTransferHandler(C container, IRecipeCategory<R> recipeCategory) {
        ErrorUtil.checkNotNull(container, "container");
        ErrorUtil.checkNotNull(recipeCategory, "recipeCategory");
        MenuType<C> menuType = this.getMenuType(container);
        RecipeType<R> recipeType = recipeCategory.getRecipeType();
        Class<? extends C> containerClass = container.getClass();
        Optional<IRecipeTransferHandler<C, R>> handler = this.getHandler(containerClass, menuType, recipeType);
        return handler.isPresent() ? handler : this.getHandler(containerClass, menuType, Constants.UNIVERSAL_RECIPE_TRANSFER_TYPE);
    }

    @Nullable
    private <C extends AbstractContainerMenu> MenuType<C> getMenuType(C container) {
        try {
            return (MenuType<C>) container.getType();
        } catch (UnsupportedOperationException var3) {
            return null;
        }
    }

    private <C extends AbstractContainerMenu, R> Optional<IRecipeTransferHandler<C, R>> getHandler(Class<? extends C> containerClass, @Nullable MenuType<C> menuType, RecipeType<?> recipeType) {
        return Optional.ofNullable((IRecipeTransferHandler) this.recipeTransferHandlers.get(containerClass, recipeType)).filter(handler -> {
            Optional<? extends MenuType<?>> handlerMenuType = handler.getMenuType();
            return handlerMenuType.isEmpty() || ((MenuType) handlerMenuType.get()).equals(menuType);
        }).flatMap(handler -> Optional.of(handler));
    }
}