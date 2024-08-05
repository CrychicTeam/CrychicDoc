package io.github.lightman314.lightmanscurrency.client.resourcepacks.data.item_trader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemPositionBlockManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static final ItemPositionBlockManager INSTANCE = new ItemPositionBlockManager();

    private final Map<ResourceLocation, List<ResourceLocation>> data = new HashMap();

    private ItemPositionBlockManager() {
        super(GSON, "lightmanscurrency/item_position_blocks");
    }

    @Nullable
    public static ResourceLocation getResourceForBlock(@Nonnull BlockState state) {
        return getResourceForBlock(state.m_60734_());
    }

    @Nullable
    public static ResourceLocation getResourceForBlock(@Nonnull Block block) {
        ResourceLocation blockID = ForgeRegistries.BLOCKS.getKey(block);
        for (Entry<ResourceLocation, List<ResourceLocation>> d : INSTANCE.data.entrySet()) {
            if (((List) d.getValue()).contains(blockID)) {
                return (ResourceLocation) d.getKey();
            }
        }
        return null;
    }

    @Nonnull
    public static ItemPositionData getDataForBlock(@Nonnull BlockState state) {
        return getDataForBlock(state.m_60734_());
    }

    @Nonnull
    public static ItemPositionData getDataForBlock(@Nonnull Block block) {
        ResourceLocation dataID = getResourceForBlock(block);
        return dataID != null ? ItemPositionManager.getDataOrEmpty(dataID) : ItemPositionData.EMPTY;
    }

    protected void apply(@Nonnull Map<ResourceLocation, JsonElement> map, @Nonnull ResourceManager resourceManager, @Nonnull ProfilerFiller filler) {
        this.data.clear();
        map.forEach((id, json) -> {
            try {
                JsonObject root = GsonHelper.convertToJsonObject(json, "top element");
                JsonArray valueList = GsonHelper.getAsJsonArray(root, "values");
                List<ResourceLocation> results = new ArrayList();
                for (int i = 0; i < valueList.size(); i++) {
                    ResourceLocation rl = new ResourceLocation(GsonHelper.convertToString(valueList.get(i), "values[" + i + "]"));
                    if (rl != null) {
                        results.add(rl);
                    }
                }
                this.data.put(id, results);
            } catch (IllegalArgumentException | ResourceLocationException | JsonSyntaxException var8) {
                LightmansCurrency.LogError("Parsing error loading item position data " + id, var8);
            }
        });
        LightmansCurrency.LogDebug("Loaded " + this.data.size() + " Item Position Block entries!");
    }
}