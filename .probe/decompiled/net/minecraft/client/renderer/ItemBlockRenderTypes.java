package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class ItemBlockRenderTypes {

    private static final Map<Block, RenderType> TYPE_BY_BLOCK = Util.make(Maps.newHashMap(), p_277225_ -> {
        RenderType $$1 = RenderType.tripwire();
        p_277225_.put(Blocks.TRIPWIRE, $$1);
        RenderType $$2 = RenderType.cutoutMipped();
        p_277225_.put(Blocks.GRASS_BLOCK, $$2);
        p_277225_.put(Blocks.IRON_BARS, $$2);
        p_277225_.put(Blocks.GLASS_PANE, $$2);
        p_277225_.put(Blocks.TRIPWIRE_HOOK, $$2);
        p_277225_.put(Blocks.HOPPER, $$2);
        p_277225_.put(Blocks.CHAIN, $$2);
        p_277225_.put(Blocks.JUNGLE_LEAVES, $$2);
        p_277225_.put(Blocks.OAK_LEAVES, $$2);
        p_277225_.put(Blocks.SPRUCE_LEAVES, $$2);
        p_277225_.put(Blocks.ACACIA_LEAVES, $$2);
        p_277225_.put(Blocks.CHERRY_LEAVES, $$2);
        p_277225_.put(Blocks.BIRCH_LEAVES, $$2);
        p_277225_.put(Blocks.DARK_OAK_LEAVES, $$2);
        p_277225_.put(Blocks.AZALEA_LEAVES, $$2);
        p_277225_.put(Blocks.FLOWERING_AZALEA_LEAVES, $$2);
        p_277225_.put(Blocks.MANGROVE_ROOTS, $$2);
        p_277225_.put(Blocks.MANGROVE_LEAVES, $$2);
        RenderType $$3 = RenderType.cutout();
        p_277225_.put(Blocks.OAK_SAPLING, $$3);
        p_277225_.put(Blocks.SPRUCE_SAPLING, $$3);
        p_277225_.put(Blocks.BIRCH_SAPLING, $$3);
        p_277225_.put(Blocks.JUNGLE_SAPLING, $$3);
        p_277225_.put(Blocks.ACACIA_SAPLING, $$3);
        p_277225_.put(Blocks.CHERRY_SAPLING, $$3);
        p_277225_.put(Blocks.DARK_OAK_SAPLING, $$3);
        p_277225_.put(Blocks.GLASS, $$3);
        p_277225_.put(Blocks.WHITE_BED, $$3);
        p_277225_.put(Blocks.ORANGE_BED, $$3);
        p_277225_.put(Blocks.MAGENTA_BED, $$3);
        p_277225_.put(Blocks.LIGHT_BLUE_BED, $$3);
        p_277225_.put(Blocks.YELLOW_BED, $$3);
        p_277225_.put(Blocks.LIME_BED, $$3);
        p_277225_.put(Blocks.PINK_BED, $$3);
        p_277225_.put(Blocks.GRAY_BED, $$3);
        p_277225_.put(Blocks.LIGHT_GRAY_BED, $$3);
        p_277225_.put(Blocks.CYAN_BED, $$3);
        p_277225_.put(Blocks.PURPLE_BED, $$3);
        p_277225_.put(Blocks.BLUE_BED, $$3);
        p_277225_.put(Blocks.BROWN_BED, $$3);
        p_277225_.put(Blocks.GREEN_BED, $$3);
        p_277225_.put(Blocks.RED_BED, $$3);
        p_277225_.put(Blocks.BLACK_BED, $$3);
        p_277225_.put(Blocks.POWERED_RAIL, $$3);
        p_277225_.put(Blocks.DETECTOR_RAIL, $$3);
        p_277225_.put(Blocks.COBWEB, $$3);
        p_277225_.put(Blocks.GRASS, $$3);
        p_277225_.put(Blocks.FERN, $$3);
        p_277225_.put(Blocks.DEAD_BUSH, $$3);
        p_277225_.put(Blocks.SEAGRASS, $$3);
        p_277225_.put(Blocks.TALL_SEAGRASS, $$3);
        p_277225_.put(Blocks.DANDELION, $$3);
        p_277225_.put(Blocks.POPPY, $$3);
        p_277225_.put(Blocks.BLUE_ORCHID, $$3);
        p_277225_.put(Blocks.ALLIUM, $$3);
        p_277225_.put(Blocks.AZURE_BLUET, $$3);
        p_277225_.put(Blocks.RED_TULIP, $$3);
        p_277225_.put(Blocks.ORANGE_TULIP, $$3);
        p_277225_.put(Blocks.WHITE_TULIP, $$3);
        p_277225_.put(Blocks.PINK_TULIP, $$3);
        p_277225_.put(Blocks.OXEYE_DAISY, $$3);
        p_277225_.put(Blocks.CORNFLOWER, $$3);
        p_277225_.put(Blocks.WITHER_ROSE, $$3);
        p_277225_.put(Blocks.LILY_OF_THE_VALLEY, $$3);
        p_277225_.put(Blocks.BROWN_MUSHROOM, $$3);
        p_277225_.put(Blocks.RED_MUSHROOM, $$3);
        p_277225_.put(Blocks.TORCH, $$3);
        p_277225_.put(Blocks.WALL_TORCH, $$3);
        p_277225_.put(Blocks.SOUL_TORCH, $$3);
        p_277225_.put(Blocks.SOUL_WALL_TORCH, $$3);
        p_277225_.put(Blocks.FIRE, $$3);
        p_277225_.put(Blocks.SOUL_FIRE, $$3);
        p_277225_.put(Blocks.SPAWNER, $$3);
        p_277225_.put(Blocks.REDSTONE_WIRE, $$3);
        p_277225_.put(Blocks.WHEAT, $$3);
        p_277225_.put(Blocks.OAK_DOOR, $$3);
        p_277225_.put(Blocks.LADDER, $$3);
        p_277225_.put(Blocks.RAIL, $$3);
        p_277225_.put(Blocks.IRON_DOOR, $$3);
        p_277225_.put(Blocks.REDSTONE_TORCH, $$3);
        p_277225_.put(Blocks.REDSTONE_WALL_TORCH, $$3);
        p_277225_.put(Blocks.CACTUS, $$3);
        p_277225_.put(Blocks.SUGAR_CANE, $$3);
        p_277225_.put(Blocks.REPEATER, $$3);
        p_277225_.put(Blocks.OAK_TRAPDOOR, $$3);
        p_277225_.put(Blocks.SPRUCE_TRAPDOOR, $$3);
        p_277225_.put(Blocks.BIRCH_TRAPDOOR, $$3);
        p_277225_.put(Blocks.JUNGLE_TRAPDOOR, $$3);
        p_277225_.put(Blocks.ACACIA_TRAPDOOR, $$3);
        p_277225_.put(Blocks.CHERRY_TRAPDOOR, $$3);
        p_277225_.put(Blocks.DARK_OAK_TRAPDOOR, $$3);
        p_277225_.put(Blocks.CRIMSON_TRAPDOOR, $$3);
        p_277225_.put(Blocks.WARPED_TRAPDOOR, $$3);
        p_277225_.put(Blocks.MANGROVE_TRAPDOOR, $$3);
        p_277225_.put(Blocks.BAMBOO_TRAPDOOR, $$3);
        p_277225_.put(Blocks.ATTACHED_PUMPKIN_STEM, $$3);
        p_277225_.put(Blocks.ATTACHED_MELON_STEM, $$3);
        p_277225_.put(Blocks.PUMPKIN_STEM, $$3);
        p_277225_.put(Blocks.MELON_STEM, $$3);
        p_277225_.put(Blocks.VINE, $$3);
        p_277225_.put(Blocks.GLOW_LICHEN, $$3);
        p_277225_.put(Blocks.LILY_PAD, $$3);
        p_277225_.put(Blocks.NETHER_WART, $$3);
        p_277225_.put(Blocks.BREWING_STAND, $$3);
        p_277225_.put(Blocks.COCOA, $$3);
        p_277225_.put(Blocks.BEACON, $$3);
        p_277225_.put(Blocks.FLOWER_POT, $$3);
        p_277225_.put(Blocks.POTTED_OAK_SAPLING, $$3);
        p_277225_.put(Blocks.POTTED_SPRUCE_SAPLING, $$3);
        p_277225_.put(Blocks.POTTED_BIRCH_SAPLING, $$3);
        p_277225_.put(Blocks.POTTED_JUNGLE_SAPLING, $$3);
        p_277225_.put(Blocks.POTTED_ACACIA_SAPLING, $$3);
        p_277225_.put(Blocks.POTTED_CHERRY_SAPLING, $$3);
        p_277225_.put(Blocks.POTTED_DARK_OAK_SAPLING, $$3);
        p_277225_.put(Blocks.POTTED_MANGROVE_PROPAGULE, $$3);
        p_277225_.put(Blocks.POTTED_FERN, $$3);
        p_277225_.put(Blocks.POTTED_DANDELION, $$3);
        p_277225_.put(Blocks.POTTED_POPPY, $$3);
        p_277225_.put(Blocks.POTTED_BLUE_ORCHID, $$3);
        p_277225_.put(Blocks.POTTED_ALLIUM, $$3);
        p_277225_.put(Blocks.POTTED_AZURE_BLUET, $$3);
        p_277225_.put(Blocks.POTTED_RED_TULIP, $$3);
        p_277225_.put(Blocks.POTTED_ORANGE_TULIP, $$3);
        p_277225_.put(Blocks.POTTED_WHITE_TULIP, $$3);
        p_277225_.put(Blocks.POTTED_PINK_TULIP, $$3);
        p_277225_.put(Blocks.POTTED_OXEYE_DAISY, $$3);
        p_277225_.put(Blocks.POTTED_CORNFLOWER, $$3);
        p_277225_.put(Blocks.POTTED_LILY_OF_THE_VALLEY, $$3);
        p_277225_.put(Blocks.POTTED_WITHER_ROSE, $$3);
        p_277225_.put(Blocks.POTTED_RED_MUSHROOM, $$3);
        p_277225_.put(Blocks.POTTED_BROWN_MUSHROOM, $$3);
        p_277225_.put(Blocks.POTTED_DEAD_BUSH, $$3);
        p_277225_.put(Blocks.POTTED_CACTUS, $$3);
        p_277225_.put(Blocks.POTTED_AZALEA, $$3);
        p_277225_.put(Blocks.POTTED_FLOWERING_AZALEA, $$3);
        p_277225_.put(Blocks.POTTED_TORCHFLOWER, $$3);
        p_277225_.put(Blocks.CARROTS, $$3);
        p_277225_.put(Blocks.POTATOES, $$3);
        p_277225_.put(Blocks.COMPARATOR, $$3);
        p_277225_.put(Blocks.ACTIVATOR_RAIL, $$3);
        p_277225_.put(Blocks.IRON_TRAPDOOR, $$3);
        p_277225_.put(Blocks.SUNFLOWER, $$3);
        p_277225_.put(Blocks.LILAC, $$3);
        p_277225_.put(Blocks.ROSE_BUSH, $$3);
        p_277225_.put(Blocks.PEONY, $$3);
        p_277225_.put(Blocks.TALL_GRASS, $$3);
        p_277225_.put(Blocks.LARGE_FERN, $$3);
        p_277225_.put(Blocks.SPRUCE_DOOR, $$3);
        p_277225_.put(Blocks.BIRCH_DOOR, $$3);
        p_277225_.put(Blocks.JUNGLE_DOOR, $$3);
        p_277225_.put(Blocks.ACACIA_DOOR, $$3);
        p_277225_.put(Blocks.CHERRY_DOOR, $$3);
        p_277225_.put(Blocks.DARK_OAK_DOOR, $$3);
        p_277225_.put(Blocks.MANGROVE_DOOR, $$3);
        p_277225_.put(Blocks.BAMBOO_DOOR, $$3);
        p_277225_.put(Blocks.END_ROD, $$3);
        p_277225_.put(Blocks.CHORUS_PLANT, $$3);
        p_277225_.put(Blocks.CHORUS_FLOWER, $$3);
        p_277225_.put(Blocks.TORCHFLOWER, $$3);
        p_277225_.put(Blocks.TORCHFLOWER_CROP, $$3);
        p_277225_.put(Blocks.PITCHER_PLANT, $$3);
        p_277225_.put(Blocks.PITCHER_CROP, $$3);
        p_277225_.put(Blocks.BEETROOTS, $$3);
        p_277225_.put(Blocks.KELP, $$3);
        p_277225_.put(Blocks.KELP_PLANT, $$3);
        p_277225_.put(Blocks.TURTLE_EGG, $$3);
        p_277225_.put(Blocks.DEAD_TUBE_CORAL, $$3);
        p_277225_.put(Blocks.DEAD_BRAIN_CORAL, $$3);
        p_277225_.put(Blocks.DEAD_BUBBLE_CORAL, $$3);
        p_277225_.put(Blocks.DEAD_FIRE_CORAL, $$3);
        p_277225_.put(Blocks.DEAD_HORN_CORAL, $$3);
        p_277225_.put(Blocks.TUBE_CORAL, $$3);
        p_277225_.put(Blocks.BRAIN_CORAL, $$3);
        p_277225_.put(Blocks.BUBBLE_CORAL, $$3);
        p_277225_.put(Blocks.FIRE_CORAL, $$3);
        p_277225_.put(Blocks.HORN_CORAL, $$3);
        p_277225_.put(Blocks.DEAD_TUBE_CORAL_FAN, $$3);
        p_277225_.put(Blocks.DEAD_BRAIN_CORAL_FAN, $$3);
        p_277225_.put(Blocks.DEAD_BUBBLE_CORAL_FAN, $$3);
        p_277225_.put(Blocks.DEAD_FIRE_CORAL_FAN, $$3);
        p_277225_.put(Blocks.DEAD_HORN_CORAL_FAN, $$3);
        p_277225_.put(Blocks.TUBE_CORAL_FAN, $$3);
        p_277225_.put(Blocks.BRAIN_CORAL_FAN, $$3);
        p_277225_.put(Blocks.BUBBLE_CORAL_FAN, $$3);
        p_277225_.put(Blocks.FIRE_CORAL_FAN, $$3);
        p_277225_.put(Blocks.HORN_CORAL_FAN, $$3);
        p_277225_.put(Blocks.DEAD_TUBE_CORAL_WALL_FAN, $$3);
        p_277225_.put(Blocks.DEAD_BRAIN_CORAL_WALL_FAN, $$3);
        p_277225_.put(Blocks.DEAD_BUBBLE_CORAL_WALL_FAN, $$3);
        p_277225_.put(Blocks.DEAD_FIRE_CORAL_WALL_FAN, $$3);
        p_277225_.put(Blocks.DEAD_HORN_CORAL_WALL_FAN, $$3);
        p_277225_.put(Blocks.TUBE_CORAL_WALL_FAN, $$3);
        p_277225_.put(Blocks.BRAIN_CORAL_WALL_FAN, $$3);
        p_277225_.put(Blocks.BUBBLE_CORAL_WALL_FAN, $$3);
        p_277225_.put(Blocks.FIRE_CORAL_WALL_FAN, $$3);
        p_277225_.put(Blocks.HORN_CORAL_WALL_FAN, $$3);
        p_277225_.put(Blocks.SEA_PICKLE, $$3);
        p_277225_.put(Blocks.CONDUIT, $$3);
        p_277225_.put(Blocks.BAMBOO_SAPLING, $$3);
        p_277225_.put(Blocks.BAMBOO, $$3);
        p_277225_.put(Blocks.POTTED_BAMBOO, $$3);
        p_277225_.put(Blocks.SCAFFOLDING, $$3);
        p_277225_.put(Blocks.STONECUTTER, $$3);
        p_277225_.put(Blocks.LANTERN, $$3);
        p_277225_.put(Blocks.SOUL_LANTERN, $$3);
        p_277225_.put(Blocks.CAMPFIRE, $$3);
        p_277225_.put(Blocks.SOUL_CAMPFIRE, $$3);
        p_277225_.put(Blocks.SWEET_BERRY_BUSH, $$3);
        p_277225_.put(Blocks.WEEPING_VINES, $$3);
        p_277225_.put(Blocks.WEEPING_VINES_PLANT, $$3);
        p_277225_.put(Blocks.TWISTING_VINES, $$3);
        p_277225_.put(Blocks.TWISTING_VINES_PLANT, $$3);
        p_277225_.put(Blocks.NETHER_SPROUTS, $$3);
        p_277225_.put(Blocks.CRIMSON_FUNGUS, $$3);
        p_277225_.put(Blocks.WARPED_FUNGUS, $$3);
        p_277225_.put(Blocks.CRIMSON_ROOTS, $$3);
        p_277225_.put(Blocks.WARPED_ROOTS, $$3);
        p_277225_.put(Blocks.POTTED_CRIMSON_FUNGUS, $$3);
        p_277225_.put(Blocks.POTTED_WARPED_FUNGUS, $$3);
        p_277225_.put(Blocks.POTTED_CRIMSON_ROOTS, $$3);
        p_277225_.put(Blocks.POTTED_WARPED_ROOTS, $$3);
        p_277225_.put(Blocks.CRIMSON_DOOR, $$3);
        p_277225_.put(Blocks.WARPED_DOOR, $$3);
        p_277225_.put(Blocks.POINTED_DRIPSTONE, $$3);
        p_277225_.put(Blocks.SMALL_AMETHYST_BUD, $$3);
        p_277225_.put(Blocks.MEDIUM_AMETHYST_BUD, $$3);
        p_277225_.put(Blocks.LARGE_AMETHYST_BUD, $$3);
        p_277225_.put(Blocks.AMETHYST_CLUSTER, $$3);
        p_277225_.put(Blocks.LIGHTNING_ROD, $$3);
        p_277225_.put(Blocks.CAVE_VINES, $$3);
        p_277225_.put(Blocks.CAVE_VINES_PLANT, $$3);
        p_277225_.put(Blocks.SPORE_BLOSSOM, $$3);
        p_277225_.put(Blocks.FLOWERING_AZALEA, $$3);
        p_277225_.put(Blocks.AZALEA, $$3);
        p_277225_.put(Blocks.MOSS_CARPET, $$3);
        p_277225_.put(Blocks.PINK_PETALS, $$3);
        p_277225_.put(Blocks.BIG_DRIPLEAF, $$3);
        p_277225_.put(Blocks.BIG_DRIPLEAF_STEM, $$3);
        p_277225_.put(Blocks.SMALL_DRIPLEAF, $$3);
        p_277225_.put(Blocks.HANGING_ROOTS, $$3);
        p_277225_.put(Blocks.SCULK_SENSOR, $$3);
        p_277225_.put(Blocks.CALIBRATED_SCULK_SENSOR, $$3);
        p_277225_.put(Blocks.SCULK_VEIN, $$3);
        p_277225_.put(Blocks.SCULK_SHRIEKER, $$3);
        p_277225_.put(Blocks.MANGROVE_PROPAGULE, $$3);
        p_277225_.put(Blocks.MANGROVE_LOG, $$3);
        p_277225_.put(Blocks.FROGSPAWN, $$3);
        RenderType $$4 = RenderType.translucent();
        p_277225_.put(Blocks.ICE, $$4);
        p_277225_.put(Blocks.NETHER_PORTAL, $$4);
        p_277225_.put(Blocks.WHITE_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.ORANGE_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.MAGENTA_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.LIGHT_BLUE_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.YELLOW_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.LIME_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.PINK_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.GRAY_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.LIGHT_GRAY_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.CYAN_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.PURPLE_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.BLUE_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.BROWN_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.GREEN_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.RED_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.BLACK_STAINED_GLASS, $$4);
        p_277225_.put(Blocks.WHITE_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.ORANGE_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.MAGENTA_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.YELLOW_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.LIME_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.PINK_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.GRAY_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.CYAN_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.PURPLE_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.BLUE_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.BROWN_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.GREEN_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.RED_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.BLACK_STAINED_GLASS_PANE, $$4);
        p_277225_.put(Blocks.SLIME_BLOCK, $$4);
        p_277225_.put(Blocks.HONEY_BLOCK, $$4);
        p_277225_.put(Blocks.FROSTED_ICE, $$4);
        p_277225_.put(Blocks.BUBBLE_COLUMN, $$4);
        p_277225_.put(Blocks.TINTED_GLASS, $$4);
    });

    private static final Map<Fluid, RenderType> TYPE_BY_FLUID = Util.make(Maps.newHashMap(), p_109290_ -> {
        RenderType $$1 = RenderType.translucent();
        p_109290_.put(Fluids.FLOWING_WATER, $$1);
        p_109290_.put(Fluids.WATER, $$1);
    });

    private static boolean renderCutout;

    public static RenderType getChunkRenderType(BlockState blockState0) {
        Block $$1 = blockState0.m_60734_();
        if ($$1 instanceof LeavesBlock) {
            return renderCutout ? RenderType.cutoutMipped() : RenderType.solid();
        } else {
            RenderType $$2 = (RenderType) TYPE_BY_BLOCK.get($$1);
            return $$2 != null ? $$2 : RenderType.solid();
        }
    }

    public static RenderType getMovingBlockRenderType(BlockState blockState0) {
        Block $$1 = blockState0.m_60734_();
        if ($$1 instanceof LeavesBlock) {
            return renderCutout ? RenderType.cutoutMipped() : RenderType.solid();
        } else {
            RenderType $$2 = (RenderType) TYPE_BY_BLOCK.get($$1);
            if ($$2 != null) {
                return $$2 == RenderType.translucent() ? RenderType.translucentMovingBlock() : $$2;
            } else {
                return RenderType.solid();
            }
        }
    }

    public static RenderType getRenderType(BlockState blockState0, boolean boolean1) {
        RenderType $$2 = getChunkRenderType(blockState0);
        if ($$2 == RenderType.translucent()) {
            if (!Minecraft.useShaderTransparency()) {
                return Sheets.translucentCullBlockSheet();
            } else {
                return boolean1 ? Sheets.translucentCullBlockSheet() : Sheets.translucentItemSheet();
            }
        } else {
            return Sheets.cutoutBlockSheet();
        }
    }

    public static RenderType getRenderType(ItemStack itemStack0, boolean boolean1) {
        Item $$2 = itemStack0.getItem();
        if ($$2 instanceof BlockItem) {
            Block $$3 = ((BlockItem) $$2).getBlock();
            return getRenderType($$3.defaultBlockState(), boolean1);
        } else {
            return boolean1 ? Sheets.translucentCullBlockSheet() : Sheets.translucentItemSheet();
        }
    }

    public static RenderType getRenderLayer(FluidState fluidState0) {
        RenderType $$1 = (RenderType) TYPE_BY_FLUID.get(fluidState0.getType());
        return $$1 != null ? $$1 : RenderType.solid();
    }

    public static void setFancy(boolean boolean0) {
        renderCutout = boolean0;
    }
}