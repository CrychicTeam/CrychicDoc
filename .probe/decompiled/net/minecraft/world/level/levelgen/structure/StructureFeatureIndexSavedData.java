package net.minecraft.world.level.levelgen.structure;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class StructureFeatureIndexSavedData extends SavedData {

    private static final String TAG_REMAINING_INDEXES = "Remaining";

    private static final String TAG_All_INDEXES = "All";

    private final LongSet all;

    private final LongSet remaining;

    private StructureFeatureIndexSavedData(LongSet longSet0, LongSet longSet1) {
        this.all = longSet0;
        this.remaining = longSet1;
    }

    public StructureFeatureIndexSavedData() {
        this(new LongOpenHashSet(), new LongOpenHashSet());
    }

    public static StructureFeatureIndexSavedData load(CompoundTag compoundTag0) {
        return new StructureFeatureIndexSavedData(new LongOpenHashSet(compoundTag0.getLongArray("All")), new LongOpenHashSet(compoundTag0.getLongArray("Remaining")));
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag0) {
        compoundTag0.putLongArray("All", this.all.toLongArray());
        compoundTag0.putLongArray("Remaining", this.remaining.toLongArray());
        return compoundTag0;
    }

    public void addIndex(long long0) {
        this.all.add(long0);
        this.remaining.add(long0);
    }

    public boolean hasStartIndex(long long0) {
        return this.all.contains(long0);
    }

    public boolean hasUnhandledIndex(long long0) {
        return this.remaining.contains(long0);
    }

    public void removeIndex(long long0) {
        this.remaining.remove(long0);
    }

    public LongSet getAll() {
        return this.all;
    }
}