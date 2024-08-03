package net.minecraftforge.common.extensions;

import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public interface IForgeMobEffectInstance {

    List<ItemStack> getCurativeItems();

    default boolean isCurativeItem(ItemStack stack) {
        return this.getCurativeItems().stream().anyMatch(e -> ItemStack.isSameItem(e, stack));
    }

    void setCurativeItems(List<ItemStack> var1);

    default void addCurativeItem(ItemStack stack) {
        if (!this.isCurativeItem(stack)) {
            this.getCurativeItems().add(stack);
        }
    }

    default void writeCurativeItems(CompoundTag nbt) {
        ListTag list = new ListTag();
        this.getCurativeItems().forEach(s -> list.add(s.save(new CompoundTag())));
        nbt.put("CurativeItems", list);
    }
}