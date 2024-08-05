package io.github.lightman314.lightmanscurrency.common.menus.slots;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SimpleSlot extends Slot {

    public boolean active = true;

    public boolean locked = false;

    private Runnable onChange = () -> {
    };

    public SimpleSlot(Container container, int index, int x, int y) {
        super(container, index, x, y);
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return this.locked ? false : super.mayPlace(stack);
    }

    @NotNull
    @Override
    public ItemStack remove(int amount) {
        return this.locked ? ItemStack.EMPTY : super.remove(amount);
    }

    @Override
    public boolean mayPickup(@NotNull Player player) {
        return this.locked ? false : super.mayPickup(player);
    }

    public final void setListener(@Nonnull Runnable onChange) {
        this.onChange = onChange;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.onChange.run();
    }

    public static void SetActive(AbstractContainerMenu menu) {
        SetActive(menu, slot -> true);
    }

    public static void SetActive(AbstractContainerMenu menu, Function<SimpleSlot, Boolean> filter) {
        menu.slots.forEach(slot -> {
            if (slot instanceof SimpleSlot simpleSlot && (Boolean) filter.apply(simpleSlot)) {
                simpleSlot.active = true;
            }
        });
    }

    public static void SetInactive(AbstractContainerMenu menu) {
        SetInactive(menu, slot -> true);
    }

    public static void SetInactive(AbstractContainerMenu menu, Function<SimpleSlot, Boolean> filter) {
        menu.slots.forEach(slot -> {
            if (slot instanceof SimpleSlot simpleSlot && (Boolean) filter.apply(simpleSlot)) {
                simpleSlot.active = false;
            }
        });
    }

    public static void SetActive(List<? extends SimpleSlot> slots) {
        SetActive(slots, true);
    }

    public static void SetInactive(List<? extends SimpleSlot> slots) {
        SetActive(slots, false);
    }

    public static void SetActive(List<? extends SimpleSlot> slots, boolean active) {
        for (SimpleSlot slot : slots) {
            slot.active = active;
        }
    }

    public static void SetLocked(AbstractContainerMenu menu, boolean locked) {
        SetLocked(menu, locked, slot -> true);
    }

    public static void SetLocked(AbstractContainerMenu menu, boolean locked, Function<SimpleSlot, Boolean> filter) {
        menu.slots.forEach(slot -> {
            if (slot instanceof SimpleSlot simpleSlot && (Boolean) filter.apply(simpleSlot)) {
                simpleSlot.locked = locked;
            }
        });
    }

    public static void Lock(AbstractContainerMenu menu) {
        SetLocked(menu, true);
    }

    public static void Lock(AbstractContainerMenu menu, Function<SimpleSlot, Boolean> filter) {
        SetLocked(menu, true, filter);
    }

    public static void Unlock(AbstractContainerMenu menu) {
        SetLocked(menu, false);
    }

    public static void Unlock(AbstractContainerMenu menu, Function<SimpleSlot, Boolean> filter) {
        SetLocked(menu, false, filter);
    }
}