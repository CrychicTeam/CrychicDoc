package net.minecraft.world.level;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class ForcedChunksSavedData extends SavedData {

    public static final String FILE_ID = "chunks";

    private static final String TAG_FORCED = "Forced";

    private final LongSet chunks;

    private ForcedChunksSavedData(LongSet longSet0) {
        this.chunks = longSet0;
    }

    public ForcedChunksSavedData() {
        this(new LongOpenHashSet());
    }

    public static ForcedChunksSavedData load(CompoundTag compoundTag0) {
        return new ForcedChunksSavedData(new LongOpenHashSet(compoundTag0.getLongArray("Forced")));
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag0) {
        compoundTag0.putLongArray("Forced", this.chunks.toLongArray());
        return compoundTag0;
    }

    public LongSet getChunks() {
        return this.chunks;
    }
}