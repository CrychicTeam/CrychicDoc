package com.craisinlord.integrated_api.misc.structurepiececounter;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.modinit.IAConditionsRegistry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;

public class StructurePieceCountsManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();

    public static final StructurePieceCountsManager STRUCTURE_PIECE_COUNTS_MANAGER = new StructurePieceCountsManager();

    private Map<ResourceLocation, List<StructurePieceCountsObj>> StructureToPieceCountsObjs = new HashMap();

    private final Map<ResourceLocation, Map<ResourceLocation, StructurePieceCountsManager.RequiredPieceNeeds>> cachedRequirePiecesMap = new HashMap();

    private final Map<ResourceLocation, Map<ResourceLocation, Integer>> cachedMaxCountPiecesMap = new HashMap();

    public StructurePieceCountsManager() {
        super(GSON, "integrated_pieces_spawn_counts");
    }

    @MethodsReturnNonnullByDefault
    private List<StructurePieceCountsObj> getStructurePieceCountsObjs(ResourceLocation fileIdentifier, JsonElement jsonElement) throws Exception {
        List<StructurePieceCountsObj> piecesSpawnCounts = (List<StructurePieceCountsObj>) GSON.fromJson(jsonElement.getAsJsonObject().get("integrated_pieces_spawn_counts"), (new TypeToken<List<StructurePieceCountsObj>>() {
        }).getType());
        for (int i = piecesSpawnCounts.size() - 1; i >= 0; i--) {
            StructurePieceCountsObj entry = (StructurePieceCountsObj) piecesSpawnCounts.get(i);
            if (entry.alwaysSpawnThisMany != null && entry.neverSpawnMoreThanThisMany != null && entry.alwaysSpawnThisMany > entry.neverSpawnMoreThanThisMany) {
                throw new Exception("Integrated API Error: Found " + entry.nbtPieceName + " entry has alwaysSpawnThisMany greater than neverSpawnMoreThanThisMany which is invalid.");
            }
            if (entry.condition != null) {
                Supplier<Boolean> supplier = IAConditionsRegistry.IA_JSON_CONDITIONS_REGISTRY.lookup().get(new ResourceLocation(entry.condition));
                if (supplier != null && !(Boolean) supplier.get()) {
                    piecesSpawnCounts.remove(entry);
                }
            }
        }
        return piecesSpawnCounts;
    }

    protected void apply(Map<ResourceLocation, JsonElement> loader, ResourceManager manager, ProfilerFiller profiler) {
        Map<ResourceLocation, List<StructurePieceCountsObj>> mapBuilder = new HashMap();
        loader.forEach((fileIdentifier, jsonElement) -> {
            try {
                mapBuilder.put(fileIdentifier, this.getStructurePieceCountsObjs(fileIdentifier, jsonElement));
            } catch (Exception var5) {
                IntegratedAPI.LOGGER.error("Integrated API Error: Couldn't parse integrated_pieces_spawn_counts file {} - JSON looks like: {}", fileIdentifier, jsonElement, var5);
            }
        });
        this.StructureToPieceCountsObjs = mapBuilder;
        this.cachedRequirePiecesMap.clear();
        StructurePieceCountsAdditionsMerger.performCountsAdditionsDetectionAndMerger(manager);
    }

    public void parseAndAddCountsJSONObj(ResourceLocation structureRL, List<JsonElement> jsonElements) {
        jsonElements.forEach(jsonElement -> {
            try {
                ((List) this.StructureToPieceCountsObjs.computeIfAbsent(structureRL, rl -> new ArrayList())).addAll(this.getStructurePieceCountsObjs(structureRL, jsonElement));
            } catch (Exception var4) {
                IntegratedAPI.LOGGER.error("Integrated API Error: Couldn't parse integrated_pieces_spawn_counts file {} - JSON looks like: {}", structureRL, jsonElement, var4);
            }
        });
    }

    @Nullable
    public Map<ResourceLocation, StructurePieceCountsManager.RequiredPieceNeeds> getRequirePieces(ResourceLocation structureRL) {
        if (!this.StructureToPieceCountsObjs.containsKey(structureRL)) {
            return null;
        } else if (this.cachedRequirePiecesMap.containsKey(structureRL)) {
            return (Map<ResourceLocation, StructurePieceCountsManager.RequiredPieceNeeds>) this.cachedRequirePiecesMap.get(structureRL);
        } else {
            Map<ResourceLocation, StructurePieceCountsManager.RequiredPieceNeeds> requirePiecesMap = new HashMap();
            List<StructurePieceCountsObj> structurePieceCountsObjs = (List<StructurePieceCountsObj>) this.StructureToPieceCountsObjs.get(structureRL);
            if (structurePieceCountsObjs != null) {
                structurePieceCountsObjs.forEach(entry -> {
                    if (entry.alwaysSpawnThisMany != null) {
                        requirePiecesMap.put(new ResourceLocation(entry.nbtPieceName), new StructurePieceCountsManager.RequiredPieceNeeds(entry.alwaysSpawnThisMany, entry.minimumDistanceFromCenterPiece != null ? entry.minimumDistanceFromCenterPiece : 0));
                    }
                });
            }
            this.cachedRequirePiecesMap.put(structureRL, requirePiecesMap);
            return requirePiecesMap;
        }
    }

    @MethodsReturnNonnullByDefault
    public Map<ResourceLocation, Integer> getMaximumCountForPieces(ResourceLocation structureRL) {
        if (this.cachedMaxCountPiecesMap.containsKey(structureRL)) {
            return (Map<ResourceLocation, Integer>) this.cachedMaxCountPiecesMap.get(structureRL);
        } else {
            Map<ResourceLocation, Integer> maxCountPiecesMap = new HashMap();
            List<StructurePieceCountsObj> structurePieceCountsObjs = (List<StructurePieceCountsObj>) this.StructureToPieceCountsObjs.get(structureRL);
            if (structurePieceCountsObjs != null) {
                structurePieceCountsObjs.forEach(entry -> {
                    if (entry.neverSpawnMoreThanThisMany != null) {
                        maxCountPiecesMap.put(new ResourceLocation(entry.nbtPieceName), entry.neverSpawnMoreThanThisMany);
                    }
                });
            }
            this.cachedMaxCountPiecesMap.put(structureRL, maxCountPiecesMap);
            return maxCountPiecesMap;
        }
    }

    public static record RequiredPieceNeeds(int maxLimit, int minDistanceFromCenter) {

        public int getRequiredAmount() {
            return this.maxLimit;
        }

        public int getMinDistanceFromCenter() {
            return this.minDistanceFromCenter;
        }
    }
}