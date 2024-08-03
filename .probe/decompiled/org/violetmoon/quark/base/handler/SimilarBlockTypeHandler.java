package org.violetmoon.quark.base.handler;

import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.violetmoon.quark.base.config.QuarkGeneralConfig;

public class SimilarBlockTypeHandler {

    public static List<String> getBasicShulkerBoxes() {
        return (List<String>) ImmutableSet.of(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, new Block[] { Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX }).stream().map(BuiltInRegistries.BLOCK::m_7981_).map(Objects::toString).collect(Collectors.toList());
    }

    public static boolean isShulkerBox(ItemStack stack) {
        return isShulkerBox(BuiltInRegistries.ITEM.getKey(stack.getItem())) && !stack.isEmpty() && stack.getMaxStackSize() == 1;
    }

    public static boolean isShulkerBox(ResourceLocation loc) {
        if (loc == null) {
            return false;
        } else {
            String locStr = loc.toString();
            return QuarkGeneralConfig.shulkerBoxes.contains(locStr) ? true : QuarkGeneralConfig.interpretShulkerBoxLikeBlocks && locStr.contains("shulker_box");
        }
    }
}