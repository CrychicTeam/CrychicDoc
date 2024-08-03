package me.jellysquid.mods.sodium.mixin.core.world.chunk;

import java.util.Objects;
import me.jellysquid.mods.sodium.client.world.PaletteStorageExtended;
import me.jellysquid.mods.sodium.client.world.ReadableContainerExtended;
import net.minecraft.util.BitStorage;
import net.minecraft.world.level.chunk.Palette;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ PalettedContainer.class })
public abstract class PalettedContainerMixin<T> implements ReadableContainerExtended<T> {

    @Shadow
    private volatile PalettedContainer.Data<T> data;

    @Shadow
    @Final
    private PalettedContainer.Strategy strategy;

    @Shadow
    public abstract PalettedContainer<T> copy();

    @Override
    public void sodium$unpack(T[] values) {
        PalettedContainer.Strategy indexer = (PalettedContainer.Strategy) Objects.requireNonNull(this.strategy);
        if (values.length != indexer.size()) {
            throw new IllegalArgumentException("Array is wrong size");
        } else {
            PalettedContainer.Data<T> data = (PalettedContainer.Data<T>) Objects.requireNonNull(this.data, "PalettedContainer must have data");
            PaletteStorageExtended storage = (PaletteStorageExtended) data.storage();
            storage.sodium$unpack(values, data.palette());
        }
    }

    @Override
    public void sodium$unpack(T[] values, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        PalettedContainer.Strategy indexer = (PalettedContainer.Strategy) Objects.requireNonNull(this.strategy);
        if (values.length != indexer.size()) {
            throw new IllegalArgumentException("Array is wrong size");
        } else {
            PalettedContainer.Data<T> data = (PalettedContainer.Data<T>) Objects.requireNonNull(this.data, "PalettedContainer must have data");
            BitStorage storage = data.storage();
            Palette<T> palette = data.palette();
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    for (int x = minX; x <= maxX; x++) {
                        int localBlockIndex = indexer.getIndex(x, y, z);
                        int paletteIndex = storage.get(localBlockIndex);
                        T paletteValue = palette.valueFor(paletteIndex);
                        values[localBlockIndex] = (T) Objects.requireNonNull(paletteValue, "Palette does not contain entry for value in storage");
                    }
                }
            }
        }
    }

    @Override
    public PalettedContainerRO<T> sodium$copy() {
        return this.copy();
    }
}