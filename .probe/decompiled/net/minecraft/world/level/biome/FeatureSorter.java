package net.minecraft.world.level.biome;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList.Builder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.Graph;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.apache.commons.lang3.mutable.MutableInt;

public class FeatureSorter {

    public static <T> List<FeatureSorter.StepFeatureData> buildFeaturesPerStep(List<T> listT0, Function<T, List<HolderSet<PlacedFeature>>> functionTListHolderSetPlacedFeature1, boolean boolean2) {
        Object2IntMap<PlacedFeature> $$3 = new Object2IntOpenHashMap();
        MutableInt $$4 = new MutableInt(0);
        record FeatureData(int f_220610_, int f_220611_, PlacedFeature f_220612_) {

            private final int featureIndex;

            private final int step;

            private final PlacedFeature feature;

            FeatureData(int f_220610_, int f_220611_, PlacedFeature f_220612_) {
                this.featureIndex = f_220610_;
                this.step = f_220611_;
                this.feature = f_220612_;
            }
        }
        Comparator<FeatureData> $$5 = Comparator.comparingInt(FeatureData::f_220611_).thenComparingInt(FeatureData::f_220610_);
        Map<FeatureData, Set<FeatureData>> $$6 = new TreeMap($$5);
        int $$7 = 0;
        for (T $$8 : listT0) {
            List<FeatureData> $$9 = Lists.newArrayList();
            List<HolderSet<PlacedFeature>> $$10 = (List<HolderSet<PlacedFeature>>) functionTListHolderSetPlacedFeature1.apply($$8);
            $$7 = Math.max($$7, $$10.size());
            for (int $$11 = 0; $$11 < $$10.size(); $$11++) {
                for (Holder<PlacedFeature> $$12 : (HolderSet) $$10.get($$11)) {
                    PlacedFeature $$13 = $$12.value();
                    $$9.add(new FeatureData($$3.computeIfAbsent($$13, p_220609_ -> $$4.getAndIncrement()), $$11, $$13));
                }
            }
            for (int $$14 = 0; $$14 < $$9.size(); $$14++) {
                Set<FeatureData> $$15 = (Set<FeatureData>) $$6.computeIfAbsent((FeatureData) $$9.get($$14), p_220602_ -> new TreeSet($$5));
                if ($$14 < $$9.size() - 1) {
                    $$15.add((FeatureData) $$9.get($$14 + 1));
                }
            }
        }
        Set<FeatureData> $$16 = new TreeSet($$5);
        Set<FeatureData> $$17 = new TreeSet($$5);
        List<FeatureData> $$18 = Lists.newArrayList();
        for (FeatureData $$19 : $$6.keySet()) {
            if (!$$17.isEmpty()) {
                throw new IllegalStateException("You somehow broke the universe; DFS bork (iteration finished with non-empty in-progress vertex set");
            }
            if (!$$16.contains($$19) && Graph.depthFirstSearch($$6, $$16, $$17, $$18::add, $$19)) {
                if (!boolean2) {
                    throw new IllegalStateException("Feature order cycle found");
                }
                List<T> $$20 = new ArrayList(listT0);
                int $$21;
                do {
                    $$21 = $$20.size();
                    ListIterator<T> $$22 = $$20.listIterator();
                    while ($$22.hasNext()) {
                        T $$23 = (T) $$22.next();
                        $$22.remove();
                        try {
                            buildFeaturesPerStep($$20, functionTListHolderSetPlacedFeature1, false);
                        } catch (IllegalStateException var18) {
                            continue;
                        }
                        $$22.add($$23);
                    }
                } while ($$21 != $$20.size());
                throw new IllegalStateException("Feature order cycle found, involved sources: " + $$20);
            }
        }
        Collections.reverse($$18);
        Builder<FeatureSorter.StepFeatureData> $$25 = ImmutableList.builder();
        for (int $$26 = 0; $$26 < $$7; $$26++) {
            int $$27 = $$26;
            List<PlacedFeature> $$28 = (List<PlacedFeature>) $$18.stream().filter(p_220599_ -> p_220599_.step() == $$27).map(FeatureData::f_220612_).collect(Collectors.toList());
            $$25.add(new FeatureSorter.StepFeatureData($$28));
        }
        return $$25.build();
    }

    public static record StepFeatureData(List<PlacedFeature> f_220624_, ToIntFunction<PlacedFeature> f_220625_) {

        private final List<PlacedFeature> features;

        private final ToIntFunction<PlacedFeature> indexMapping;

        StepFeatureData(List<PlacedFeature> p_220627_) {
            this(p_220627_, Util.createIndexLookup(p_220627_, p_220633_ -> new Object2IntOpenCustomHashMap(p_220633_, Util.identityStrategy())));
        }

        public StepFeatureData(List<PlacedFeature> f_220624_, ToIntFunction<PlacedFeature> f_220625_) {
            this.features = f_220624_;
            this.indexMapping = f_220625_;
        }
    }
}