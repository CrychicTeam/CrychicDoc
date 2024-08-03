package snownee.jade.addon.universal;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class ItemIterator<T> {

    public static final AtomicLong version = new AtomicLong();

    protected final Function<Object, T> containerFinder;

    protected final int fromIndex;

    protected boolean finished;

    protected int currentIndex;

    protected ItemIterator(Function<Object, T> containerFinder, int fromIndex) {
        this.containerFinder = containerFinder;
        this.currentIndex = this.fromIndex = fromIndex;
    }

    @Nullable
    public T find(Object target) {
        return (T) this.containerFinder.apply(target);
    }

    public final boolean isFinished() {
        return this.finished;
    }

    public long getVersion(T container) {
        return version.getAndIncrement();
    }

    public abstract Stream<ItemStack> populate(T var1);

    public void reset() {
        this.currentIndex = this.fromIndex;
        this.finished = false;
    }

    public void afterPopulate(int count) {
        this.currentIndex += count;
        if (count == 0 || this.currentIndex >= 10000) {
            this.finished = true;
        }
    }

    public float getCollectingProgress() {
        return Float.NaN;
    }

    public static class ContainerItemIterator extends ItemIterator.SlottedItemIterator<Container> {

        public ContainerItemIterator(int fromIndex) {
            this(Container.class::cast, fromIndex);
        }

        public ContainerItemIterator(Function<Object, Container> containerFinder, int fromIndex) {
            super(containerFinder, fromIndex);
        }

        protected int getSlotCount(Container container) {
            return container.getContainerSize();
        }

        protected ItemStack getItemInSlot(Container container, int slot) {
            return container.getItem(slot);
        }
    }

    public abstract static class SlotlessItemIterator<T> extends ItemIterator<T> {

        protected SlotlessItemIterator(Function<Object, T> containerFinder, int fromIndex) {
            super(containerFinder, fromIndex);
        }

        @Override
        public Stream<ItemStack> populate(T container) {
            return this.populateRaw(container).skip((long) this.currentIndex).limit(108L);
        }

        protected abstract Stream<ItemStack> populateRaw(T var1);
    }

    public abstract static class SlottedItemIterator<T> extends ItemIterator<T> {

        protected float progress;

        public SlottedItemIterator(Function<Object, T> containerFinder, int fromIndex) {
            super(containerFinder, fromIndex);
        }

        protected abstract int getSlotCount(T var1);

        protected abstract ItemStack getItemInSlot(T var1, int var2);

        @Override
        public Stream<ItemStack> populate(T container) {
            int slotCount = this.getSlotCount(container);
            int toIndex = this.currentIndex + 108;
            if (toIndex >= slotCount) {
                toIndex = slotCount;
                this.finished = true;
            }
            this.progress = (float) (this.currentIndex - this.fromIndex) / (float) (slotCount - this.fromIndex);
            return IntStream.range(this.currentIndex, toIndex).mapToObj(slot -> this.getItemInSlot(container, slot));
        }

        @Override
        public float getCollectingProgress() {
            return this.progress;
        }
    }
}