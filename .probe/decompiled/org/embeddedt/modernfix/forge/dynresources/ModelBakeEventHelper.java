package org.embeddedt.modernfix.forge.dynresources;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo.ModVersion;
import net.minecraftforge.registries.ForgeRegistries;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.util.ForwardingInclDefaultsMap;
import org.jetbrains.annotations.Nullable;

public class ModelBakeEventHelper {

    private static final Set<String> INCOMPATIBLE_MODS = ImmutableSet.of("industrialforegoing", "mekanism", "vampirism", "elevatorid", "embers");

    private final Map<ResourceLocation, BakedModel> modelRegistry;

    private final Set<ResourceLocation> topLevelModelLocations;

    private final MutableGraph<String> dependencyGraph;

    private static final Set<String> WARNED_MOD_IDS = new HashSet();

    public ModelBakeEventHelper(Map<ResourceLocation, BakedModel> modelRegistry) {
        this.modelRegistry = modelRegistry;
        this.topLevelModelLocations = new HashSet(modelRegistry.keySet());
        ForgeRegistries.BLOCKS.getEntries().forEach(entry -> {
            ResourceLocation location = ((ResourceKey) entry.getKey()).location();
            UnmodifiableIterator var3x = ((Block) entry.getValue()).getStateDefinition().getPossibleStates().iterator();
            while (var3x.hasNext()) {
                BlockState state = (BlockState) var3x.next();
                this.topLevelModelLocations.add(BlockModelShaper.stateToModelLocation(location, state));
            }
        });
        ForgeRegistries.ITEMS.getKeys().forEach(key -> this.topLevelModelLocations.add(new ModelResourceLocation(key, "inventory")));
        this.dependencyGraph = GraphBuilder.undirected().build();
        ModList.get().forEachModContainer((idx, mc) -> {
            this.dependencyGraph.addNode(idx);
            for (ModVersion versionx : mc.getModInfo().getDependencies()) {
                this.dependencyGraph.addNode(versionx.getModId());
            }
        });
        for (String id : this.dependencyGraph.nodes()) {
            Optional<? extends ModContainer> mContainer = ModList.get().getModContainerById(id);
            if (mContainer.isPresent()) {
                for (ModVersion version : ((ModContainer) mContainer.get()).getModInfo().getDependencies()) {
                    if (!Objects.equals(id, version.getModId())) {
                        this.dependencyGraph.putEdge(id, version.getModId());
                    }
                }
            }
        }
    }

    private Map<ResourceLocation, BakedModel> createWarningRegistry(String modId) {
        return new ForwardingInclDefaultsMap<ResourceLocation, BakedModel>() {

            protected Map<ResourceLocation, BakedModel> delegate() {
                return ModelBakeEventHelper.this.modelRegistry;
            }

            private void logWarning() {
                if (ModelBakeEventHelper.WARNED_MOD_IDS.add(modId)) {
                    ModernFix.LOGGER.warn("Mod '{}' is accessing Map#keySet/entrySet/values/replaceAll on the model registry map inside its event handler. This probably won't work as expected with dynamic resources on. Prefer using Map#get/put and constructing ModelResourceLocations another way.", modId);
                }
            }

            public Set<ResourceLocation> keySet() {
                this.logWarning();
                return super.keySet();
            }

            public Set<Entry<ResourceLocation, BakedModel>> entrySet() {
                this.logWarning();
                return super.entrySet();
            }

            public Collection<BakedModel> values() {
                this.logWarning();
                return super.values();
            }

            @Override
            public void replaceAll(BiFunction<? super ResourceLocation, ? super BakedModel, ? extends BakedModel> function) {
                this.logWarning();
                super.replaceAll(function);
            }
        };
    }

    public Map<ResourceLocation, BakedModel> wrapRegistry(String modId) {
        final Set<String> modIdsToInclude = new HashSet();
        modIdsToInclude.add(modId);
        try {
            modIdsToInclude.addAll(this.dependencyGraph.adjacentNodes(modId));
        } catch (IllegalArgumentException var5) {
        }
        modIdsToInclude.remove("minecraft");
        if (modIdsToInclude.stream().noneMatch(INCOMPATIBLE_MODS::contains)) {
            return this.createWarningRegistry(modId);
        } else {
            final Set<ResourceLocation> ourModelLocations = Sets.filter(this.topLevelModelLocations, loc -> modIdsToInclude.contains(loc.getNamespace()));
            final BakedModel missingModel = (BakedModel) this.modelRegistry.get(ModelBakery.MISSING_MODEL_LOCATION);
            return new ForwardingMap<ResourceLocation, BakedModel>() {

                protected Map<ResourceLocation, BakedModel> delegate() {
                    return ModelBakeEventHelper.this.modelRegistry;
                }

                public BakedModel get(@Nullable Object key) {
                    BakedModel model = (BakedModel) super.get(key);
                    if (model == null && key != null && modIdsToInclude.contains(((ResourceLocation) key).getNamespace())) {
                        ModernFix.LOGGER.warn("Model {} is missing, but was requested in model bake event. Returning missing model", key);
                        return missingModel;
                    } else {
                        return model;
                    }
                }

                public Set<ResourceLocation> keySet() {
                    return ourModelLocations;
                }

                public boolean containsKey(@Nullable Object key) {
                    return ourModelLocations.contains(key) || super.containsKey(key);
                }

                public void replaceAll(BiFunction<? super ResourceLocation, ? super BakedModel, ? extends BakedModel> function) {
                    ModernFix.LOGGER.warn("Mod '{}' is calling replaceAll on the model registry. Some hacks will be used to keep this fast, but they may not be 100% compatible.", modId);
                    for (ResourceLocation location : new ArrayList(this.keySet())) {
                        boolean needsReplacement;
                        try {
                            needsReplacement = function.apply(location, null) != null;
                        } catch (Throwable var8) {
                            needsReplacement = true;
                        }
                        if (needsReplacement) {
                            BakedModel existing = this.get(location);
                            BakedModel replacement = (BakedModel) function.apply(location, existing);
                            if (replacement != existing) {
                                this.put(location, replacement);
                            }
                        }
                    }
                }
            };
        }
    }
}