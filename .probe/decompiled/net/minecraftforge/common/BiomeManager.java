package net.minecraftforge.common;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public class BiomeManager {

    private static BiomeManager.TrackedList<BiomeManager.BiomeEntry>[] biomes = setupBiomes();

    private static final List<ResourceKey<Biome>> additionalOverworldBiomes = new ArrayList();

    private static final List<ResourceKey<Biome>> additionalOverworldBiomesView = Collections.unmodifiableList(additionalOverworldBiomes);

    private static BiomeManager.TrackedList<BiomeManager.BiomeEntry>[] setupBiomes() {
        BiomeManager.TrackedList<BiomeManager.BiomeEntry>[] currentBiomes = new BiomeManager.TrackedList[BiomeManager.BiomeType.values().length];
        currentBiomes[BiomeManager.BiomeType.DESERT_LEGACY.ordinal()] = new BiomeManager.TrackedList<>(new BiomeManager.BiomeEntry(Biomes.DESERT, 10), new BiomeManager.BiomeEntry(Biomes.FOREST, 10), new BiomeManager.BiomeEntry(Biomes.SWAMP, 10), new BiomeManager.BiomeEntry(Biomes.PLAINS, 10), new BiomeManager.BiomeEntry(Biomes.TAIGA, 10));
        currentBiomes[BiomeManager.BiomeType.DESERT.ordinal()] = new BiomeManager.TrackedList<>(new BiomeManager.BiomeEntry(Biomes.DESERT, 30), new BiomeManager.BiomeEntry(Biomes.SAVANNA, 20), new BiomeManager.BiomeEntry(Biomes.PLAINS, 10));
        currentBiomes[BiomeManager.BiomeType.WARM.ordinal()] = new BiomeManager.TrackedList<>(new BiomeManager.BiomeEntry(Biomes.FOREST, 10), new BiomeManager.BiomeEntry(Biomes.DARK_FOREST, 10), new BiomeManager.BiomeEntry(Biomes.PLAINS, 10), new BiomeManager.BiomeEntry(Biomes.BIRCH_FOREST, 10), new BiomeManager.BiomeEntry(Biomes.SWAMP, 10));
        currentBiomes[BiomeManager.BiomeType.COOL.ordinal()] = new BiomeManager.TrackedList<>(new BiomeManager.BiomeEntry(Biomes.FOREST, 10), new BiomeManager.BiomeEntry(Biomes.TAIGA, 10), new BiomeManager.BiomeEntry(Biomes.PLAINS, 10));
        currentBiomes[BiomeManager.BiomeType.ICY.ordinal()] = new BiomeManager.TrackedList<>(new BiomeManager.BiomeEntry(Biomes.SNOWY_TAIGA, 10));
        return currentBiomes;
    }

    public static void addAdditionalOverworldBiomes(ResourceKey<Biome> biome) {
        if (!"minecraft".equals(biome.location().getNamespace()) && additionalOverworldBiomes.stream().noneMatch(entry -> entry.location().equals(biome.location()))) {
            additionalOverworldBiomes.add(biome);
        }
    }

    public static boolean addBiome(BiomeManager.BiomeType type, BiomeManager.BiomeEntry entry) {
        int idx = type.ordinal();
        List<BiomeManager.BiomeEntry> list = idx > biomes.length ? null : biomes[idx];
        if (list != null) {
            additionalOverworldBiomes.add(entry.key);
            return list.add(entry);
        } else {
            return false;
        }
    }

    public static boolean removeBiome(BiomeManager.BiomeType type, BiomeManager.BiomeEntry entry) {
        int idx = type.ordinal();
        List<BiomeManager.BiomeEntry> list = idx > biomes.length ? null : biomes[idx];
        return list == null ? false : list.remove(entry);
    }

    public static List<ResourceKey<Biome>> getAdditionalOverworldBiomes() {
        return additionalOverworldBiomesView;
    }

    public static ImmutableList<BiomeManager.BiomeEntry> getBiomes(BiomeManager.BiomeType type) {
        int idx = type.ordinal();
        List<BiomeManager.BiomeEntry> list = idx >= biomes.length ? null : biomes[idx];
        return list != null ? ImmutableList.copyOf(list) : ImmutableList.of();
    }

    public static boolean isTypeListModded(BiomeManager.BiomeType type) {
        int idx = type.ordinal();
        BiomeManager.TrackedList<BiomeManager.BiomeEntry> list = idx > biomes.length ? null : biomes[idx];
        return list == null ? false : list.isModded();
    }

    public static class BiomeEntry {

        private final ResourceKey<Biome> key;

        public BiomeEntry(ResourceKey<Biome> key, int weight) {
            this.key = key;
        }

        public ResourceKey<Biome> getKey() {
            return this.key;
        }
    }

    public static enum BiomeType {

        DESERT, DESERT_LEGACY, WARM, COOL, ICY
    }

    private static class TrackedList<E> extends ArrayList<E> {

        private static final long serialVersionUID = 1L;

        private boolean isModded = false;

        @SafeVarargs
        private <T extends E> TrackedList(T... c) {
            super(Arrays.asList(c));
        }

        public E set(int index, E element) {
            this.isModded = true;
            return (E) super.set(index, element);
        }

        public boolean add(E e) {
            this.isModded = true;
            return super.add(e);
        }

        public void add(int index, E element) {
            this.isModded = true;
            super.add(index, element);
        }

        public E remove(int index) {
            this.isModded = true;
            return (E) super.remove(index);
        }

        public boolean remove(Object o) {
            this.isModded = true;
            return super.remove(o);
        }

        public void clear() {
            this.isModded = true;
            super.clear();
        }

        public boolean addAll(Collection<? extends E> c) {
            this.isModded = true;
            return super.addAll(c);
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            this.isModded = true;
            return super.addAll(index, c);
        }

        public boolean removeAll(Collection<?> c) {
            this.isModded = true;
            return super.removeAll(c);
        }

        public boolean retainAll(Collection<?> c) {
            this.isModded = true;
            return super.retainAll(c);
        }

        public boolean isModded() {
            return this.isModded;
        }
    }
}