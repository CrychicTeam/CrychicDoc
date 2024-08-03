package se.mickelus.tetra.items.modular.impl.toolbelt.inventory;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
public class PredicateSlot extends Slot {

    protected Predicate<ItemStack> predicate;

    public PredicateSlot(Container inventory, int index, int x, int y, Predicate<ItemStack> predicate) {
        super(inventory, index, x, y);
        this.predicate = predicate;
    }

    @Override
    public boolean mayPlace(@Nullable ItemStack itemStack) {
        return itemStack != null && this.predicate.test(itemStack);
    }
}