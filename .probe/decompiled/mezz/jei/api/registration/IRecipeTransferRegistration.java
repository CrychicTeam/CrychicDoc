package mezz.jei.api.registration;

import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public interface IRecipeTransferRegistration {

    IJeiHelpers getJeiHelpers();

    IRecipeTransferHandlerHelper getTransferHelper();

    <C extends AbstractContainerMenu, R> void addRecipeTransferHandler(Class<? extends C> var1, @Nullable MenuType<C> var2, RecipeType<R> var3, int var4, int var5, int var6, int var7);

    <C extends AbstractContainerMenu, R> void addRecipeTransferHandler(IRecipeTransferInfo<C, R> var1);

    <C extends AbstractContainerMenu, R> void addRecipeTransferHandler(IRecipeTransferHandler<C, R> var1, RecipeType<R> var2);

    <C extends AbstractContainerMenu, R> void addUniversalRecipeTransferHandler(IRecipeTransferHandler<C, R> var1);
}