package me.jellysquid.mods.lithium.mixin.chunk.serialization;

import me.jellysquid.mods.lithium.common.world.chunk.CompactingPackedIntegerArray;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.world.level.chunk.Palette;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ SimpleBitStorage.class })
public abstract class PackedIntegerArrayMixin implements CompactingPackedIntegerArray {

    @Shadow
    @Final
    private int size;

    @Shadow
    @Final
    private int bits;

    @Shadow
    @Final
    private long mask;

    @Shadow
    @Final
    private int valuesPerLong;

    @Shadow
    @Final
    private long[] data;

    @Override
    public <T> void compact(Palette<T> srcPalette, Palette<T> dstPalette, short[] out) {
        if (this.size >= 32767) {
            throw new IllegalStateException("Array too large");
        } else if (this.size != out.length) {
            throw new IllegalStateException("Array size mismatch");
        } else {
            short[] mappings = new short[(int) (this.mask + 1L)];
            int idx = 0;
            for (long word : this.data) {
                long bits = word;
                for (int elementIdx = 0; elementIdx < this.valuesPerLong; elementIdx++) {
                    int value = (int) (bits & this.mask);
                    int remappedId = mappings[value];
                    if (remappedId == 0) {
                        remappedId = dstPalette.idFor(srcPalette.valueFor(value)) + 1;
                        mappings[value] = (short) remappedId;
                    }
                    out[idx] = (short) (remappedId - 1);
                    bits >>= this.bits;
                    if (++idx >= this.size) {
                        return;
                    }
                }
            }
        }
    }
}