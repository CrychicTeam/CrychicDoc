package net.minecraftforge.event.brewing;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;

public class PotionBrewEvent extends Event {

    private NonNullList<ItemStack> stacks;

    protected PotionBrewEvent(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }

    @NotNull
    public ItemStack getItem(int index) {
        return index >= 0 && index < this.stacks.size() ? this.stacks.get(index) : ItemStack.EMPTY;
    }

    public void setItem(int index, @NotNull ItemStack stack) {
        if (index < this.stacks.size()) {
            this.stacks.set(index, stack);
        }
    }

    public int getLength() {
        return this.stacks.size();
    }

    public static class Post extends PotionBrewEvent {

        public Post(NonNullList<ItemStack> stacks) {
            super(stacks);
        }
    }

    @Cancelable
    public static class Pre extends PotionBrewEvent {

        public Pre(NonNullList<ItemStack> stacks) {
            super(stacks);
        }
    }
}