package mezz.jei.library.transfer;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.CraftingRecipe;
import org.jetbrains.annotations.Nullable;

public class PlayerRecipeTransferHandler implements IRecipeTransferHandler<InventoryMenu, CraftingRecipe> {

    private static final IntSet PLAYER_INV_INDEXES = IntArraySet.of(new int[] { 0, 1, 3, 4 });

    private final IRecipeTransferHandlerHelper handlerHelper;

    private final IRecipeTransferHandler<InventoryMenu, CraftingRecipe> handler;

    public PlayerRecipeTransferHandler(IRecipeTransferHandlerHelper handlerHelper) {
        this.handlerHelper = handlerHelper;
        IRecipeTransferInfo<InventoryMenu, CraftingRecipe> basicRecipeTransferInfo = handlerHelper.createBasicRecipeTransferInfo(InventoryMenu.class, null, RecipeTypes.CRAFTING, 1, 4, 9, 36);
        this.handler = handlerHelper.createUnregisteredRecipeTransferHandler(basicRecipeTransferInfo);
    }

    @Override
    public Class<? extends InventoryMenu> getContainerClass() {
        return this.handler.getContainerClass();
    }

    @Override
    public Optional<MenuType<InventoryMenu>> getMenuType() {
        return this.handler.getMenuType();
    }

    @Override
    public RecipeType<CraftingRecipe> getRecipeType() {
        return RecipeTypes.CRAFTING;
    }

    @Nullable
    public IRecipeTransferError transferRecipe(InventoryMenu container, CraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, Player player, boolean maxTransfer, boolean doTransfer) {
        if (!this.handlerHelper.recipeTransferHasServerSupport()) {
            Component tooltipMessage = Component.translatable("jei.tooltip.error.recipe.transfer.no.server");
            return this.handlerHelper.createUserErrorWithTooltip(tooltipMessage);
        } else {
            List<IRecipeSlotView> slotViews = recipeSlotsView.getSlotViews(RecipeIngredientRole.INPUT);
            if (!validateIngredientsOutsidePlayerGridAreEmpty(slotViews)) {
                Component tooltipMessage = Component.translatable("jei.tooltip.error.recipe.transfer.too.large.player.inventory");
                return this.handlerHelper.createUserErrorWithTooltip(tooltipMessage);
            } else {
                List<IRecipeSlotView> filteredSlotViews = filterSlots(slotViews);
                IRecipeSlotsView filteredRecipeSlots = this.handlerHelper.createRecipeSlotsView(filteredSlotViews);
                return this.handler.transferRecipe(container, recipe, filteredRecipeSlots, player, maxTransfer, doTransfer);
            }
        }
    }

    private static boolean validateIngredientsOutsidePlayerGridAreEmpty(List<IRecipeSlotView> slotViews) {
        int bound = slotViews.size();
        for (int i = 0; i < bound; i++) {
            if (!PLAYER_INV_INDEXES.contains(i)) {
                IRecipeSlotView slotView = (IRecipeSlotView) slotViews.get(i);
                if (!slotView.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<IRecipeSlotView> filterSlots(List<IRecipeSlotView> slotViews) {
        return PLAYER_INV_INDEXES.intStream().mapToObj(slotViews::get).toList();
    }
}