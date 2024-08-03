package com.simibubi.create.foundation.utility;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

public class DyeHelper {

    public static final Map<DyeColor, Couple<Integer>> DYE_TABLE = new Builder().put(DyeColor.BLACK, Couple.create(4538427, 2170911)).put(DyeColor.RED, Couple.create(11614519, 6498103)).put(DyeColor.GREEN, Couple.create(2132550, 1925189)).put(DyeColor.BROWN, Couple.create(11306332, 6837054)).put(DyeColor.BLUE, Couple.create(5476833, 5262224)).put(DyeColor.GRAY, Couple.create(6121071, 3224888)).put(DyeColor.LIGHT_GRAY, Couple.create(9803419, 7368816)).put(DyeColor.PURPLE, Couple.create(10441902, 6501996)).put(DyeColor.CYAN, Couple.create(4107188, 3962994)).put(DyeColor.PINK, Couple.create(14002379, 12086165)).put(DyeColor.LIME, Couple.create(10739541, 5222767)).put(DyeColor.YELLOW, Couple.create(15128406, 15313961)).put(DyeColor.LIGHT_BLUE, Couple.create(6934226, 5278373)).put(DyeColor.ORANGE, Couple.create(15635014, 14240039)).put(DyeColor.MAGENTA, Couple.create(15753904, 12600456)).put(DyeColor.WHITE, Couple.create(15592165, 12302000)).build();

    public static ItemLike getWoolOfDye(DyeColor color) {
        switch(color) {
            case BLACK:
                return Blocks.BLACK_WOOL;
            case BLUE:
                return Blocks.BLUE_WOOL;
            case BROWN:
                return Blocks.BROWN_WOOL;
            case CYAN:
                return Blocks.CYAN_WOOL;
            case GRAY:
                return Blocks.GRAY_WOOL;
            case GREEN:
                return Blocks.GREEN_WOOL;
            case LIGHT_BLUE:
                return Blocks.LIGHT_BLUE_WOOL;
            case LIGHT_GRAY:
                return Blocks.LIGHT_GRAY_WOOL;
            case LIME:
                return Blocks.LIME_WOOL;
            case MAGENTA:
                return Blocks.MAGENTA_WOOL;
            case ORANGE:
                return Blocks.ORANGE_WOOL;
            case PINK:
                return Blocks.PINK_WOOL;
            case PURPLE:
                return Blocks.PURPLE_WOOL;
            case RED:
                return Blocks.RED_WOOL;
            case YELLOW:
                return Blocks.YELLOW_WOOL;
            case WHITE:
            default:
                return Blocks.WHITE_WOOL;
        }
    }
}