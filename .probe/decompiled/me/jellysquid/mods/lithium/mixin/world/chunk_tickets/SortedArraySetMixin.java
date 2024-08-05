package me.jellysquid.mods.lithium.mixin.world.chunk_tickets;

import java.util.Collection;
import java.util.function.Predicate;
import net.minecraft.util.SortedArraySet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ SortedArraySet.class })
public abstract class SortedArraySetMixin<T> implements Collection<T> {

    @Shadow
    int size;

    @Shadow
    T[] contents;

    public boolean removeIf(Predicate<? super T> filter) {
        T[] arr = this.contents;
        int writeLim = this.size;
        int writeIdx = 0;
        for (int readIdx = 0; readIdx < writeLim; readIdx++) {
            T obj = arr[readIdx];
            if (!filter.test(obj)) {
                if (writeIdx != readIdx) {
                    arr[writeIdx] = obj;
                }
                writeIdx++;
            }
        }
        this.size = writeIdx;
        return writeLim != writeIdx;
    }
}