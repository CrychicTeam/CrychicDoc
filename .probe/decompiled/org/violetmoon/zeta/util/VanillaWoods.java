package org.violetmoon.zeta.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

public class VanillaWoods {

    public static VanillaWoods.Wood OAK = new VanillaWoods.Wood("oak", Blocks.OAK_LOG, Blocks.OAK_WOOD, Blocks.OAK_PLANKS, Blocks.OAK_LEAVES, Blocks.OAK_FENCE, false, SoundType.WOOD, SoundType.WOOD);

    public static VanillaWoods.Wood SPRUCE = new VanillaWoods.Wood("spruce", Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_LEAVES, Blocks.SPRUCE_FENCE, false, SoundType.WOOD, SoundType.WOOD);

    public static VanillaWoods.Wood BIRCH = new VanillaWoods.Wood("birch", Blocks.BIRCH_LOG, Blocks.BIRCH_WOOD, Blocks.BIRCH_PLANKS, Blocks.BIRCH_LEAVES, Blocks.BIRCH_FENCE, false, SoundType.WOOD, SoundType.WOOD);

    public static VanillaWoods.Wood JUNGLE = new VanillaWoods.Wood("jungle", Blocks.JUNGLE_LOG, Blocks.JUNGLE_WOOD, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_LEAVES, Blocks.JUNGLE_FENCE, false, SoundType.WOOD, SoundType.WOOD);

    public static VanillaWoods.Wood ACACIA = new VanillaWoods.Wood("acacia", Blocks.ACACIA_LOG, Blocks.ACACIA_WOOD, Blocks.ACACIA_PLANKS, Blocks.ACACIA_LEAVES, Blocks.ACACIA_FENCE, false, SoundType.WOOD, SoundType.WOOD);

    public static VanillaWoods.Wood DARK_OAK = new VanillaWoods.Wood("dark_oak", Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_WOOD, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_LEAVES, Blocks.DARK_OAK_FENCE, false, SoundType.WOOD, SoundType.WOOD);

    public static VanillaWoods.Wood MANGROVE = new VanillaWoods.Wood("mangrove", Blocks.MANGROVE_LOG, Blocks.MANGROVE_WOOD, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_LEAVES, Blocks.MANGROVE_FENCE, false, SoundType.WOOD, SoundType.WOOD);

    public static VanillaWoods.Wood BAMBOO = new VanillaWoods.Wood("bamboo", Blocks.BAMBOO_BLOCK, null, Blocks.BAMBOO_PLANKS, null, Blocks.BAMBOO_FENCE, false, SoundType.BAMBOO_WOOD, SoundType.BAMBOO_WOOD);

    public static VanillaWoods.Wood CHERRY = new VanillaWoods.Wood("cherry", Blocks.CHERRY_LOG, Blocks.CHERRY_WOOD, Blocks.CHERRY_PLANKS, Blocks.CHERRY_LEAVES, Blocks.CHERRY_FENCE, false, SoundType.CHERRY_WOOD, SoundType.CHERRY_WOOD);

    public static VanillaWoods.Wood CRIMSON = new VanillaWoods.Wood("crimson", Blocks.CRIMSON_STEM, Blocks.CRIMSON_HYPHAE, Blocks.CRIMSON_PLANKS, null, Blocks.CRIMSON_FENCE, true, SoundType.STEM, SoundType.NETHER_WOOD);

    public static VanillaWoods.Wood WARPED = new VanillaWoods.Wood("warped", Blocks.WARPED_STEM, Blocks.WARPED_HYPHAE, Blocks.WARPED_PLANKS, null, Blocks.WARPED_FENCE, true, SoundType.STEM, SoundType.NETHER_WOOD);

    public static final VanillaWoods.Wood[] OVERWORLD_NON_OAK = new VanillaWoods.Wood[] { SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK, MANGROVE, BAMBOO, CHERRY };

    public static final VanillaWoods.Wood[] OVERWORLD = new VanillaWoods.Wood[] { OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK, MANGROVE, BAMBOO, CHERRY };

    public static final VanillaWoods.Wood[] OVERWORLD_WITH_TREE = new VanillaWoods.Wood[] { OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK, MANGROVE, CHERRY };

    public static final VanillaWoods.Wood[] NETHER = new VanillaWoods.Wood[] { CRIMSON, WARPED };

    public static final VanillaWoods.Wood[] ALL = new VanillaWoods.Wood[] { OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK, CRIMSON, WARPED, MANGROVE, BAMBOO, CHERRY };

    public static final VanillaWoods.Wood[] ALL_WITH_LOGS = new VanillaWoods.Wood[] { OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK, CRIMSON, WARPED, MANGROVE, CHERRY };

    public static final VanillaWoods.Wood[] NON_OAK = new VanillaWoods.Wood[] { SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK, CRIMSON, WARPED, MANGROVE, BAMBOO, CHERRY };

    public static record Wood(String name, Block log, Block wood, Block planks, Block leaf, Block fence, boolean nether, SoundType soundWood, SoundType soundPlanks) {
    }
}