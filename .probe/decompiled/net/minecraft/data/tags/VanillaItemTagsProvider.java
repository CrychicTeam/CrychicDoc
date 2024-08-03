package net.minecraft.data.tags;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class VanillaItemTagsProvider extends ItemTagsProvider {

    public VanillaItemTagsProvider(PackOutput packOutput0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1, CompletableFuture<TagsProvider.TagLookup<Block>> completableFutureTagsProviderTagLookupBlock2) {
        super(packOutput0, completableFutureHolderLookupProvider1, completableFutureTagsProviderTagLookupBlock2);
    }

    @Override
    protected void addTags(HolderLookup.Provider holderLookupProvider0) {
        this.m_206421_(BlockTags.WOOL, ItemTags.WOOL);
        this.m_206421_(BlockTags.PLANKS, ItemTags.PLANKS);
        this.m_206421_(BlockTags.STONE_BRICKS, ItemTags.STONE_BRICKS);
        this.m_206421_(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
        this.m_206421_(BlockTags.STONE_BUTTONS, ItemTags.STONE_BUTTONS);
        this.m_206421_(BlockTags.BUTTONS, ItemTags.BUTTONS);
        this.m_206421_(BlockTags.WOOL_CARPETS, ItemTags.WOOL_CARPETS);
        this.m_206421_(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        this.m_206421_(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        this.m_206421_(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        this.m_206421_(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        this.m_206421_(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
        this.m_206421_(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        this.m_206421_(BlockTags.DOORS, ItemTags.DOORS);
        this.m_206421_(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
        this.m_206421_(BlockTags.BAMBOO_BLOCKS, ItemTags.BAMBOO_BLOCKS);
        this.m_206421_(BlockTags.OAK_LOGS, ItemTags.OAK_LOGS);
        this.m_206421_(BlockTags.DARK_OAK_LOGS, ItemTags.DARK_OAK_LOGS);
        this.m_206421_(BlockTags.BIRCH_LOGS, ItemTags.BIRCH_LOGS);
        this.m_206421_(BlockTags.ACACIA_LOGS, ItemTags.ACACIA_LOGS);
        this.m_206421_(BlockTags.SPRUCE_LOGS, ItemTags.SPRUCE_LOGS);
        this.m_206421_(BlockTags.MANGROVE_LOGS, ItemTags.MANGROVE_LOGS);
        this.m_206421_(BlockTags.JUNGLE_LOGS, ItemTags.JUNGLE_LOGS);
        this.m_206421_(BlockTags.CHERRY_LOGS, ItemTags.CHERRY_LOGS);
        this.m_206421_(BlockTags.CRIMSON_STEMS, ItemTags.CRIMSON_STEMS);
        this.m_206421_(BlockTags.WARPED_STEMS, ItemTags.WARPED_STEMS);
        this.m_206421_(BlockTags.WART_BLOCKS, ItemTags.WART_BLOCKS);
        this.m_206421_(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
        this.m_206421_(BlockTags.LOGS, ItemTags.LOGS);
        this.m_206421_(BlockTags.SAND, ItemTags.SAND);
        this.m_206421_(BlockTags.SMELTS_TO_GLASS, ItemTags.SMELTS_TO_GLASS);
        this.m_206421_(BlockTags.SLABS, ItemTags.SLABS);
        this.m_206421_(BlockTags.WALLS, ItemTags.WALLS);
        this.m_206421_(BlockTags.STAIRS, ItemTags.STAIRS);
        this.m_206421_(BlockTags.ANVIL, ItemTags.ANVIL);
        this.m_206421_(BlockTags.RAILS, ItemTags.RAILS);
        this.m_206421_(BlockTags.LEAVES, ItemTags.LEAVES);
        this.m_206421_(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
        this.m_206421_(BlockTags.TRAPDOORS, ItemTags.TRAPDOORS);
        this.m_206421_(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS);
        this.m_206421_(BlockTags.BEDS, ItemTags.BEDS);
        this.m_206421_(BlockTags.FENCES, ItemTags.FENCES);
        this.m_206421_(BlockTags.TALL_FLOWERS, ItemTags.TALL_FLOWERS);
        this.m_206421_(BlockTags.FLOWERS, ItemTags.FLOWERS);
        this.m_206421_(BlockTags.SOUL_FIRE_BASE_BLOCKS, ItemTags.SOUL_FIRE_BASE_BLOCKS);
        this.m_206421_(BlockTags.CANDLES, ItemTags.CANDLES);
        this.m_206421_(BlockTags.DAMPENS_VIBRATIONS, ItemTags.DAMPENS_VIBRATIONS);
        this.m_206421_(BlockTags.GOLD_ORES, ItemTags.GOLD_ORES);
        this.m_206421_(BlockTags.IRON_ORES, ItemTags.IRON_ORES);
        this.m_206421_(BlockTags.DIAMOND_ORES, ItemTags.DIAMOND_ORES);
        this.m_206421_(BlockTags.REDSTONE_ORES, ItemTags.REDSTONE_ORES);
        this.m_206421_(BlockTags.LAPIS_ORES, ItemTags.LAPIS_ORES);
        this.m_206421_(BlockTags.COAL_ORES, ItemTags.COAL_ORES);
        this.m_206421_(BlockTags.EMERALD_ORES, ItemTags.EMERALD_ORES);
        this.m_206421_(BlockTags.COPPER_ORES, ItemTags.COPPER_ORES);
        this.m_206421_(BlockTags.DIRT, ItemTags.DIRT);
        this.m_206421_(BlockTags.TERRACOTTA, ItemTags.TERRACOTTA);
        this.m_206421_(BlockTags.COMPLETES_FIND_TREE_TUTORIAL, ItemTags.COMPLETES_FIND_TREE_TUTORIAL);
        this.m_206424_(ItemTags.BANNERS).add(Items.WHITE_BANNER, Items.ORANGE_BANNER, Items.MAGENTA_BANNER, Items.LIGHT_BLUE_BANNER, Items.YELLOW_BANNER, Items.LIME_BANNER, Items.PINK_BANNER, Items.GRAY_BANNER, Items.LIGHT_GRAY_BANNER, Items.CYAN_BANNER, Items.PURPLE_BANNER, Items.BLUE_BANNER, Items.BROWN_BANNER, Items.GREEN_BANNER, Items.RED_BANNER, Items.BLACK_BANNER);
        this.m_206424_(ItemTags.BOATS).add(Items.OAK_BOAT, Items.SPRUCE_BOAT, Items.BIRCH_BOAT, Items.JUNGLE_BOAT, Items.ACACIA_BOAT, Items.DARK_OAK_BOAT, Items.MANGROVE_BOAT, Items.BAMBOO_RAFT, Items.CHERRY_BOAT).addTag(ItemTags.CHEST_BOATS);
        this.m_206424_(ItemTags.CHEST_BOATS).add(Items.OAK_CHEST_BOAT, Items.SPRUCE_CHEST_BOAT, Items.BIRCH_CHEST_BOAT, Items.JUNGLE_CHEST_BOAT, Items.ACACIA_CHEST_BOAT, Items.DARK_OAK_CHEST_BOAT, Items.MANGROVE_CHEST_BOAT, Items.BAMBOO_CHEST_RAFT, Items.CHERRY_CHEST_BOAT);
        this.m_206424_(ItemTags.FISHES).add(Items.COD, Items.COOKED_COD, Items.SALMON, Items.COOKED_SALMON, Items.PUFFERFISH, Items.TROPICAL_FISH);
        this.m_206421_(BlockTags.STANDING_SIGNS, ItemTags.SIGNS);
        this.m_206421_(BlockTags.CEILING_HANGING_SIGNS, ItemTags.HANGING_SIGNS);
        this.m_206424_(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(Items.MUSIC_DISC_13, Items.MUSIC_DISC_CAT, Items.MUSIC_DISC_BLOCKS, Items.MUSIC_DISC_CHIRP, Items.MUSIC_DISC_FAR, Items.MUSIC_DISC_MALL, Items.MUSIC_DISC_MELLOHI, Items.MUSIC_DISC_STAL, Items.MUSIC_DISC_STRAD, Items.MUSIC_DISC_WARD, Items.MUSIC_DISC_11, Items.MUSIC_DISC_WAIT);
        this.m_206424_(ItemTags.MUSIC_DISCS).addTag(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(Items.MUSIC_DISC_PIGSTEP).add(Items.MUSIC_DISC_OTHERSIDE).add(Items.MUSIC_DISC_5).add(Items.MUSIC_DISC_RELIC);
        this.m_206424_(ItemTags.COALS).add(Items.COAL, Items.CHARCOAL);
        this.m_206424_(ItemTags.ARROWS).add(Items.ARROW, Items.TIPPED_ARROW, Items.SPECTRAL_ARROW);
        this.m_206424_(ItemTags.LECTERN_BOOKS).add(Items.WRITTEN_BOOK, Items.WRITABLE_BOOK);
        this.m_206424_(ItemTags.BEACON_PAYMENT_ITEMS).add(Items.NETHERITE_INGOT, Items.EMERALD, Items.DIAMOND, Items.GOLD_INGOT, Items.IRON_INGOT);
        this.m_206424_(ItemTags.PIGLIN_REPELLENTS).add(Items.SOUL_TORCH).add(Items.SOUL_LANTERN).add(Items.SOUL_CAMPFIRE);
        this.m_206424_(ItemTags.PIGLIN_LOVED).addTag(ItemTags.GOLD_ORES).add(Items.GOLD_BLOCK, Items.GILDED_BLACKSTONE, Items.LIGHT_WEIGHTED_PRESSURE_PLATE, Items.GOLD_INGOT, Items.BELL, Items.CLOCK, Items.GOLDEN_CARROT, Items.GLISTERING_MELON_SLICE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS, Items.GOLDEN_HORSE_ARMOR, Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.RAW_GOLD, Items.RAW_GOLD_BLOCK);
        this.m_206424_(ItemTags.IGNORED_BY_PIGLIN_BABIES).add(Items.LEATHER);
        this.m_206424_(ItemTags.PIGLIN_FOOD).add(Items.PORKCHOP, Items.COOKED_PORKCHOP);
        this.m_206424_(ItemTags.FOX_FOOD).add(Items.SWEET_BERRIES, Items.GLOW_BERRIES);
        this.m_206424_(ItemTags.NON_FLAMMABLE_WOOD).add(Items.WARPED_STEM, Items.STRIPPED_WARPED_STEM, Items.WARPED_HYPHAE, Items.STRIPPED_WARPED_HYPHAE, Items.CRIMSON_STEM, Items.STRIPPED_CRIMSON_STEM, Items.CRIMSON_HYPHAE, Items.STRIPPED_CRIMSON_HYPHAE, Items.CRIMSON_PLANKS, Items.WARPED_PLANKS, Items.CRIMSON_SLAB, Items.WARPED_SLAB, Items.CRIMSON_PRESSURE_PLATE, Items.WARPED_PRESSURE_PLATE, Items.CRIMSON_FENCE, Items.WARPED_FENCE, Items.CRIMSON_TRAPDOOR, Items.WARPED_TRAPDOOR, Items.CRIMSON_FENCE_GATE, Items.WARPED_FENCE_GATE, Items.CRIMSON_STAIRS, Items.WARPED_STAIRS, Items.CRIMSON_BUTTON, Items.WARPED_BUTTON, Items.CRIMSON_DOOR, Items.WARPED_DOOR, Items.CRIMSON_SIGN, Items.WARPED_SIGN, Items.WARPED_HANGING_SIGN, Items.CRIMSON_HANGING_SIGN);
        this.m_206424_(ItemTags.STONE_TOOL_MATERIALS).add(Items.COBBLESTONE, Items.BLACKSTONE, Items.COBBLED_DEEPSLATE);
        this.m_206424_(ItemTags.STONE_CRAFTING_MATERIALS).add(Items.COBBLESTONE, Items.BLACKSTONE, Items.COBBLED_DEEPSLATE);
        this.m_206424_(ItemTags.FREEZE_IMMUNE_WEARABLES).add(Items.LEATHER_BOOTS, Items.LEATHER_LEGGINGS, Items.LEATHER_CHESTPLATE, Items.LEATHER_HELMET, Items.LEATHER_HORSE_ARMOR);
        this.m_206424_(ItemTags.AXOLOTL_TEMPT_ITEMS).add(Items.TROPICAL_FISH_BUCKET);
        this.m_206424_(ItemTags.CLUSTER_MAX_HARVESTABLES).add(Items.DIAMOND_PICKAXE, Items.GOLDEN_PICKAXE, Items.IRON_PICKAXE, Items.NETHERITE_PICKAXE, Items.STONE_PICKAXE, Items.WOODEN_PICKAXE);
        this.m_206424_(ItemTags.COMPASSES).add(Items.COMPASS).add(Items.RECOVERY_COMPASS);
        this.m_206424_(ItemTags.CREEPER_IGNITERS).add(Items.FLINT_AND_STEEL).add(Items.FIRE_CHARGE);
        this.m_206424_(ItemTags.SWORDS).add(Items.DIAMOND_SWORD).add(Items.STONE_SWORD).add(Items.GOLDEN_SWORD).add(Items.NETHERITE_SWORD).add(Items.WOODEN_SWORD).add(Items.IRON_SWORD);
        this.m_206424_(ItemTags.AXES).add(Items.DIAMOND_AXE).add(Items.STONE_AXE).add(Items.GOLDEN_AXE).add(Items.NETHERITE_AXE).add(Items.WOODEN_AXE).add(Items.IRON_AXE);
        this.m_206424_(ItemTags.PICKAXES).add(Items.DIAMOND_PICKAXE).add(Items.STONE_PICKAXE).add(Items.GOLDEN_PICKAXE).add(Items.NETHERITE_PICKAXE).add(Items.WOODEN_PICKAXE).add(Items.IRON_PICKAXE);
        this.m_206424_(ItemTags.SHOVELS).add(Items.DIAMOND_SHOVEL).add(Items.STONE_SHOVEL).add(Items.GOLDEN_SHOVEL).add(Items.NETHERITE_SHOVEL).add(Items.WOODEN_SHOVEL).add(Items.IRON_SHOVEL);
        this.m_206424_(ItemTags.HOES).add(Items.DIAMOND_HOE).add(Items.STONE_HOE).add(Items.GOLDEN_HOE).add(Items.NETHERITE_HOE).add(Items.WOODEN_HOE).add(Items.IRON_HOE);
        this.m_206424_(ItemTags.TOOLS).addTag(ItemTags.SWORDS).addTag(ItemTags.AXES).addTag(ItemTags.PICKAXES).addTag(ItemTags.SHOVELS).addTag(ItemTags.HOES).add(Items.TRIDENT);
        this.m_206424_(ItemTags.BREAKS_DECORATED_POTS).addTag(ItemTags.TOOLS);
        this.m_206424_(ItemTags.DECORATED_POT_SHERDS).add(Items.ANGLER_POTTERY_SHERD, Items.ARCHER_POTTERY_SHERD, Items.ARMS_UP_POTTERY_SHERD, Items.BLADE_POTTERY_SHERD, Items.BREWER_POTTERY_SHERD, Items.BURN_POTTERY_SHERD, Items.DANGER_POTTERY_SHERD, Items.EXPLORER_POTTERY_SHERD, Items.FRIEND_POTTERY_SHERD, Items.HEART_POTTERY_SHERD, Items.HEARTBREAK_POTTERY_SHERD, Items.HOWL_POTTERY_SHERD, Items.MINER_POTTERY_SHERD, Items.MOURNER_POTTERY_SHERD, Items.PLENTY_POTTERY_SHERD, Items.PRIZE_POTTERY_SHERD, Items.SHEAF_POTTERY_SHERD, Items.SHELTER_POTTERY_SHERD, Items.SKULL_POTTERY_SHERD, Items.SNORT_POTTERY_SHERD);
        this.m_206424_(ItemTags.DECORATED_POT_INGREDIENTS).add(Items.BRICK).addTag(ItemTags.DECORATED_POT_SHERDS);
        this.m_206424_(ItemTags.TRIMMABLE_ARMOR).add(Items.NETHERITE_HELMET).add(Items.NETHERITE_CHESTPLATE).add(Items.NETHERITE_LEGGINGS).add(Items.NETHERITE_BOOTS).add(Items.DIAMOND_HELMET).add(Items.DIAMOND_CHESTPLATE).add(Items.DIAMOND_LEGGINGS).add(Items.DIAMOND_BOOTS).add(Items.GOLDEN_HELMET).add(Items.GOLDEN_CHESTPLATE).add(Items.GOLDEN_LEGGINGS).add(Items.GOLDEN_BOOTS).add(Items.IRON_HELMET).add(Items.IRON_CHESTPLATE).add(Items.IRON_LEGGINGS).add(Items.IRON_BOOTS).add(Items.CHAINMAIL_HELMET).add(Items.CHAINMAIL_CHESTPLATE).add(Items.CHAINMAIL_LEGGINGS).add(Items.CHAINMAIL_BOOTS).add(Items.LEATHER_HELMET).add(Items.LEATHER_CHESTPLATE).add(Items.LEATHER_LEGGINGS).add(Items.LEATHER_BOOTS).add(Items.TURTLE_HELMET);
        this.m_206424_(ItemTags.TRIM_MATERIALS).add(Items.IRON_INGOT).add(Items.COPPER_INGOT).add(Items.GOLD_INGOT).add(Items.LAPIS_LAZULI).add(Items.EMERALD).add(Items.DIAMOND).add(Items.NETHERITE_INGOT).add(Items.REDSTONE).add(Items.QUARTZ).add(Items.AMETHYST_SHARD);
        this.m_206424_(ItemTags.TRIM_TEMPLATES).add(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE).add(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE);
        this.m_206424_(ItemTags.BOOKSHELF_BOOKS).add(Items.BOOK, Items.WRITTEN_BOOK, Items.ENCHANTED_BOOK, Items.WRITABLE_BOOK, Items.KNOWLEDGE_BOOK);
        this.m_206424_(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS).add(Items.ZOMBIE_HEAD, Items.SKELETON_SKULL, Items.CREEPER_HEAD, Items.DRAGON_HEAD, Items.WITHER_SKELETON_SKULL, Items.PIGLIN_HEAD, Items.PLAYER_HEAD);
        this.m_206424_(ItemTags.SNIFFER_FOOD).add(Items.TORCHFLOWER_SEEDS);
        this.m_206424_(ItemTags.VILLAGER_PLANTABLE_SEEDS).add(Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD);
    }
}