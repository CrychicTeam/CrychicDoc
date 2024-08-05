package com.almostreliable.summoningrituals.util;

import com.almostreliable.summoningrituals.platform.Platform;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public final class SerializeUtils {

    private SerializeUtils() {
    }

    public static Vec3i vec3FromJson(JsonObject json) {
        int x = GsonHelper.getAsInt(json, "x", 0);
        int y = GsonHelper.getAsInt(json, "y", 0);
        int z = GsonHelper.getAsInt(json, "z", 0);
        return new Vec3i(x, y, z);
    }

    public static JsonObject vec3ToJson(Vec3i vec) {
        JsonObject json = new JsonObject();
        json.addProperty("x", vec.getX());
        json.addProperty("y", vec.getY());
        json.addProperty("z", vec.getZ());
        return json;
    }

    public static Vec3i vec3FromNetwork(FriendlyByteBuf buffer) {
        int x = buffer.readVarInt();
        int y = buffer.readVarInt();
        int z = buffer.readVarInt();
        return new Vec3i(x, y, z);
    }

    public static void vec3ToNetwork(FriendlyByteBuf buffer, Vec3i vec) {
        buffer.writeVarInt(vec.getX());
        buffer.writeVarInt(vec.getY());
        buffer.writeVarInt(vec.getZ());
    }

    public static JsonObject stackToJson(ItemStack stack) {
        if (stack.isEmpty()) {
            throw new IllegalArgumentException("stack is empty");
        } else {
            JsonObject json = new JsonObject();
            json.addProperty("item", Platform.getId(stack.getItem()).toString());
            json.addProperty("count", stack.getCount());
            if (stack.hasTag()) {
                assert stack.getTag() != null;
                json.addProperty("nbt", stack.getTag().toString());
            }
            return json;
        }
    }

    public static Block blockFromId(@Nullable ResourceLocation id) {
        return getFromRegistry(ForgeRegistries.BLOCKS, id);
    }

    public static EntityType<?> mobFromNetwork(FriendlyByteBuf buffer) {
        ResourceLocation id = new ResourceLocation(buffer.readUtf());
        return Platform.mobFromId(id);
    }

    public static Map<String, String> mapFromJson(JsonObject json) {
        return (Map<String, String>) json.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> ((JsonElement) entry.getValue()).getAsString()));
    }

    public static JsonObject mapToJson(Map<String, String> map) {
        JsonObject json = new JsonObject();
        for (Entry<String, String> entry : map.entrySet()) {
            json.addProperty((String) entry.getKey(), (String) entry.getValue());
        }
        return json;
    }

    public static Map<String, String> mapFromNetwork(FriendlyByteBuf buffer) {
        HashMap<String, String> map = new HashMap();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            map.put(buffer.readUtf(), buffer.readUtf());
        }
        return map;
    }

    public static void mapToNetwork(FriendlyByteBuf buffer, Map<String, String> map) {
        buffer.writeVarInt(map.size());
        for (Entry<String, String> entry : map.entrySet()) {
            buffer.writeUtf((String) entry.getKey());
            buffer.writeUtf((String) entry.getValue());
        }
    }

    public static CompoundTag nbtFromString(String nbtString) {
        try {
            return TagParser.parseTag(nbtString);
        } catch (CommandSyntaxException var2) {
            throw new IllegalArgumentException("Invalid NBT string: " + nbtString, var2);
        }
    }

    public static <T> T getFromRegistry(IForgeRegistry<T> registry, @Nullable ResourceLocation id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        } else {
            T value = registry.getValue(id);
            if (value == null) {
                throw new IllegalArgumentException("No value for id: " + id);
            } else {
                return value;
            }
        }
    }
}