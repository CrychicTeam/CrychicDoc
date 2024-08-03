package dev.architectury.hooks.item.tool;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class ShovelItemHooks {

    private ShovelItemHooks() {
    }

    public static void addFlattenable(Block input, BlockState result) {
        if (ShovelItem.FLATTENABLES instanceof ImmutableMap) {
            ShovelItem.FLATTENABLES = new HashMap(ShovelItem.FLATTENABLES);
        }
        ShovelItem.FLATTENABLES.put(input, result);
    }
}