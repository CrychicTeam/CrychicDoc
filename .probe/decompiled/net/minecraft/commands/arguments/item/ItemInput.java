package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemInput implements Predicate<ItemStack> {

    private static final Dynamic2CommandExceptionType ERROR_STACK_TOO_BIG = new Dynamic2CommandExceptionType((p_120986_, p_120987_) -> Component.translatable("arguments.item.overstacked", p_120986_, p_120987_));

    private final Holder<Item> item;

    @Nullable
    private final CompoundTag tag;

    public ItemInput(Holder<Item> holderItem0, @Nullable CompoundTag compoundTag1) {
        this.item = holderItem0;
        this.tag = compoundTag1;
    }

    public Item getItem() {
        return this.item.value();
    }

    public boolean test(ItemStack itemStack0) {
        return itemStack0.is(this.item) && NbtUtils.compareNbt(this.tag, itemStack0.getTag(), true);
    }

    public ItemStack createItemStack(int int0, boolean boolean1) throws CommandSyntaxException {
        ItemStack $$2 = new ItemStack(this.item, int0);
        if (this.tag != null) {
            $$2.setTag(this.tag);
        }
        if (boolean1 && int0 > $$2.getMaxStackSize()) {
            throw ERROR_STACK_TOO_BIG.create(this.getItemName(), $$2.getMaxStackSize());
        } else {
            return $$2;
        }
    }

    public String serialize() {
        StringBuilder $$0 = new StringBuilder(this.getItemName());
        if (this.tag != null) {
            $$0.append(this.tag);
        }
        return $$0.toString();
    }

    private String getItemName() {
        return this.item.unwrapKey().map(ResourceKey::m_135782_).orElseGet(() -> "unknown[" + this.item + "]").toString();
    }
}