package malte0811.ferritecore.fastmap;

import net.minecraft.world.level.block.state.properties.Property;

public abstract class FastMapKey<T extends Comparable<T>> {

    private final PropertyIndexer<T> indexer;

    protected FastMapKey(Property<T> property) {
        this.indexer = PropertyIndexer.makeIndexer(property);
    }

    public abstract T getValue(int var1);

    abstract int replaceIn(int var1, T var2);

    abstract int toPartialMapIndex(Comparable<?> var1);

    abstract int getFactorToNext();

    public final int numValues() {
        return this.indexer.numValues();
    }

    public final Property<T> getProperty() {
        return this.indexer.getProperty();
    }

    protected final int getInternalIndex(Comparable<?> value) {
        return this.indexer.toIndex((T) value);
    }

    protected final T byInternalIndex(int internalIndex) {
        return this.indexer.byIndex(internalIndex);
    }
}