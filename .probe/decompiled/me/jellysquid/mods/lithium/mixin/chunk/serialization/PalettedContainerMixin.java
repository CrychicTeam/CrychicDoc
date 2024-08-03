package me.jellysquid.mods.lithium.mixin.chunk.serialization;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;
import me.jellysquid.mods.lithium.common.world.chunk.CompactingPackedIntegerArray;
import me.jellysquid.mods.lithium.common.world.chunk.LithiumHashPalette;
import net.minecraft.core.IdMap;
import net.minecraft.util.BitStorage;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.util.ZeroBitStorage;
import net.minecraft.world.level.chunk.Palette;
import net.minecraft.world.level.chunk.PaletteResize;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ PalettedContainer.class })
public abstract class PalettedContainerMixin<T> {

    private static final ThreadLocal<short[]> CACHED_ARRAY_4096 = ThreadLocal.withInitial(() -> new short[4096]);

    private static final ThreadLocal<short[]> CACHED_ARRAY_64 = ThreadLocal.withInitial(() -> new short[64]);

    @Shadow
    private volatile PalettedContainer.Data<T> data;

    @Shadow
    @Final
    private PaletteResize<T> dummyPaletteResize;

    @Shadow
    public abstract void acquire();

    @Shadow
    protected abstract T get(int var1);

    @Shadow
    public abstract void release();

    @Overwrite
    public PalettedContainerRO.PackedData<T> pack(IdMap<T> idList, PalettedContainer.Strategy provider) {
        this.acquire();
        LithiumHashPalette<T> hashPalette = null;
        Optional<LongStream> data = Optional.empty();
        List<T> elements = null;
        Palette<T> palette = this.data.palette();
        BitStorage storage = this.data.storage();
        if (storage instanceof ZeroBitStorage || palette.getSize() == 1) {
            elements = List.of(palette.valueFor(0));
        } else if (palette instanceof LithiumHashPalette<T> lithiumHashPalette) {
            hashPalette = lithiumHashPalette;
        }
        if (elements == null) {
            LithiumHashPalette<T> compactedPalette = new LithiumHashPalette<>(idList, storage.getBits(), this.dummyPaletteResize);
            short[] array = this.getOrCreate(provider.size());
            ((CompactingPackedIntegerArray) storage).compact(this.data.palette(), compactedPalette, array);
            if (hashPalette != null && hashPalette.getSize() == compactedPalette.getSize() && storage.getBits() == provider.calculateBitsForSerialization(idList, hashPalette.getSize())) {
                data = this.asOptional((long[]) storage.getRaw().clone());
                elements = hashPalette.getElements();
            } else {
                int bits = provider.calculateBitsForSerialization(idList, compactedPalette.getSize());
                if (bits != 0) {
                    SimpleBitStorage copy = new SimpleBitStorage(bits, array.length);
                    for (int i = 0; i < array.length; i++) {
                        copy.set(i, array[i]);
                    }
                    data = this.asOptional(copy.getRaw());
                }
                elements = compactedPalette.getElements();
            }
        }
        this.release();
        return new PalettedContainerRO.PackedData<>(elements, data);
    }

    private Optional<LongStream> asOptional(long[] data) {
        return Optional.of(Arrays.stream(data));
    }

    private short[] getOrCreate(int size) {
        return switch(size) {
            case 64 ->
                (short[]) CACHED_ARRAY_64.get();
            case 4096 ->
                (short[]) CACHED_ARRAY_4096.get();
            default ->
                new short[size];
        };
    }

    @Inject(method = { "count(Lnet/minecraft/world/chunk/PalettedContainer$Counter;)V" }, at = { @At("HEAD") }, cancellable = true)
    public void count(PalettedContainer.CountConsumer<T> consumer, CallbackInfo ci) {
        int len = this.data.palette().getSize();
        if (len <= 4096) {
            short[] counts = new short[len];
            this.data.storage().getAll(ix -> counts[ix]++);
            for (int i = 0; i < counts.length; i++) {
                T obj = this.data.palette().valueFor(i);
                if (obj != null) {
                    consumer.accept(obj, counts[i]);
                }
            }
            ci.cancel();
        }
    }
}