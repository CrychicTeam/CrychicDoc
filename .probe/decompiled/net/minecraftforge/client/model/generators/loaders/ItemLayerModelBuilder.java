package net.minecraftforge.client.model.generators.loaders;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.ForgeFaceData;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemLayerModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

    private final Int2ObjectMap<ForgeFaceData> faceData = new Int2ObjectOpenHashMap();

    private final Map<ResourceLocation, IntSet> renderTypes = new LinkedHashMap();

    private final IntSet layersWithRenderTypes = new IntOpenHashSet();

    public static <T extends ModelBuilder<T>> ItemLayerModelBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper) {
        return new ItemLayerModelBuilder<>(parent, existingFileHelper);
    }

    protected ItemLayerModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(new ResourceLocation("forge:item_layers"), parent, existingFileHelper);
    }

    public ItemLayerModelBuilder<T> emissive(int blockLight, int skyLight, int... layers) {
        Preconditions.checkNotNull(layers, "Layers must not be null");
        Preconditions.checkArgument(layers.length > 0, "At least one layer must be specified");
        Preconditions.checkArgument(Arrays.stream(layers).allMatch(ix -> ix >= 0), "All layers must be >= 0");
        for (int i : layers) {
            this.faceData.compute(i, (key, value) -> {
                ForgeFaceData fallback = value == null ? ForgeFaceData.DEFAULT : value;
                return new ForgeFaceData(fallback.color(), blockLight, skyLight, fallback.ambientOcclusion(), fallback.calculateNormals());
            });
        }
        return this;
    }

    public ItemLayerModelBuilder<T> color(int color, int... layers) {
        Preconditions.checkNotNull(layers, "Layers must not be null");
        Preconditions.checkArgument(layers.length > 0, "At least one layer must be specified");
        Preconditions.checkArgument(Arrays.stream(layers).allMatch(ix -> ix >= 0), "All layers must be >= 0");
        for (int i : layers) {
            this.faceData.compute(i, (key, value) -> {
                ForgeFaceData fallback = value == null ? ForgeFaceData.DEFAULT : value;
                return new ForgeFaceData(color, fallback.blockLight(), fallback.skyLight(), fallback.ambientOcclusion(), fallback.calculateNormals());
            });
        }
        return this;
    }

    public ItemLayerModelBuilder<T> renderType(String renderType, int... layers) {
        Preconditions.checkNotNull(renderType, "Render type must not be null");
        ResourceLocation asLoc;
        if (renderType.contains(":")) {
            asLoc = new ResourceLocation(renderType);
        } else {
            asLoc = new ResourceLocation(this.parent.getLocation().getNamespace(), renderType);
        }
        return this.renderType(asLoc, layers);
    }

    public ItemLayerModelBuilder<T> renderType(ResourceLocation renderType, int... layers) {
        Preconditions.checkNotNull(renderType, "Render type must not be null");
        Preconditions.checkNotNull(layers, "Layers must not be null");
        Preconditions.checkArgument(layers.length > 0, "At least one layer must be specified");
        Preconditions.checkArgument(Arrays.stream(layers).allMatch(i -> i >= 0), "All layers must be >= 0");
        int[] alreadyAssigned = Arrays.stream(layers).filter(this.layersWithRenderTypes::contains).toArray();
        Preconditions.checkArgument(alreadyAssigned.length == 0, "Attempted to re-assign layer render types: " + Arrays.toString(alreadyAssigned));
        IntSet renderTypeLayers = (IntSet) this.renderTypes.computeIfAbsent(renderType, $ -> new IntOpenHashSet());
        Arrays.stream(layers).forEach(layer -> {
            renderTypeLayers.add(layer);
            this.layersWithRenderTypes.add(layer);
        });
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        json = super.toJson(json);
        JsonObject forgeData = new JsonObject();
        JsonObject layerObj = new JsonObject();
        ObjectIterator renderTypes = this.faceData.int2ObjectEntrySet().iterator();
        while (renderTypes.hasNext()) {
            Entry<ForgeFaceData> entry = (Entry<ForgeFaceData>) renderTypes.next();
            layerObj.add(String.valueOf(entry.getIntKey()), (JsonElement) ForgeFaceData.CODEC.encodeStart(JsonOps.INSTANCE, (ForgeFaceData) entry.getValue()).getOrThrow(false, s -> {
            }));
        }
        forgeData.add("layers", layerObj);
        json.add("forge_data", forgeData);
        JsonObject renderTypesx = new JsonObject();
        this.renderTypes.forEach((renderType, layers) -> {
            JsonArray array = new JsonArray();
            layers.intStream().sorted().forEach(array::add);
            renderTypes.add(renderType.toString(), array);
        });
        json.add("render_types", renderTypesx);
        return json;
    }
}