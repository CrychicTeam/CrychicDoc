package se.mickelus.tetra.items.modular.impl.toolbelt.inventory;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.impl.toolbelt.ModularToolbeltItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.SlotType;

@ParametersAreNonnullByDefault
public class QuickslotInventory extends ToolbeltInventory {

    public static final int maxSize = 12;

    private static final String inventoryKey = "quickInventory";

    private static final String shadowsKey = "quickShadows";

    private final NonNullList<ItemStack> inventoryShadows;

    public QuickslotInventory(ItemStack stack) {
        super("quickInventory", stack, 12, SlotType.quick);
        ModularToolbeltItem item = (ModularToolbeltItem) stack.getItem();
        this.numSlots = item.getNumSlots(stack, SlotType.quick);
        this.inventoryShadows = NonNullList.withSize(12, ItemStack.EMPTY);
        this.predicate = getPredicate("quickslot");
        this.readFromNBT(stack.getOrCreateTag());
    }

    @Override
    public void readFromNBT(CompoundTag tagCompound) {
        super.readFromNBT(tagCompound);
        ListTag shadows = tagCompound.getList("quickShadows", 10);
        for (int i = 0; i < shadows.size(); i++) {
            CompoundTag item = shadows.getCompound(i);
            int slot = item.getInt("slot");
            if (0 <= slot && slot < this.m_6643_()) {
                this.inventoryShadows.set(slot, ItemStack.of(item));
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag tagcompound) {
        super.writeToNBT(tagcompound);
        ListTag shadows = new ListTag();
        for (int i = 0; i < 12; i++) {
            CompoundTag item = new CompoundTag();
            item.putInt("slot", i);
            this.getShadowOfSlot(i).save(item);
            shadows.add(item);
        }
        tagcompound.put("quickShadows", shadows);
    }

    public ItemStack getShadowOfSlot(int index) {
        return this.inventoryShadows.get(index);
    }

    @Override
    public void setChanged() {
        for (int i = 0; i < this.m_6643_(); i++) {
            if (this.m_8020_(i).getCount() == 0) {
                this.inventoryContents.set(i, ItemStack.EMPTY);
            }
        }
        for (int ix = 0; ix < this.m_6643_(); ix++) {
            if (!this.m_8020_(ix).isEmpty()) {
                this.inventoryShadows.set(ix, this.m_8020_(ix).copy());
            }
        }
        this.writeToNBT(this.toolbeltItemStack.getOrCreateTag());
    }

    private int getShadowIndex(ItemStack itemStack) {
        for (int i = 0; i < this.m_6643_(); i++) {
            if (itemStack.is(this.getShadowOfSlot(i).getItem()) && this.m_8020_(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean storeItemInInventory(ItemStack itemStack) {
        if (!this.isItemValid(itemStack)) {
            return false;
        } else {
            for (int i = 0; i < this.m_6643_(); i++) {
                ItemStack storedStack = this.m_8020_(i);
                if (storedStack.is(itemStack.getItem()) && storedStack.getCount() < storedStack.getMaxStackSize()) {
                    int moveCount = Math.min(itemStack.getCount(), storedStack.getMaxStackSize() - storedStack.getCount());
                    storedStack.grow(moveCount);
                    this.m_6836_(i, storedStack);
                    itemStack.shrink(moveCount);
                    if (itemStack.isEmpty()) {
                        return true;
                    }
                }
            }
            int restockIndex = this.getShadowIndex(itemStack);
            if (restockIndex != -1) {
                this.m_6836_(restockIndex, itemStack);
                return true;
            } else {
                for (int ix = 0; ix < this.m_6643_(); ix++) {
                    if (this.m_8020_(ix).isEmpty()) {
                        this.m_6836_(ix, itemStack);
                        return true;
                    }
                }
                return false;
            }
        }
    }
}