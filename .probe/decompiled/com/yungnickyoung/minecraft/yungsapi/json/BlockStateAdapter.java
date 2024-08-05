package com.yungnickyoung.minecraft.yungsapi.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.yungnickyoung.minecraft.yungsapi.YungsApiCommon;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockStateAdapter extends TypeAdapter<BlockState> {

    public BlockState read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        } else {
            return resolveBlockState(reader.nextString());
        }
    }

    public void write(JsonWriter writer, BlockState blockState) throws IOException {
        if (blockState == null) {
            writer.nullValue();
        } else {
            String blockString = String.valueOf(blockState);
            if (blockString.startsWith("Block")) {
                blockString = blockString.replace("Block", "");
            }
            blockString = blockString.replace("{", "");
            blockString = blockString.replace("}", "");
            writer.value(blockString);
        }
    }

    public static BlockState resolveBlockState(String fullString) {
        Map<String, String> properties = new HashMap();
        String blockString = fullString;
        int startIndex = fullString.indexOf(91);
        int stopIndex = fullString.indexOf(93);
        if (startIndex != -1) {
            blockString = fullString.substring(0, startIndex);
            if (stopIndex < startIndex) {
                YungsApiCommon.LOGGER.error("JSON: Malformed property {}. Missing a bracket?", fullString);
                YungsApiCommon.LOGGER.error("Using air instead...");
                return Blocks.AIR.defaultBlockState();
            }
            int index = startIndex + 1;
            String currKey = "";
            for (StringBuilder currString = new StringBuilder(); index <= stopIndex; index++) {
                char currChar = fullString.charAt(index);
                if (currChar == '=') {
                    currKey = currString.toString();
                    currString = new StringBuilder();
                } else if (currChar != ',' && currChar != ']') {
                    currString.append(fullString.charAt(index));
                } else {
                    properties.put(currKey, currString.toString());
                    currString = new StringBuilder();
                }
            }
        }
        BlockState blockState;
        try {
            blockState = BuiltInRegistries.BLOCK.get(new ResourceLocation(blockString)).defaultBlockState();
        } catch (Exception var10) {
            YungsApiCommon.LOGGER.error("JSON: Unable to read block '{}': {}", blockString, var10.toString());
            YungsApiCommon.LOGGER.error("Using air instead...");
            return Blocks.AIR.defaultBlockState();
        }
        if (properties.size() > 0) {
            blockState = getConfiguredBlockState(blockState, properties);
        }
        return blockState;
    }

    private static <T extends Comparable<T>> BlockState getConfiguredBlockState(BlockState blockState, Map<String, String> properties) {
        Block block = blockState.m_60734_();
        for (Entry<String, String> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            boolean found = false;
            for (Property<?> p : blockState.m_61147_()) {
                if (p.getName().equals(key)) {
                    T val = (T) p.getValue(value).orElse(null);
                    if (val != null) {
                        blockState = (BlockState) blockState.m_61124_(p, val);
                        found = true;
                        break;
                    }
                    YungsApiCommon.LOGGER.error("JSON: Found null for property {} for block {}", p, BuiltInRegistries.BLOCK.m_7447_(block));
                }
            }
            if (!found) {
                YungsApiCommon.LOGGER.error("JSON: Unable to find property {} for block {}", key, BuiltInRegistries.BLOCK.m_7447_(block));
            }
        }
        return blockState;
    }
}