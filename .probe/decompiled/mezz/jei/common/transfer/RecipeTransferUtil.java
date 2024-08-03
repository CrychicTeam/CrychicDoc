package mezz.jei.common.transfer;

import it.unimi.dsi.fastutil.Hash.Strategy;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferManager;
import mezz.jei.common.util.StringUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class RecipeTransferUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    private RecipeTransferUtil() {
    }

    public static Optional<IRecipeTransferError> getTransferRecipeError(IRecipeTransferManager recipeTransferManager, AbstractContainerMenu container, IRecipeLayoutDrawable<?> recipeLayout, Player player) {
        return transferRecipe(recipeTransferManager, container, recipeLayout, player, false, false);
    }

    public static boolean transferRecipe(IRecipeTransferManager recipeTransferManager, AbstractContainerMenu container, IRecipeLayoutDrawable<?> recipeLayout, Player player, boolean maxTransfer) {
        return (Boolean) transferRecipe(recipeTransferManager, container, recipeLayout, player, maxTransfer, true).map(error -> error.getType().allowsTransfer).orElse(true);
    }

    private static <C extends AbstractContainerMenu, R> Optional<IRecipeTransferError> transferRecipe(IRecipeTransferManager recipeTransferManager, C container, IRecipeLayoutDrawable<R> recipeLayout, Player player, boolean maxTransfer, boolean doTransfer) {
        IRecipeCategory<R> recipeCategory = recipeLayout.getRecipeCategory();
        Optional<IRecipeTransferHandler<C, R>> recipeTransferHandler = recipeTransferManager.getRecipeTransferHandler(container, recipeCategory);
        if (recipeTransferHandler.isEmpty()) {
            if (doTransfer) {
                LOGGER.error("No Recipe Transfer handler for container {}", container.getClass());
            }
            return Optional.of(RecipeTransferErrorInternal.INSTANCE);
        } else {
            IRecipeTransferHandler<C, R> transferHandler = (IRecipeTransferHandler<C, R>) recipeTransferHandler.get();
            IRecipeSlotsView recipeSlotsView = recipeLayout.getRecipeSlotsView();
            try {
                IRecipeTransferError transferError = transferHandler.transferRecipe(container, recipeLayout.getRecipe(), recipeSlotsView, player, maxTransfer, doTransfer);
                return Optional.ofNullable(transferError);
            } catch (RuntimeException var11) {
                LOGGER.error("Recipe transfer handler '{}' for container '{}' and recipe type '{}' threw an error: ", transferHandler.getClass(), transferHandler.getContainerClass(), recipeCategory.getRecipeType().getUid(), var11);
                return Optional.of(RecipeTransferErrorInternal.INSTANCE);
            }
        }
    }

    public static boolean validateSlots(Player player, Collection<TransferOperation> transferOperations, Collection<Slot> craftingSlots, Collection<Slot> inventorySlots) {
        Set<Integer> inventorySlotIndexes = (Set<Integer>) inventorySlots.stream().map(s -> s.index).collect(Collectors.toSet());
        Set<Integer> craftingSlotIndexes = (Set<Integer>) craftingSlots.stream().map(s -> s.index).collect(Collectors.toSet());
        List<Integer> invalidRecipeIndexes = transferOperations.stream().map(TransferOperation::craftingSlot).map(s -> s.index).filter(s -> !craftingSlotIndexes.contains(s)).toList();
        if (!invalidRecipeIndexes.isEmpty()) {
            LOGGER.error("Transfer handler has invalid slots for the destination of the recipe, the slots are not included in the list of crafting slots. " + StringUtil.intsToString(invalidRecipeIndexes));
            return false;
        } else {
            invalidRecipeIndexes = transferOperations.stream().map(TransferOperation::inventorySlot).map(s -> s.index).filter(s -> !inventorySlotIndexes.contains(s) && !craftingSlotIndexes.contains(s)).toList();
            if (!invalidRecipeIndexes.isEmpty()) {
                LOGGER.error("Transfer handler has invalid source slots for the inventory stacks for the recipe, the slots are not included in the list of inventory slots or recipe slots. " + StringUtil.intsToString(invalidRecipeIndexes) + "\n inventory slots: " + StringUtil.intsToString(inventorySlotIndexes) + "\n crafting slots: " + StringUtil.intsToString(craftingSlotIndexes));
                return false;
            } else {
                Set<Integer> overlappingSlots = (Set<Integer>) inventorySlotIndexes.stream().filter(craftingSlotIndexes::contains).collect(Collectors.toSet());
                if (!overlappingSlots.isEmpty()) {
                    LOGGER.error("Transfer handler has invalid slots, inventorySlots and craftingSlots should not share any slot, but both have: " + StringUtil.intsToString(overlappingSlots));
                    return false;
                } else {
                    invalidRecipeIndexes = Stream.concat(craftingSlots.stream(), inventorySlots.stream()).filter(Slot::m_6657_).filter(slot -> !slot.mayPickup(player)).map(slot -> slot.index).toList();
                    if (!invalidRecipeIndexes.isEmpty()) {
                        LOGGER.error("Transfer handler has invalid slots, the player is unable to pickup from them: " + StringUtil.intsToString(invalidRecipeIndexes));
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
    }

    public static RecipeTransferOperationsResult getRecipeTransferOperations(IStackHelper stackhelper, Map<Slot, ItemStack> availableItemStacks, List<IRecipeSlotView> requiredItemStacks, List<Slot> craftingSlots) {
        RecipeTransferOperationsResult transferOperations = new RecipeTransferOperationsResult();
        Map<IRecipeSlotView, Map<ItemStack, ArrayList<RecipeTransferUtil.PhantomSlotState>>> relevantSlots = new IdentityHashMap();
        for (Entry<Slot, ItemStack> slotTuple : availableItemStacks.entrySet()) {
            for (IRecipeSlotView ingredient : requiredItemStacks) {
                if (!ingredient.isEmpty() && ingredient.getItemStacks().anyMatch(it -> stackhelper.isEquivalent(it, (ItemStack) slotTuple.getValue(), UidContext.Ingredient))) {
                    ((ArrayList) ((Map) relevantSlots.computeIfAbsent(ingredient, it -> new Object2ObjectOpenCustomHashMap(new Strategy<ItemStack>() {

                        public int hashCode(ItemStack o) {
                            return o.getItem().hashCode();
                        }

                        public boolean equals(ItemStack a, ItemStack b) {
                            return stackhelper.isEquivalent(a, b, UidContext.Ingredient);
                        }
                    }))).computeIfAbsent((ItemStack) slotTuple.getValue(), it -> new ArrayList())).add(new RecipeTransferUtil.PhantomSlotState((Slot) slotTuple.getKey(), (ItemStack) slotTuple.getValue()));
                }
            }
        }
        Map<IRecipeSlotView, ArrayList<RecipeTransferUtil.PhantomSlotStateList>> bestMatches = new Object2ObjectArrayMap();
        for (Entry<IRecipeSlotView, Map<ItemStack, ArrayList<RecipeTransferUtil.PhantomSlotState>>> entry : relevantSlots.entrySet()) {
            ArrayList<RecipeTransferUtil.PhantomSlotStateList> countedAndSorted = new ArrayList();
            for (Entry<ItemStack, ArrayList<RecipeTransferUtil.PhantomSlotState>> foundSlots : ((Map) entry.getValue()).entrySet()) {
                ((ArrayList) foundSlots.getValue()).sort((o1, o2) -> {
                    int compare = Integer.compare(o1.itemStack.getCount(), o2.itemStack.getCount());
                    return compare == 0 ? Integer.compare(o1.slot.index, o2.slot.index) : compare;
                });
                countedAndSorted.add(new RecipeTransferUtil.PhantomSlotStateList((List<RecipeTransferUtil.PhantomSlotState>) foundSlots.getValue()));
            }
            countedAndSorted.sort((o1, o2) -> {
                int compare = Long.compare(o2.totalItemCount, o1.totalItemCount);
                return compare == 0 ? Integer.compare(o1.stateList.stream().mapToInt(it -> it.slot.index).min().orElse(0), o2.stateList.stream().mapToInt(it -> it.slot.index).min().orElse(0)) : compare;
            });
            bestMatches.put((IRecipeSlotView) entry.getKey(), countedAndSorted);
        }
        for (IRecipeSlotView ingredientx : requiredItemStacks) {
            if (!ingredientx.isEmpty()) {
                bestMatches.computeIfAbsent(ingredientx, it -> new ArrayList());
            }
        }
        for (int i = 0; i < requiredItemStacks.size(); i++) {
            IRecipeSlotView requiredItemStack = (IRecipeSlotView) requiredItemStacks.get(i);
            if (!requiredItemStack.isEmpty()) {
                Slot craftingSlot = (Slot) craftingSlots.get(i);
                RecipeTransferUtil.PhantomSlotState matching = (RecipeTransferUtil.PhantomSlotState) ((ArrayList) bestMatches.get(requiredItemStack)).stream().flatMap(RecipeTransferUtil.PhantomSlotStateList::stream).findFirst().orElse(null);
                if (matching == null) {
                    transferOperations.missingItems.add(requiredItemStack);
                } else {
                    Slot matchingSlot = matching.slot;
                    ItemStack matchingStack = matching.itemStack;
                    matchingStack.shrink(1);
                    transferOperations.results.add(new TransferOperation(matchingSlot, craftingSlot));
                }
            }
        }
        return transferOperations;
    }

    private static record PhantomSlotState(Slot slot, ItemStack itemStack) {
    }

    private static record PhantomSlotStateList(List<RecipeTransferUtil.PhantomSlotState> stateList, long totalItemCount) {

        public PhantomSlotStateList(List<RecipeTransferUtil.PhantomSlotState> states) {
            this(states, states.stream().mapToLong(it -> (long) it.itemStack.getCount()).sum());
        }

        public Stream<RecipeTransferUtil.PhantomSlotState> stream() {
            return this.stateList.stream().filter(it -> !it.itemStack.isEmpty());
        }
    }
}