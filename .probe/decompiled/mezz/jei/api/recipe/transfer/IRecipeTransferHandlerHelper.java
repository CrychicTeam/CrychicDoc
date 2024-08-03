package mezz.jei.api.recipe.transfer;

import java.util.Collection;
import java.util.List;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public interface IRecipeTransferHandlerHelper {

    IRecipeTransferError createInternalError();

    IRecipeTransferError createUserErrorWithTooltip(Component var1);

    IRecipeTransferError createUserErrorForMissingSlots(Component var1, Collection<IRecipeSlotView> var2);

    <C extends AbstractContainerMenu, R> IRecipeTransferInfo<C, R> createBasicRecipeTransferInfo(Class<? extends C> var1, @Nullable MenuType<C> var2, RecipeType<R> var3, int var4, int var5, int var6, int var7);

    <C extends AbstractContainerMenu, R> IRecipeTransferHandler<C, R> createUnregisteredRecipeTransferHandler(IRecipeTransferInfo<C, R> var1);

    IRecipeSlotsView createRecipeSlotsView(List<IRecipeSlotView> var1);

    boolean recipeTransferHasServerSupport();
}