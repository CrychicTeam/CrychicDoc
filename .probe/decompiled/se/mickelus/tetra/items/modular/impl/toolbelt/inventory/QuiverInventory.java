package se.mickelus.tetra.items.modular.impl.toolbelt.inventory;

import java.util.ArrayList;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.impl.toolbelt.ModularToolbeltItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.SlotType;

@ParametersAreNonnullByDefault
public class QuiverInventory extends ToolbeltInventory {

    private static final String inventoryKey = "quiverInventory";

    public static int maxSize = 30;

    public QuiverInventory(ItemStack stack) {
        super("quiverInventory", stack, maxSize, SlotType.quiver);
        ModularToolbeltItem item = (ModularToolbeltItem) stack.getItem();
        this.numSlots = item.getNumSlots(stack, SlotType.quiver);
        this.predicate = getPredicate("quiver");
        this.readFromNBT(stack.getOrCreateTag());
    }

    public ItemStack[] getAggregatedStacks() {
        ArrayList<ItemStack> aggregatedStacks = new ArrayList();
        for (ItemStack itemStack : this.inventoryContents) {
            boolean found = false;
            for (ItemStack aggregatedStack : aggregatedStacks) {
                if (ItemStack.isSameItemSameTags(itemStack, aggregatedStack)) {
                    found = true;
                    aggregatedStack.grow(itemStack.getCount());
                    break;
                }
            }
            if (!found && !itemStack.isEmpty()) {
                aggregatedStacks.add(itemStack.copy());
            }
        }
        return (ItemStack[]) aggregatedStacks.toArray(new ItemStack[aggregatedStacks.size()]);
    }

    public int getFirstIndexForStack(ItemStack itemStack) {
        for (int i = 0; i < this.inventoryContents.size(); i++) {
            if (ItemStack.isSameItemSameTags(itemStack, this.inventoryContents.get(i))) {
                return i;
            }
        }
        return -1;
    }
}