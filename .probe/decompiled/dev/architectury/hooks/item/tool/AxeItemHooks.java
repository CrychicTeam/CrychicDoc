package dev.architectury.hooks.item.tool;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;

public final class AxeItemHooks {

    private AxeItemHooks() {
    }

    public static void addStrippable(Block input, Block result) {
        if (!input.defaultBlockState().m_61138_(RotatedPillarBlock.AXIS)) {
            throw new IllegalArgumentException("Input block is missing required 'AXIS' property!");
        } else if (!result.defaultBlockState().m_61138_(RotatedPillarBlock.AXIS)) {
            throw new IllegalArgumentException("Result block is missing required 'AXIS' property!");
        } else {
            if (AxeItem.STRIPPABLES instanceof ImmutableMap) {
                AxeItem.STRIPPABLES = new HashMap(AxeItem.STRIPPABLES);
            }
            AxeItem.STRIPPABLES.put(input, result);
        }
    }
}