package net.mehvahdjukaar.moonlight.api.fluids;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.misc.StrOpt;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class FluidContainerList {

    private final Map<Item, FluidContainerList.Category> emptyToFilledMap = new IdentityHashMap();

    public FluidContainerList(List<FluidContainerList.Category> categoryList) {
        categoryList.forEach(this::addCategory);
    }

    public FluidContainerList() {
    }

    private void addCategory(FluidContainerList.Category newCategory) {
        if (!newCategory.isEmpty()) {
            if (this.emptyToFilledMap.containsKey(newCategory.emptyContainer)) {
                FluidContainerList.Category c = (FluidContainerList.Category) this.emptyToFilledMap.get(newCategory.emptyContainer);
                if (c.containerCapacity == newCategory.containerCapacity) {
                    c.filled.addAll(newCategory.filled);
                }
            } else {
                this.emptyToFilledMap.put(newCategory.emptyContainer, newCategory);
            }
        }
    }

    public Optional<Item> getEmpty(Item filledContainer) {
        for (Entry<Item, FluidContainerList.Category> e : this.emptyToFilledMap.entrySet()) {
            if (((FluidContainerList.Category) e.getValue()).getFilledItems().contains(filledContainer)) {
                return Optional.of((Item) e.getKey());
            }
        }
        return Optional.empty();
    }

    public Optional<Item> getFilled(Item emptyContainer) {
        FluidContainerList.Category c = (FluidContainerList.Category) this.emptyToFilledMap.get(emptyContainer);
        return c != null ? c.getFirstFilled() : Optional.empty();
    }

    public Optional<FluidContainerList.Category> getCategoryFromEmpty(Item emptyContainer) {
        return Optional.ofNullable((FluidContainerList.Category) this.emptyToFilledMap.get(emptyContainer));
    }

    public Optional<FluidContainerList.Category> getCategoryFromFilled(Item filledContainer) {
        return this.getEmpty(filledContainer).map(this.emptyToFilledMap::get);
    }

    protected Optional<List<FluidContainerList.Category>> encodeList() {
        return this.emptyToFilledMap.isEmpty() ? Optional.empty() : Optional.of(new ArrayList(this.emptyToFilledMap.values()));
    }

    public Collection<Item> getPossibleFilled() {
        List<Item> list = new ArrayList();
        this.emptyToFilledMap.values().forEach(c -> list.addAll(c.filled));
        return list;
    }

    public Collection<Item> getPossibleEmpty() {
        return this.emptyToFilledMap.keySet();
    }

    public Collection<FluidContainerList.Category> getCategories() {
        return this.emptyToFilledMap.values();
    }

    protected void merge(FluidContainerList other) {
        other.emptyToFilledMap.values().forEach(this::addCategory);
    }

    protected void add(Item empty, Item filled, int amount) {
        FluidContainerList.Category c = (FluidContainerList.Category) this.emptyToFilledMap.computeIfAbsent(empty, i -> new FluidContainerList.Category(i, amount));
        c.addItem(filled);
    }

    protected void add(Item empty, Item filled, int amount, SoundEvent fillSound, SoundEvent emptySound) {
        FluidContainerList.Category c = (FluidContainerList.Category) this.emptyToFilledMap.computeIfAbsent(empty, i -> new FluidContainerList.Category(i, amount));
        c.addItem(filled);
        c.fillSound = fillSound;
        c.emptySound = emptySound;
    }

    private static <T> Function<FluidContainerList.Category, Optional<T>> getHackyOptional(Function<FluidContainerList.Category, T> getter) {
        return f -> {
            T value = (T) getter.apply(f);
            T def = (T) getter.apply((FluidContainerList.Category) FluidContainerList.Category.EMPTY.get());
            return value.equals(def) ? Optional.empty() : Optional.of(value);
        };
    }

    public static class Category {

        private static final Supplier<FluidContainerList.Category> EMPTY = Suppliers.memoize(() -> new FluidContainerList.Category(BuiltInRegistries.ITEM.get(BuiltInRegistries.ITEM.getDefaultKey()), 1));

        public static final Codec<FluidContainerList.Category> CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceLocation.CODEC.fieldOf("empty").forGetter(c -> Utils.getID(c.emptyContainer)), SoftFluid.Capacity.INT_CODEC.fieldOf("capacity").forGetter(FluidContainerList.Category::getCapacity), ResourceLocation.CODEC.listOf().fieldOf("filled").forGetter(c -> c.filled.stream().map(Utils::getID).toList()), StrOpt.of(BuiltInRegistries.SOUND_EVENT.byNameCodec(), "fill_sound").forGetter(FluidContainerList.getHackyOptional(FluidContainerList.Category::getFillSound)), StrOpt.of(BuiltInRegistries.SOUND_EVENT.byNameCodec(), "empty_sound").forGetter(FluidContainerList.getHackyOptional(FluidContainerList.Category::getEmptySound))).apply(instance, FluidContainerList.Category::decode));

        private final Item emptyContainer;

        private final int containerCapacity;

        private SoundEvent fillSound;

        private SoundEvent emptySound;

        private final List<Item> filled = new ArrayList();

        private Category(Item emptyContainer, int capacity, @Nullable SoundEvent fillSound, @Nullable SoundEvent emptySound) {
            this.emptyContainer = emptyContainer;
            this.containerCapacity = capacity;
            this.fillSound = fillSound == null ? SoundEvents.BOTTLE_FILL : fillSound;
            this.emptySound = emptySound == null ? SoundEvents.BOTTLE_EMPTY : emptySound;
        }

        private Category(Item emptyContainer, int capacity) {
            this(emptyContainer, capacity, null, null);
        }

        private static FluidContainerList.Category decode(ResourceLocation empty, int capacity, List<ResourceLocation> filled) {
            return decode(empty, capacity, filled, Optional.empty(), Optional.empty());
        }

        private static FluidContainerList.Category decode(ResourceLocation empty, int capacity, List<ResourceLocation> filled, Optional<SoundEvent> fillSound, Optional<SoundEvent> emptySound) {
            Optional<Item> opt = BuiltInRegistries.ITEM.m_6612_(empty);
            if (opt.isEmpty()) {
                return (FluidContainerList.Category) EMPTY.get();
            } else {
                FluidContainerList.Category category = new FluidContainerList.Category((Item) opt.get(), capacity, (SoundEvent) fillSound.orElse(null), (SoundEvent) emptySound.orElse(null));
                filled.forEach(f -> {
                    Optional<Item> opt2 = BuiltInRegistries.ITEM.m_6612_(f);
                    opt2.ifPresent(category::addItem);
                });
                return category.isEmpty() ? (FluidContainerList.Category) EMPTY.get() : category;
            }
        }

        public Item getEmptyContainer() {
            return this.emptyContainer;
        }

        public int getCapacity() {
            return this.containerCapacity;
        }

        private void addItem(Item i) {
            if (!i.getDefaultInstance().isEmpty() && !this.filled.contains(i)) {
                this.filled.add(i);
            }
        }

        public int getAmount() {
            return this.containerCapacity;
        }

        public SoundEvent getFillSound() {
            return this.fillSound;
        }

        public SoundEvent getEmptySound() {
            return this.emptySound;
        }

        public List<Item> getFilledItems() {
            return this.filled;
        }

        public boolean isEmpty() {
            return this.filled.isEmpty();
        }

        public Optional<Item> getFirstFilled() {
            return this.filled.stream().findFirst();
        }
    }
}