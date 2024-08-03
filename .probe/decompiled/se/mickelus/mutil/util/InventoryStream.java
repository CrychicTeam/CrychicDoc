package se.mickelus.mutil.util;

import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
public class InventoryStream {

    public static Stream<ItemStack> of(final Container inventory) {
        return StreamSupport.stream(new AbstractSpliterator<ItemStack>((long) inventory.getContainerSize(), 320) {

            int index = 0;

            public boolean tryAdvance(Consumer<? super ItemStack> consumer) {
                if (this.index < inventory.getContainerSize()) {
                    consumer.accept(inventory.getItem(this.index++));
                    return true;
                } else {
                    return false;
                }
            }
        }, false);
    }
}