package se.mickelus.tetra.items.modular.impl.toolbelt.inventory;

import java.util.Collection;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.impl.toolbelt.ModularToolbeltItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.SlotType;

@ParametersAreNonnullByDefault
public class StorageInventory extends ToolbeltInventory {

    private static final String inventoryKey = "storageInventory";

    public static int maxSize = 36;

    public StorageInventory(ItemStack stack) {
        super("storageInventory", stack, maxSize, SlotType.storage);
        ModularToolbeltItem item = (ModularToolbeltItem) stack.getItem();
        this.numSlots = item.getNumSlots(stack, SlotType.storage);
        this.predicate = getPredicate("storage");
        this.readFromNBT(stack.getOrCreateTag());
    }

    public static int getColumns(int slotCount) {
        for (int i = 12; i >= 5; i--) {
            if (slotCount % i == 0) {
                return i;
            }
        }
        return 9;
    }

    @Override
    public boolean storeItemInInventory(ItemStack itemStack) {
        if (!this.isItemValid(itemStack)) {
            return false;
        } else {
            List<Collection<ItemEffect>> effects = this.getSlotEffects();
            for (int i = 0; i < this.m_6643_(); i++) {
                ItemStack storedStack = this.m_8020_(i);
                if (((Collection) effects.get(i)).contains(ItemEffect.quickAccess) && storedStack.is(itemStack.getItem()) && storedStack.getCount() < storedStack.getMaxStackSize()) {
                    int moveCount = Math.min(itemStack.getCount(), storedStack.getMaxStackSize() - storedStack.getCount());
                    storedStack.grow(moveCount);
                    this.m_6836_(i, storedStack);
                    itemStack.shrink(moveCount);
                    if (itemStack.isEmpty()) {
                        return true;
                    }
                }
            }
            for (int ix = 0; ix < this.m_6643_(); ix++) {
                if (((Collection) effects.get(ix)).contains(ItemEffect.quickAccess) && this.m_8020_(ix).isEmpty()) {
                    this.m_6836_(ix, itemStack);
                    return true;
                }
            }
            return false;
        }
    }
}