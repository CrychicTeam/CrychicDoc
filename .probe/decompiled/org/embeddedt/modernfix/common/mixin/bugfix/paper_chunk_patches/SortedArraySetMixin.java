package org.embeddedt.modernfix.common.mixin.bugfix.paper_chunk_patches;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.function.Predicate;
import net.minecraft.util.SortedArraySet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ SortedArraySet.class })
public abstract class SortedArraySetMixin<T> extends AbstractSet<T> {

    @Shadow
    private int size;

    @Shadow
    private T[] contents;

    public boolean removeIf(Predicate<? super T> filter) {
        int i = 0;
        int len = this.size;
        for (T[] backingArray = this.contents; i < len; i++) {
            if (filter.test(backingArray[i])) {
                int lastIndex;
                for (lastIndex = i; i < len; i++) {
                    T curr = backingArray[i];
                    if (!filter.test(curr)) {
                        backingArray[lastIndex++] = curr;
                    }
                }
                Arrays.fill(backingArray, lastIndex, len, null);
                this.size = lastIndex;
                return true;
            }
        }
        return false;
    }
}