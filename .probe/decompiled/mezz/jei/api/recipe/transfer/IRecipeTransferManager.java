package mezz.jei.api.recipe.transfer;

import java.util.Optional;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface IRecipeTransferManager {

    <C extends AbstractContainerMenu, R> Optional<IRecipeTransferHandler<C, R>> getRecipeTransferHandler(C var1, IRecipeCategory<R> var2);
}