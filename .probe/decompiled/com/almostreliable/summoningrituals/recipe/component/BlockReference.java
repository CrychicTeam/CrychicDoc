package com.almostreliable.summoningrituals.recipe.component;

import com.almostreliable.summoningrituals.platform.Platform;
import com.almostreliable.summoningrituals.util.SerializeUtils;
import com.almostreliable.summoningrituals.util.Utils;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public final class BlockReference implements Predicate<BlockState> {

    private final Block block;

    private final Map<String, String> properties;

    private final Map<Integer, Boolean> testCache;

    private BlockState displayState;

    private BlockReference(Block block, Map<String, String> properties) {
        this.block = block;
        this.properties = properties;
        this.testCache = new HashMap();
    }

    public static BlockReference fromJson(JsonObject json) {
        ResourceLocation blockId = new ResourceLocation(GsonHelper.getAsString(json, "block"));
        Block block = SerializeUtils.blockFromId(blockId);
        Map<String, String> properties = new HashMap();
        if (json.has("properties")) {
            properties = SerializeUtils.mapFromJson(json.getAsJsonObject("properties"));
        }
        return new BlockReference(block, properties);
    }

    public static BlockReference fromNetwork(FriendlyByteBuf buffer) {
        ResourceLocation blockId = buffer.readResourceLocation();
        Block block = SerializeUtils.blockFromId(blockId);
        Map<String, String> properties = SerializeUtils.mapFromNetwork(buffer);
        return new BlockReference(block, properties);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("block", Platform.getId(this.block).toString());
        if (!this.properties.isEmpty()) {
            json.add("properties", SerializeUtils.mapToJson(this.properties));
        }
        return json;
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(Platform.getId(this.block));
        SerializeUtils.mapToNetwork(buffer, this.properties);
    }

    public boolean test(BlockState blockState) {
        Boolean cached = (Boolean) this.testCache.get(Block.getId(blockState));
        if (cached != null) {
            return cached;
        } else if (!this.block.equals(blockState.m_60734_())) {
            this.testCache.put(Block.getId(blockState), false);
            return false;
        } else {
            ImmutableMap<Property<?>, Comparable<?>> toCompareProps = blockState.m_61148_();
            for (Entry<String, String> prop : this.properties.entrySet()) {
                if (toCompareProps.entrySet().stream().noneMatch(entry -> ((Property) entry.getKey()).getName().equalsIgnoreCase((String) prop.getKey()) && ((Comparable) entry.getValue()).toString().equalsIgnoreCase((String) prop.getValue()))) {
                    this.testCache.put(Block.getId(blockState), false);
                    return false;
                }
            }
            this.testCache.put(Block.getId(blockState), true);
            return true;
        }
    }

    public BlockState getDisplayState() {
        if (this.displayState != null) {
            return this.displayState;
        } else {
            AtomicReference<BlockState> newState = new AtomicReference(this.block.defaultBlockState());
            for (Property<?> property : ((BlockState) newState.get()).m_61147_()) {
                Object newValue = this.properties.get(property.getName());
                if (newValue != null) {
                    try {
                        newState.set((BlockState) ((BlockState) newState.get()).m_61124_(property, Utils.cast(newValue)));
                    } catch (Exception var6) {
                        property.getValue(newValue.toString()).ifPresent(v -> newState.set((BlockState) ((BlockState) newState.get()).m_61124_(property, Utils.cast(v))));
                    }
                }
            }
            this.displayState = (BlockState) newState.get();
            return this.displayState;
        }
    }
}