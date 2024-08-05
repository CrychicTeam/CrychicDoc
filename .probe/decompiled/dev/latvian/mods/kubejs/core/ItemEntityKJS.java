package dev.latvian.mods.kubejs.core;

import dev.architectury.hooks.level.entity.ItemEntityHooks;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@RemapPrefixForJS("kjs$")
public interface ItemEntityKJS extends EntityKJS {

    default ItemEntity kjs$self() {
        return (ItemEntity) this;
    }

    @Nullable
    @Override
    default ItemStack kjs$getItem() {
        ItemStack stack = this.kjs$self().getItem();
        return stack.isEmpty() ? null : stack;
    }

    default int kjs$getLifespan() {
        return ItemEntityHooks.lifespan(this.kjs$self()).getAsInt();
    }

    default void kjs$setLifespan(int lifespan) {
        ItemEntityHooks.lifespan(this.kjs$self()).accept(lifespan);
    }

    default void kjs$setDefaultPickUpDelay() {
        this.kjs$self().setPickUpDelay(10);
    }

    default void kjs$setNoPickUpDelay() {
        this.kjs$self().setPickUpDelay(0);
    }

    default void kjs$setInfinitePickUpDelay() {
        this.kjs$self().setPickUpDelay(32767);
    }

    default void kjs$setNoDespawn() {
        this.kjs$self().setUnlimitedLifetime();
    }

    default int kjs$getTicksUntilDespawn() {
        return this.kjs$getLifespan() - this.kjs$self().age;
    }

    default void kjs$setTicksUntilDespawn(int ticks) {
        this.kjs$self().age = this.kjs$getLifespan() - ticks;
    }
}