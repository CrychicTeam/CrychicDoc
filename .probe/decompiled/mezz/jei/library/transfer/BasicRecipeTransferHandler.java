package mezz.jei.library.transfer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.common.network.packets.PacketRecipeTransfer;
import mezz.jei.common.transfer.RecipeTransferOperationsResult;
import mezz.jei.common.transfer.RecipeTransferUtil;
import mezz.jei.common.util.StringUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class BasicRecipeTransferHandler<C extends AbstractContainerMenu, R> implements IRecipeTransferHandler<C, R> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final IConnectionToServer serverConnection;

    private final IStackHelper stackHelper;

    private final IRecipeTransferHandlerHelper handlerHelper;

    private final IRecipeTransferInfo<C, R> transferInfo;

    public BasicRecipeTransferHandler(IConnectionToServer serverConnection, IStackHelper stackHelper, IRecipeTransferHandlerHelper handlerHelper, IRecipeTransferInfo<C, R> transferInfo) {
        this.serverConnection = serverConnection;
        this.stackHelper = stackHelper;
        this.handlerHelper = handlerHelper;
        this.transferInfo = transferInfo;
    }

    @Override
    public Class<? extends C> getContainerClass() {
        return this.transferInfo.getContainerClass();
    }

    @Override
    public Optional<MenuType<C>> getMenuType() {
        return this.transferInfo.getMenuType();
    }

    @Override
    public RecipeType<R> getRecipeType() {
        return this.transferInfo.getRecipeType();
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(C container, R recipe, IRecipeSlotsView recipeSlotsView, Player player, boolean maxTransfer, boolean doTransfer) {
        if (!this.serverConnection.isJeiOnServer()) {
            Component tooltipMessage = Component.translatable("jei.tooltip.error.recipe.transfer.no.server");
            return this.handlerHelper.createUserErrorWithTooltip(tooltipMessage);
        } else if (!this.transferInfo.canHandle(container, recipe)) {
            IRecipeTransferError handlingError = this.transferInfo.getHandlingError(container, recipe);
            return handlingError != null ? handlingError : this.handlerHelper.createInternalError();
        } else {
            List<Slot> craftingSlots = Collections.unmodifiableList(this.transferInfo.getRecipeSlots(container, recipe));
            List<Slot> inventorySlots = Collections.unmodifiableList(this.transferInfo.getInventorySlots(container, recipe));
            if (!validateTransferInfo(this.transferInfo, container, craftingSlots, inventorySlots)) {
                return this.handlerHelper.createInternalError();
            } else {
                List<IRecipeSlotView> inputItemSlotViews = recipeSlotsView.getSlotViews(RecipeIngredientRole.INPUT);
                if (!validateRecipeView(this.transferInfo, container, craftingSlots, inputItemSlotViews)) {
                    return this.handlerHelper.createInternalError();
                } else {
                    BasicRecipeTransferHandler.InventoryState inventoryState = getInventoryState(craftingSlots, inventorySlots, player, container, this.transferInfo);
                    if (inventoryState == null) {
                        return this.handlerHelper.createInternalError();
                    } else {
                        int inputCount = inputItemSlotViews.size();
                        if (!inventoryState.hasRoom(inputCount)) {
                            Component message = Component.translatable("jei.tooltip.error.recipe.transfer.inventory.full");
                            return this.handlerHelper.createUserErrorWithTooltip(message);
                        } else {
                            RecipeTransferOperationsResult transferOperations = RecipeTransferUtil.getRecipeTransferOperations(this.stackHelper, inventoryState.availableItemStacks, inputItemSlotViews, craftingSlots);
                            if (transferOperations.missingItems.size() > 0) {
                                Component message = Component.translatable("jei.tooltip.error.recipe.transfer.missing");
                                return this.handlerHelper.createUserErrorForMissingSlots(message, transferOperations.missingItems);
                            } else if (!RecipeTransferUtil.validateSlots(player, transferOperations.results, craftingSlots, inventorySlots)) {
                                return this.handlerHelper.createInternalError();
                            } else {
                                if (doTransfer) {
                                    boolean requireCompleteSets = this.transferInfo.requireCompleteSets(container, recipe);
                                    PacketRecipeTransfer packet = new PacketRecipeTransfer(transferOperations.results, craftingSlots, inventorySlots, maxTransfer, requireCompleteSets);
                                    this.serverConnection.sendPacketToServer(packet);
                                }
                                return null;
                            }
                        }
                    }
                }
            }
        }
    }

    public static <C extends AbstractContainerMenu, R> boolean validateTransferInfo(IRecipeTransferInfo<C, R> transferInfo, C container, List<Slot> craftingSlots, List<Slot> inventorySlots) {
        Collection<Integer> craftingSlotIndexes = slotIndexes(craftingSlots);
        Collection<Integer> inventorySlotIndexes = slotIndexes(inventorySlots);
        Collection<Integer> containerSlotIndexes = slotIndexes(container.slots);
        if (!containerSlotIndexes.containsAll(craftingSlotIndexes)) {
            LOGGER.error("Recipe Transfer helper {} does not work for container {}. The Recipes Transfer Helper references crafting slot indexes [{}] that are not found in the inventory container slots [{}]", transferInfo.getClass(), container.getClass(), StringUtil.intsToString(craftingSlotIndexes), StringUtil.intsToString(containerSlotIndexes));
            return false;
        } else if (!containerSlotIndexes.containsAll(inventorySlotIndexes)) {
            LOGGER.error("Recipe Transfer helper {} does not work for container {}. The Recipes Transfer Helper references inventory slot indexes [{}] that are not found in the inventory container slots [{}]", transferInfo.getClass(), container.getClass(), StringUtil.intsToString(inventorySlotIndexes), StringUtil.intsToString(containerSlotIndexes));
            return false;
        } else {
            return true;
        }
    }

    public static <C extends AbstractContainerMenu, R> boolean validateRecipeView(IRecipeTransferInfo<C, R> transferInfo, C container, List<Slot> craftingSlots, List<IRecipeSlotView> inputSlots) {
        if (inputSlots.size() > craftingSlots.size()) {
            LOGGER.error("Recipe View {} does not work for container {}. The Recipe View has more input slots ({}) than the number of inventory crafting slots ({})", transferInfo.getClass(), container.getClass(), inputSlots.size(), craftingSlots.size());
            return false;
        } else {
            return true;
        }
    }

    public static Set<Integer> slotIndexes(Collection<Slot> slots) {
        return (Set<Integer>) slots.stream().map(s -> s.index).collect(Collectors.toSet());
    }

    @Nullable
    public static <C extends AbstractContainerMenu, R> BasicRecipeTransferHandler.InventoryState getInventoryState(Collection<Slot> craftingSlots, Collection<Slot> inventorySlots, Player player, C container, IRecipeTransferInfo<C, R> transferInfo) {
        Map<Slot, ItemStack> availableItemStacks = new HashMap();
        int filledCraftSlotCount = 0;
        int emptySlotCount = 0;
        for (Slot slot : craftingSlots) {
            ItemStack stack = slot.getItem();
            if (!stack.isEmpty()) {
                if (!slot.mayPickup(player)) {
                    LOGGER.error("Recipe Transfer helper {} does not work for container {}. The Player is not able to move items out of Crafting Slot number {}", transferInfo.getClass(), container.getClass(), slot.index);
                    return null;
                }
                filledCraftSlotCount++;
                availableItemStacks.put(slot, stack.copy());
            }
        }
        for (Slot slotx : inventorySlots) {
            ItemStack stack = slotx.getItem();
            if (!stack.isEmpty()) {
                if (!slotx.mayPickup(player)) {
                    LOGGER.error("Recipe Transfer helper {} does not work for container {}. The Player is not able to move items out of Inventory Slot number {}", transferInfo.getClass(), container.getClass(), slotx.index);
                    return null;
                }
                availableItemStacks.put(slotx, stack.copy());
            } else {
                emptySlotCount++;
            }
        }
        return new BasicRecipeTransferHandler.InventoryState(availableItemStacks, filledCraftSlotCount, emptySlotCount);
    }

    public static record InventoryState(Map<Slot, ItemStack> availableItemStacks, int filledCraftSlotCount, int emptySlotCount) {

        public boolean hasRoom(int inputCount) {
            return this.filledCraftSlotCount - inputCount <= this.emptySlotCount;
        }
    }
}