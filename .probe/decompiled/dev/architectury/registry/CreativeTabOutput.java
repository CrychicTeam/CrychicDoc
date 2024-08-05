package dev.architectury.registry;

import java.util.Collection;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@NonExtendable
@Experimental
public interface CreativeTabOutput extends CreativeModeTab.Output {

    void acceptAfter(ItemStack var1, ItemStack var2, CreativeModeTab.TabVisibility var3);

    void acceptBefore(ItemStack var1, ItemStack var2, CreativeModeTab.TabVisibility var3);

    @Override
    default void accept(ItemStack stack, CreativeModeTab.TabVisibility visibility) {
        this.acceptAfter(ItemStack.EMPTY, stack, visibility);
    }

    default void acceptAfter(ItemStack after, ItemStack stack) {
        this.acceptAfter(after, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    default void acceptAfter(ItemStack after, ItemLike item, CreativeModeTab.TabVisibility visibility) {
        this.acceptAfter(after, new ItemStack(item), visibility);
    }

    default void acceptAfter(ItemStack after, ItemLike item) {
        this.acceptAfter(after, new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    default void acceptAllAfter(ItemStack after, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility visibility) {
        stacks.forEach(stack -> this.acceptAfter(after, stack, visibility));
    }

    default void acceptAllAfter(ItemStack after, Collection<ItemStack> stacks) {
        this.acceptAllAfter(after, stacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    default void acceptAfter(ItemLike after, ItemStack stack) {
        this.acceptAfter(new ItemStack(after), stack);
    }

    default void acceptAfter(ItemLike after, ItemLike item, CreativeModeTab.TabVisibility visibility) {
        this.acceptAfter(new ItemStack(after), item, visibility);
    }

    default void acceptAfter(ItemLike after, ItemLike item) {
        this.acceptAfter(new ItemStack(after), item);
    }

    default void acceptAllAfter(ItemLike after, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility visibility) {
        this.acceptAllAfter(new ItemStack(after), stacks, visibility);
    }

    default void acceptAllAfter(ItemLike after, Collection<ItemStack> stacks) {
        this.acceptAllAfter(new ItemStack(after), stacks);
    }

    default void acceptBefore(ItemStack before, ItemStack stack) {
        this.acceptBefore(before, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    default void acceptBefore(ItemStack before, ItemLike item, CreativeModeTab.TabVisibility visibility) {
        this.acceptBefore(before, new ItemStack(item), visibility);
    }

    default void acceptBefore(ItemStack before, ItemLike item) {
        this.acceptBefore(before, new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    default void acceptAllBefore(ItemStack before, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility visibility) {
        stacks.forEach(stack -> this.acceptBefore(before, stack, visibility));
    }

    default void acceptAllBefore(ItemStack before, Collection<ItemStack> stacks) {
        this.acceptAllBefore(before, stacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    default void acceptBefore(ItemLike before, ItemStack stack) {
        this.acceptBefore(new ItemStack(before), stack);
    }

    default void acceptBefore(ItemLike before, ItemLike item, CreativeModeTab.TabVisibility visibility) {
        this.acceptBefore(new ItemStack(before), item, visibility);
    }

    default void acceptBefore(ItemLike before, ItemLike item) {
        this.acceptBefore(new ItemStack(before), item);
    }

    default void acceptAllBefore(ItemLike before, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility visibility) {
        this.acceptAllBefore(new ItemStack(before), stacks, visibility);
    }

    default void acceptAllBefore(ItemLike before, Collection<ItemStack> stacks) {
        this.acceptAllBefore(new ItemStack(before), stacks);
    }
}