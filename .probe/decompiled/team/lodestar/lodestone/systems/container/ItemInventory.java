package team.lodestar.lodestone.systems.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemInventory extends SimpleContainer {

    private final ItemStack stack;

    public ItemInventory(ItemStack stack, int expectedSize) {
        super(expectedSize);
        this.stack = stack;
        ListTag list = stack.getOrCreateTag().getList("items", 10);
        for (int i = 0; i < expectedSize && i < list.size(); i++) {
            this.m_6836_(i, ItemStack.of(list.getCompound(i)));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return !this.stack.isEmpty();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        ListTag list = new ListTag();
        for (int i = 0; i < this.m_6643_(); i++) {
            list.add(this.m_8020_(i).save(new CompoundTag()));
        }
        this.stack.getOrCreateTag().put("items", list);
    }
}