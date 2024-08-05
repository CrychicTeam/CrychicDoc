package snownee.jade.addon.universal;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.view.ViewGroup;

public class ItemCollector<T> {

    public static final int MAX_SIZE = 54;

    public static final ItemCollector<?> EMPTY = new ItemCollector(null);

    private static final Predicate<ItemStack> NON_EMPTY = stack -> {
        if (stack.isEmpty()) {
            return false;
        } else {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains("CustomModelData")) {
                for (String key : stack.getTag().getAllKeys()) {
                    if (key.toLowerCase(Locale.ENGLISH).endsWith("clear") && stack.getTag().getBoolean(key)) {
                        return false;
                    }
                }
            }
            return true;
        }
    };

    private final Object2IntLinkedOpenHashMap<ItemCollector.ItemDefinition> items = new Object2IntLinkedOpenHashMap();

    private final ItemIterator<T> iterator;

    public long version;

    public long lastTimeFinished;

    public List<ViewGroup<ItemStack>> mergedResult;

    public ItemCollector(ItemIterator<T> iterator) {
        this.iterator = iterator;
    }

    public List<ViewGroup<ItemStack>> update(Object target, long gameTime) {
        if (this.iterator == null) {
            return null;
        } else {
            T container = this.iterator.find(target);
            if (container == null) {
                return null;
            } else {
                long currentVersion = this.iterator.getVersion(container);
                if (this.mergedResult != null && this.iterator.isFinished()) {
                    if (this.version == currentVersion) {
                        return this.mergedResult;
                    }
                    if (this.lastTimeFinished + 5L > gameTime) {
                        return this.mergedResult;
                    }
                    this.iterator.reset();
                }
                AtomicInteger count = new AtomicInteger();
                this.iterator.populate(container).forEach(stack -> {
                    count.incrementAndGet();
                    if (NON_EMPTY.test(stack)) {
                        ItemCollector.ItemDefinition def = new ItemCollector.ItemDefinition(stack);
                        this.items.addTo(def, stack.getCount());
                    }
                });
                this.iterator.afterPopulate(count.get());
                if (this.mergedResult != null && !this.iterator.isFinished()) {
                    this.updateCollectingProgress((ViewGroup<ItemStack>) this.mergedResult.get(0));
                    return this.mergedResult;
                } else {
                    List<ItemStack> partialResult = this.items.object2IntEntrySet().stream().limit(54L).map(entry -> {
                        ItemCollector.ItemDefinition def = (ItemCollector.ItemDefinition) entry.getKey();
                        return def.toStack(entry.getIntValue());
                    }).toList();
                    List<ViewGroup<ItemStack>> groups = List.of(this.updateCollectingProgress(new ViewGroup<>(partialResult)));
                    if (this.iterator.isFinished()) {
                        this.mergedResult = groups;
                        this.version = currentVersion;
                        this.lastTimeFinished = gameTime;
                        this.items.clear();
                    }
                    return groups;
                }
            }
        }
    }

    protected ViewGroup<ItemStack> updateCollectingProgress(ViewGroup<ItemStack> group) {
        float progress = this.iterator.getCollectingProgress();
        CompoundTag data = group.getExtraData();
        if (Float.isNaN(progress)) {
            progress = 0.0F;
        }
        if (progress >= 1.0F) {
            data.remove("Collecting");
        } else {
            data.putFloat("Collecting", progress);
        }
        return group;
    }

    public static record ItemDefinition(Item item, @Nullable CompoundTag tag) {

        ItemDefinition(ItemStack stack) {
            this(stack.getItem(), stack.getTag());
        }

        public ItemStack toStack(int count) {
            ItemStack stack = new ItemStack(this.item);
            stack.setCount(count);
            stack.setTag(this.tag);
            return stack;
        }
    }
}