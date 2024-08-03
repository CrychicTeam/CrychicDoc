package malte0811.ferritecore.fastmap;

import com.google.common.base.Preconditions;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.Property;

public class BinaryFastMapKey<T extends Comparable<T>> extends FastMapKey<T> {

    private final byte firstBitInValue;

    private final byte firstBitAfterValue;

    public BinaryFastMapKey(Property<T> property, int mapFactor) {
        super(property);
        Preconditions.checkArgument(Mth.isPowerOfTwo(mapFactor));
        int addedFactor = Mth.smallestEncompassingPowerOfTwo(this.numValues());
        Preconditions.checkState(this.numValues() <= addedFactor);
        Preconditions.checkState(addedFactor < 2 * this.numValues());
        int setBitInBaseFactor = Mth.log2(mapFactor);
        int setBitInAddedFactor = Mth.log2(addedFactor);
        Preconditions.checkState(setBitInBaseFactor + setBitInAddedFactor <= 31);
        this.firstBitInValue = (byte) setBitInBaseFactor;
        this.firstBitAfterValue = (byte) (setBitInBaseFactor + setBitInAddedFactor);
    }

    @Override
    public T getValue(int mapIndex) {
        int clearAbove = mapIndex & this.lowestNBits(this.firstBitAfterValue);
        return this.byInternalIndex(clearAbove >>> this.firstBitInValue);
    }

    @Override
    public int replaceIn(int mapIndex, T newValue) {
        int newPartialIndex = this.toPartialMapIndex(newValue);
        if (newPartialIndex < 0) {
            return -1;
        } else {
            int keepMask = ~this.lowestNBits(this.firstBitAfterValue) | this.lowestNBits(this.firstBitInValue);
            return keepMask & mapIndex | newPartialIndex;
        }
    }

    @Override
    public int toPartialMapIndex(Comparable<?> value) {
        int internalIndex = this.getInternalIndex(value);
        return internalIndex >= 0 && internalIndex < this.numValues() ? internalIndex << this.firstBitInValue : -1;
    }

    @Override
    public int getFactorToNext() {
        return 1 << this.firstBitAfterValue - this.firstBitInValue;
    }

    private int lowestNBits(byte n) {
        return n >= 32 ? -1 : (1 << n) - 1;
    }
}