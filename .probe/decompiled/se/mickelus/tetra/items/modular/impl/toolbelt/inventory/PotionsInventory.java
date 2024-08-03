package se.mickelus.tetra.items.modular.impl.toolbelt.inventory;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.impl.toolbelt.ModularToolbeltItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.SlotType;

@ParametersAreNonnullByDefault
public class PotionsInventory extends ToolbeltInventory {

    private static final String inventoryKey = "potionsInventory";

    public static int maxSize = 10;

    public PotionsInventory(ItemStack stack) {
        super("potionsInventory", stack, maxSize, SlotType.potion);
        ModularToolbeltItem item = (ModularToolbeltItem) stack.getItem();
        this.numSlots = item.getNumSlots(stack, SlotType.potion);
        this.predicate = getPredicate("potion");
        this.readFromNBT(stack.getOrCreateTag());
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack itemStack) {
        return this.isItemValid(itemStack);
    }

    @Override
    public boolean storeItemInInventory(ItemStack itemStack) {
        if (!this.isItemValid(itemStack)) {
            return false;
        } else {
            for (int i = 0; i < this.m_6643_(); i++) {
                ItemStack storedStack = this.m_8020_(i);
                if (ItemStack.isSameItemSameTags(storedStack, itemStack) && storedStack.getCount() < 64) {
                    int moveCount = Math.min(itemStack.getCount(), 64 - storedStack.getCount());
                    storedStack.grow(moveCount);
                    this.m_6836_(i, storedStack);
                    itemStack.shrink(moveCount);
                    if (itemStack.isEmpty()) {
                        return true;
                    }
                }
            }
            for (int ix = 0; ix < this.m_6643_(); ix++) {
                if (this.m_8020_(ix).isEmpty()) {
                    this.m_6836_(ix, itemStack);
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public ItemStack takeItemStack(int index) {
        ItemStack itemStack = this.m_8020_(index);
        itemStack = itemStack.split(itemStack.getMaxStackSize());
        if (this.m_8020_(index).isEmpty()) {
            this.m_6836_(index, ItemStack.EMPTY);
        } else {
            this.m_6596_();
        }
        return itemStack;
    }

    @Override
    public void emptyOverflowSlots(Player player) {
        for (int i = this.m_6643_(); i < maxSize; i++) {
            ItemStack itemStack = this.m_8020_(i);
            while (!itemStack.isEmpty()) {
                this.moveStackToPlayer(itemStack.split(itemStack.getMaxStackSize()), player);
            }
        }
        this.m_6596_();
    }
}