package mezz.jei.api.recipe.transfer;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

public interface IRecipeTransferInfo<C extends AbstractContainerMenu, R> {

    Class<? extends C> getContainerClass();

    Optional<MenuType<C>> getMenuType();

    RecipeType<R> getRecipeType();

    boolean canHandle(C var1, R var2);

    @Nullable
    default IRecipeTransferError getHandlingError(C container, R recipe) {
        return null;
    }

    List<Slot> getRecipeSlots(C var1, R var2);

    List<Slot> getInventorySlots(C var1, R var2);

    default boolean requireCompleteSets(C container, R recipe) {
        return true;
    }
}