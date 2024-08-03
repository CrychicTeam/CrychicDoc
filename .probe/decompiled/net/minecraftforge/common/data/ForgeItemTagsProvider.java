package net.minecraftforge.common.data;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public final class ForgeItemTagsProvider extends ItemTagsProvider {

    public ForgeItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, "forge", existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider lookupProvider) {
        this.m_206421_(Tags.Blocks.BARRELS, Tags.Items.BARRELS);
        this.m_206421_(Tags.Blocks.BARRELS_WOODEN, Tags.Items.BARRELS_WOODEN);
        this.m_206424_(Tags.Items.BONES).add(Items.BONE);
        this.m_206421_(Tags.Blocks.BOOKSHELVES, Tags.Items.BOOKSHELVES);
        this.m_206421_(Tags.Blocks.CHESTS, Tags.Items.CHESTS);
        this.m_206421_(Tags.Blocks.CHESTS_ENDER, Tags.Items.CHESTS_ENDER);
        this.m_206421_(Tags.Blocks.CHESTS_TRAPPED, Tags.Items.CHESTS_TRAPPED);
        this.m_206421_(Tags.Blocks.CHESTS_WOODEN, Tags.Items.CHESTS_WOODEN);
        this.m_206421_(Tags.Blocks.COBBLESTONE, Tags.Items.COBBLESTONE);
        this.m_206421_(Tags.Blocks.COBBLESTONE_NORMAL, Tags.Items.COBBLESTONE_NORMAL);
        this.m_206421_(Tags.Blocks.COBBLESTONE_INFESTED, Tags.Items.COBBLESTONE_INFESTED);
        this.m_206421_(Tags.Blocks.COBBLESTONE_MOSSY, Tags.Items.COBBLESTONE_MOSSY);
        this.m_206421_(Tags.Blocks.COBBLESTONE_DEEPSLATE, Tags.Items.COBBLESTONE_DEEPSLATE);
        this.m_206424_(Tags.Items.CROPS).addTags(new TagKey[] { Tags.Items.CROPS_BEETROOT, Tags.Items.CROPS_CARROT, Tags.Items.CROPS_NETHER_WART, Tags.Items.CROPS_POTATO, Tags.Items.CROPS_WHEAT });
        this.m_206424_(Tags.Items.CROPS_BEETROOT).add(Items.BEETROOT);
        this.m_206424_(Tags.Items.CROPS_CARROT).add(Items.CARROT);
        this.m_206424_(Tags.Items.CROPS_NETHER_WART).add(Items.NETHER_WART);
        this.m_206424_(Tags.Items.CROPS_POTATO).add(Items.POTATO);
        this.m_206424_(Tags.Items.CROPS_WHEAT).add(Items.WHEAT);
        this.m_206424_(Tags.Items.DUSTS).addTags(new TagKey[] { Tags.Items.DUSTS_GLOWSTONE, Tags.Items.DUSTS_PRISMARINE, Tags.Items.DUSTS_REDSTONE });
        this.m_206424_(Tags.Items.DUSTS_GLOWSTONE).add(Items.GLOWSTONE_DUST);
        this.m_206424_(Tags.Items.DUSTS_PRISMARINE).add(Items.PRISMARINE_SHARD);
        this.m_206424_(Tags.Items.DUSTS_REDSTONE).add(Items.REDSTONE);
        this.addColored(xva$0 -> rec$.addTags(new TagKey[] { xva$0 }), Tags.Items.DYES, "{color}_dye");
        this.m_206424_(Tags.Items.EGGS).add(Items.EGG);
        this.m_206424_(Tags.Items.ENCHANTING_FUELS).addTag(Tags.Items.GEMS_LAPIS);
        this.m_206421_(Tags.Blocks.END_STONES, Tags.Items.END_STONES);
        this.m_206424_(Tags.Items.ENDER_PEARLS).add(Items.ENDER_PEARL);
        this.m_206424_(Tags.Items.FEATHERS).add(Items.FEATHER);
        this.m_206421_(Tags.Blocks.FENCE_GATES, Tags.Items.FENCE_GATES);
        this.m_206421_(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);
        this.m_206421_(Tags.Blocks.FENCES, Tags.Items.FENCES);
        this.m_206421_(Tags.Blocks.FENCES_NETHER_BRICK, Tags.Items.FENCES_NETHER_BRICK);
        this.m_206421_(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
        this.m_206424_(Tags.Items.GEMS).addTags(new TagKey[] { Tags.Items.GEMS_AMETHYST, Tags.Items.GEMS_DIAMOND, Tags.Items.GEMS_EMERALD, Tags.Items.GEMS_LAPIS, Tags.Items.GEMS_PRISMARINE, Tags.Items.GEMS_QUARTZ });
        this.m_206424_(Tags.Items.GEMS_AMETHYST).add(Items.AMETHYST_SHARD);
        this.m_206424_(Tags.Items.GEMS_DIAMOND).add(Items.DIAMOND);
        this.m_206424_(Tags.Items.GEMS_EMERALD).add(Items.EMERALD);
        this.m_206424_(Tags.Items.GEMS_LAPIS).add(Items.LAPIS_LAZULI);
        this.m_206424_(Tags.Items.GEMS_PRISMARINE).add(Items.PRISMARINE_CRYSTALS);
        this.m_206424_(Tags.Items.GEMS_QUARTZ).add(Items.QUARTZ);
        this.m_206421_(Tags.Blocks.GLASS, Tags.Items.GLASS);
        this.m_206421_(Tags.Blocks.GLASS_TINTED, Tags.Items.GLASS_TINTED);
        this.m_206421_(Tags.Blocks.GLASS_SILICA, Tags.Items.GLASS_SILICA);
        this.copyColored(Tags.Blocks.GLASS, Tags.Items.GLASS);
        this.m_206421_(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES);
        this.copyColored(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES);
        this.m_206421_(Tags.Blocks.GRAVEL, Tags.Items.GRAVEL);
        this.m_206424_(Tags.Items.GUNPOWDER).add(Items.GUNPOWDER);
        this.m_206424_(Tags.Items.HEADS).add(Items.CREEPER_HEAD, Items.DRAGON_HEAD, Items.PLAYER_HEAD, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.ZOMBIE_HEAD);
        this.m_206424_(Tags.Items.INGOTS).addTags(new TagKey[] { Tags.Items.INGOTS_BRICK, Tags.Items.INGOTS_COPPER, Tags.Items.INGOTS_GOLD, Tags.Items.INGOTS_IRON, Tags.Items.INGOTS_NETHERITE, Tags.Items.INGOTS_NETHER_BRICK });
        this.m_206424_(Tags.Items.INGOTS_BRICK).add(Items.BRICK);
        this.m_206424_(Tags.Items.INGOTS_COPPER).add(Items.COPPER_INGOT);
        this.m_206424_(Tags.Items.INGOTS_GOLD).add(Items.GOLD_INGOT);
        this.m_206424_(Tags.Items.INGOTS_IRON).add(Items.IRON_INGOT);
        this.m_206424_(Tags.Items.INGOTS_NETHERITE).add(Items.NETHERITE_INGOT);
        this.m_206424_(Tags.Items.INGOTS_NETHER_BRICK).add(Items.NETHER_BRICK);
        this.m_206424_(Tags.Items.LEATHER).add(Items.LEATHER);
        this.m_206424_(Tags.Items.MUSHROOMS).add(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM);
        this.m_206424_(Tags.Items.NETHER_STARS).add(Items.NETHER_STAR);
        this.m_206421_(Tags.Blocks.NETHERRACK, Tags.Items.NETHERRACK);
        this.m_206424_(Tags.Items.NUGGETS).addTags(new TagKey[] { Tags.Items.NUGGETS_IRON, Tags.Items.NUGGETS_GOLD });
        this.m_206424_(Tags.Items.NUGGETS_IRON).add(Items.IRON_NUGGET);
        this.m_206424_(Tags.Items.NUGGETS_GOLD).add(Items.GOLD_NUGGET);
        this.m_206421_(Tags.Blocks.OBSIDIAN, Tags.Items.OBSIDIAN);
        this.m_206421_(Tags.Blocks.ORE_BEARING_GROUND_DEEPSLATE, Tags.Items.ORE_BEARING_GROUND_DEEPSLATE);
        this.m_206421_(Tags.Blocks.ORE_BEARING_GROUND_NETHERRACK, Tags.Items.ORE_BEARING_GROUND_NETHERRACK);
        this.m_206421_(Tags.Blocks.ORE_BEARING_GROUND_STONE, Tags.Items.ORE_BEARING_GROUND_STONE);
        this.m_206421_(Tags.Blocks.ORE_RATES_DENSE, Tags.Items.ORE_RATES_DENSE);
        this.m_206421_(Tags.Blocks.ORE_RATES_SINGULAR, Tags.Items.ORE_RATES_SINGULAR);
        this.m_206421_(Tags.Blocks.ORE_RATES_SPARSE, Tags.Items.ORE_RATES_SPARSE);
        this.m_206421_(Tags.Blocks.ORES, Tags.Items.ORES);
        this.m_206421_(Tags.Blocks.ORES_COAL, Tags.Items.ORES_COAL);
        this.m_206421_(Tags.Blocks.ORES_COPPER, Tags.Items.ORES_COPPER);
        this.m_206421_(Tags.Blocks.ORES_DIAMOND, Tags.Items.ORES_DIAMOND);
        this.m_206421_(Tags.Blocks.ORES_EMERALD, Tags.Items.ORES_EMERALD);
        this.m_206421_(Tags.Blocks.ORES_GOLD, Tags.Items.ORES_GOLD);
        this.m_206421_(Tags.Blocks.ORES_IRON, Tags.Items.ORES_IRON);
        this.m_206421_(Tags.Blocks.ORES_LAPIS, Tags.Items.ORES_LAPIS);
        this.m_206421_(Tags.Blocks.ORES_QUARTZ, Tags.Items.ORES_QUARTZ);
        this.m_206421_(Tags.Blocks.ORES_REDSTONE, Tags.Items.ORES_REDSTONE);
        this.m_206421_(Tags.Blocks.ORES_NETHERITE_SCRAP, Tags.Items.ORES_NETHERITE_SCRAP);
        this.m_206421_(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE);
        this.m_206421_(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, Tags.Items.ORES_IN_GROUND_NETHERRACK);
        this.m_206421_(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE);
        this.m_206424_(Tags.Items.RAW_MATERIALS).addTags(new TagKey[] { Tags.Items.RAW_MATERIALS_COPPER, Tags.Items.RAW_MATERIALS_GOLD, Tags.Items.RAW_MATERIALS_IRON });
        this.m_206424_(Tags.Items.RAW_MATERIALS_COPPER).add(Items.RAW_COPPER);
        this.m_206424_(Tags.Items.RAW_MATERIALS_GOLD).add(Items.RAW_GOLD);
        this.m_206424_(Tags.Items.RAW_MATERIALS_IRON).add(Items.RAW_IRON);
        this.m_206424_(Tags.Items.RODS).addTags(new TagKey[] { Tags.Items.RODS_BLAZE, Tags.Items.RODS_WOODEN });
        this.m_206424_(Tags.Items.RODS_BLAZE).add(Items.BLAZE_ROD);
        this.m_206424_(Tags.Items.RODS_WOODEN).add(Items.STICK);
        this.m_206421_(Tags.Blocks.SAND, Tags.Items.SAND);
        this.m_206421_(Tags.Blocks.SAND_COLORLESS, Tags.Items.SAND_COLORLESS);
        this.m_206421_(Tags.Blocks.SAND_RED, Tags.Items.SAND_RED);
        this.m_206421_(Tags.Blocks.SANDSTONE, Tags.Items.SANDSTONE);
        this.m_206424_(Tags.Items.SEEDS).addTags(new TagKey[] { Tags.Items.SEEDS_BEETROOT, Tags.Items.SEEDS_MELON, Tags.Items.SEEDS_PUMPKIN, Tags.Items.SEEDS_WHEAT });
        this.m_206424_(Tags.Items.SEEDS_BEETROOT).add(Items.BEETROOT_SEEDS);
        this.m_206424_(Tags.Items.SEEDS_MELON).add(Items.MELON_SEEDS);
        this.m_206424_(Tags.Items.SEEDS_PUMPKIN).add(Items.PUMPKIN_SEEDS);
        this.m_206424_(Tags.Items.SEEDS_WHEAT).add(Items.WHEAT_SEEDS);
        this.m_206424_(Tags.Items.SHEARS).add(Items.SHEARS);
        this.m_206424_(Tags.Items.SLIMEBALLS).add(Items.SLIME_BALL);
        this.m_206421_(Tags.Blocks.STAINED_GLASS, Tags.Items.STAINED_GLASS);
        this.m_206421_(Tags.Blocks.STAINED_GLASS_PANES, Tags.Items.STAINED_GLASS_PANES);
        this.m_206421_(Tags.Blocks.STONE, Tags.Items.STONE);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS_AMETHYST, Tags.Items.STORAGE_BLOCKS_AMETHYST);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS_COAL, Tags.Items.STORAGE_BLOCKS_COAL);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS_COPPER, Tags.Items.STORAGE_BLOCKS_COPPER);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS_DIAMOND, Tags.Items.STORAGE_BLOCKS_DIAMOND);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS_EMERALD, Tags.Items.STORAGE_BLOCKS_EMERALD);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS_GOLD, Tags.Items.STORAGE_BLOCKS_GOLD);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS_IRON, Tags.Items.STORAGE_BLOCKS_IRON);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS_LAPIS, Tags.Items.STORAGE_BLOCKS_LAPIS);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS_QUARTZ, Tags.Items.STORAGE_BLOCKS_QUARTZ);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS_REDSTONE, Tags.Items.STORAGE_BLOCKS_REDSTONE);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS_RAW_COPPER, Tags.Items.STORAGE_BLOCKS_RAW_COPPER);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS_RAW_GOLD, Tags.Items.STORAGE_BLOCKS_RAW_GOLD);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS_RAW_IRON, Tags.Items.STORAGE_BLOCKS_RAW_IRON);
        this.m_206421_(Tags.Blocks.STORAGE_BLOCKS_NETHERITE, Tags.Items.STORAGE_BLOCKS_NETHERITE);
        this.m_206424_(Tags.Items.STRING).add(Items.STRING);
        this.m_206424_(Tags.Items.TOOLS_SHIELDS).add(Items.SHIELD);
        this.m_206424_(Tags.Items.TOOLS_BOWS).add(Items.BOW);
        this.m_206424_(Tags.Items.TOOLS_CROSSBOWS).add(Items.CROSSBOW);
        this.m_206424_(Tags.Items.TOOLS_FISHING_RODS).add(Items.FISHING_ROD);
        this.m_206424_(Tags.Items.TOOLS_TRIDENTS).add(Items.TRIDENT);
        this.m_206424_(Tags.Items.TOOLS).addTags(new TagKey[] { ItemTags.SWORDS, ItemTags.AXES, ItemTags.PICKAXES, ItemTags.SHOVELS, ItemTags.HOES }).addTags(new TagKey[] { Tags.Items.TOOLS_SHIELDS, Tags.Items.TOOLS_BOWS, Tags.Items.TOOLS_CROSSBOWS, Tags.Items.TOOLS_FISHING_RODS, Tags.Items.TOOLS_TRIDENTS });
        this.m_206424_(Tags.Items.ARMORS_HELMETS).add(Items.LEATHER_HELMET, Items.TURTLE_HELMET, Items.CHAINMAIL_HELMET, Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.DIAMOND_HELMET, Items.NETHERITE_HELMET);
        this.m_206424_(Tags.Items.ARMORS_CHESTPLATES).add(Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE);
        this.m_206424_(Tags.Items.ARMORS_LEGGINGS).add(Items.LEATHER_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.DIAMOND_LEGGINGS, Items.NETHERITE_LEGGINGS);
        this.m_206424_(Tags.Items.ARMORS_BOOTS).add(Items.LEATHER_BOOTS, Items.CHAINMAIL_BOOTS, Items.IRON_BOOTS, Items.GOLDEN_BOOTS, Items.DIAMOND_BOOTS, Items.NETHERITE_BOOTS);
        this.m_206424_(Tags.Items.ARMORS).addTags(new TagKey[] { Tags.Items.ARMORS_HELMETS, Tags.Items.ARMORS_CHESTPLATES, Tags.Items.ARMORS_LEGGINGS, Tags.Items.ARMORS_BOOTS });
    }

    private void addColored(Consumer<TagKey<Item>> consumer, TagKey<Item> group, String pattern) {
        String prefix = group.location().getPath().toUpperCase(Locale.ENGLISH) + "_";
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation key = new ResourceLocation("minecraft", pattern.replace("{color}", color.getName()));
            TagKey<Item> tag = this.getForgeItemTag(prefix + color.getName());
            Item item = ForgeRegistries.ITEMS.getValue(key);
            if (item == null || item == Items.AIR) {
                throw new IllegalStateException("Unknown vanilla item: " + key.toString());
            }
            this.m_206424_(tag).add(item);
            consumer.accept(tag);
        }
    }

    private void copyColored(TagKey<Block> blockGroup, TagKey<Item> itemGroup) {
        String blockPre = blockGroup.location().getPath().toUpperCase(Locale.ENGLISH) + "_";
        String itemPre = itemGroup.location().getPath().toUpperCase(Locale.ENGLISH) + "_";
        for (DyeColor color : DyeColor.values()) {
            TagKey<Block> from = this.getForgeBlockTag(blockPre + color.getName());
            TagKey<Item> to = this.getForgeItemTag(itemPre + color.getName());
            this.m_206421_(from, to);
        }
        this.m_206421_(this.getForgeBlockTag(blockPre + "colorless"), this.getForgeItemTag(itemPre + "colorless"));
    }

    private TagKey<Block> getForgeBlockTag(String name) {
        try {
            name = name.toUpperCase(Locale.ENGLISH);
            return (TagKey<Block>) Tags.Blocks.class.getDeclaredField(name).get(null);
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException | IllegalArgumentException var3) {
            throw new IllegalStateException(Tags.Blocks.class.getName() + " is missing tag name: " + name);
        }
    }

    private TagKey<Item> getForgeItemTag(String name) {
        try {
            name = name.toUpperCase(Locale.ENGLISH);
            return (TagKey<Item>) Tags.Items.class.getDeclaredField(name).get(null);
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException | IllegalArgumentException var3) {
            throw new IllegalStateException(Tags.Items.class.getName() + " is missing tag name: " + name);
        }
    }

    @Override
    public String getName() {
        return "Forge Item Tags";
    }
}