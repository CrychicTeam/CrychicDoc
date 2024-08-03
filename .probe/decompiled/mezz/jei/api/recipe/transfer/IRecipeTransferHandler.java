package mezz.jei.api.recipe.transfer;

import java.util.Optional;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public interface IRecipeTransferHandler<C extends AbstractContainerMenu, R> {

    Class<? extends C> getContainerClass();

    Optional<MenuType<C>> getMenuType();

    RecipeType<R> getRecipeType();

    @Nullable
    IRecipeTransferError transferRecipe(C var1, R var2, IRecipeSlotsView var3, Player var4, boolean var5, boolean var6);
}