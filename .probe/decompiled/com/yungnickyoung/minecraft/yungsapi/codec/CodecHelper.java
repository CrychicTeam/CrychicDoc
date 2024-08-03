package com.yungnickyoung.minecraft.yungsapi.codec;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.yungsapi.YungsApiCommon;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

public class CodecHelper {

    public static Codec<BlockState> BLOCKSTATE_STRING_CODEC = Codec.STRING.xmap(CodecHelper::blockStateFromString, StateHolder::toString);

    public static BlockState blockStateFromString(String blockStateString) {
        Map<String, String> properties = new HashMap();
        String blockString = blockStateString;
        int startIndex = blockStateString.indexOf(91);
        int stopIndex = blockStateString.indexOf(93);
        if (startIndex != -1) {
            blockString = blockStateString.substring(0, startIndex);
            if (stopIndex < startIndex) {
                YungsApiCommon.LOGGER.error("JSON: Malformed property {}. Missing a bracket?", blockStateString);
                YungsApiCommon.LOGGER.error("Using air instead...");
                return Blocks.AIR.defaultBlockState();
            }
            int index = startIndex + 1;
            String currKey = "";
            for (StringBuilder currString = new StringBuilder(); index <= stopIndex; index++) {
                char currChar = blockStateString.charAt(index);
                if (currChar == '=') {
                    currKey = currString.toString();
                    currString = new StringBuilder();
                } else if (currChar != ',' && currChar != ']') {
                    currString.append(blockStateString.charAt(index));
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