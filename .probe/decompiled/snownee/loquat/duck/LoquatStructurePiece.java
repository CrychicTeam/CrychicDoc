package snownee.loquat.duck;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.tuple.Pair;

public interface LoquatStructurePiece {

    ThreadLocal<Pair<LoquatStructurePiece, RegistryAccess>> CURRENT = new ThreadLocal();

    static LoquatStructurePiece current() {
        Pair<LoquatStructurePiece, RegistryAccess> pair = (Pair<LoquatStructurePiece, RegistryAccess>) CURRENT.get();
        return pair == null ? null : (LoquatStructurePiece) pair.getLeft();
    }

    void loquat$setAttachedData(CompoundTag var1);

    CompoundTag loquat$getAttachedData();
}