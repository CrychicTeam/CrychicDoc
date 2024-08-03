package top.theillusivec4.curios.api.type.inventory;

import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IDynamicStackHandler extends IItemHandlerModifiable {

    @Override
    void setStackInSlot(int var1, @Nonnull ItemStack var2);

    @Nonnull
    @Override
    ItemStack getStackInSlot(int var1);

    void setPreviousStackInSlot(int var1, @Nonnull ItemStack var2);

    ItemStack getPreviousStackInSlot(int var1);

    @Override
    int getSlots();

    void grow(int var1);

    void shrink(int var1);

    CompoundTag serializeNBT();

    void deserializeNBT(CompoundTag var1);
}