package com.simibubi.create.foundation.block;

import com.tterrag.registrate.util.entry.BlockEntry;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

public class DyedBlockList<T extends Block> implements Iterable<BlockEntry<T>> {

    private static final int COLOR_AMOUNT = DyeColor.values().length;

    private final BlockEntry<?>[] values = new BlockEntry[COLOR_AMOUNT];

    public DyedBlockList(Function<DyeColor, BlockEntry<? extends T>> filler) {
        for (DyeColor color : DyeColor.values()) {
            this.values[color.ordinal()] = (BlockEntry<?>) filler.apply(color);
        }
    }

    public BlockEntry<T> get(DyeColor color) {
        return (BlockEntry<T>) this.values[color.ordinal()];
    }

    public boolean contains(Block block) {
        for (BlockEntry<?> entry : this.values) {
            if (entry.is(block)) {
                return true;
            }
        }
        return false;
    }

    public BlockEntry<T>[] toArray() {
        return (BlockEntry<T>[]) Arrays.copyOf(this.values, this.values.length);
    }

    public Iterator<BlockEntry<T>> iterator() {
        return new Iterator<BlockEntry<T>>() {

            private int index = 0;

            public boolean hasNext() {
                return this.index < DyedBlockList.this.values.length;
            }

            public BlockEntry<T> next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                } else {
                    return (BlockEntry<T>) DyedBlockList.this.values[this.index++];
                }
            }
        };
    }
}